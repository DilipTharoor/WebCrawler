
package com.hw.webcrawler.core.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.hw.webcrawler.config.CrawlManagerConfigProperties;
import com.hw.webcrawler.config.JsonConfiguration;
import com.hw.webcrawler.core.parser.URLParser;
import com.hw.webcrawler.entity.UrlInfoEntity;
import com.hw.webcrawler.repository.CrawlInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * {@inheritDoc}
 *
 * @author Dilip Tharoor
 */
@Service
public class CrawlManagerServiceImpl implements CrawlManagerService {

    private static final Logger logger = LoggerFactory.getLogger(CrawlManagerServiceImpl.class);

    private ThreadPoolExecutor threadPoolExecutor;
    private ListeningExecutorService executorService;
    private final CrawlManagerConfigProperties crawlManagerConfigProperties;
    private final CrawlInfoRepository crawlInfoRepository;
    private final URLParser urlParser;

    private int maxDepth;
    private boolean shouldCrawlingContinue = true;

    /**
     * The cache is used to store the jobs that are being parsed and not yet persisted.  This makes sure that the
     * queues don't take any task that is still being executed.  Along with DB, this makes sure that every new
     * execution is done only on fresh urls.
     */
    private static final Set<String> cache = new HashSet<>();

    private CrawlManagerServiceImpl(CrawlManagerConfigProperties crawlManagerConfigProperties,
                                    CrawlInfoRepository crawlInfoRepository,
                                    URLParser urlParser) {
        this.crawlManagerConfigProperties = crawlManagerConfigProperties;
        this.crawlInfoRepository = crawlInfoRepository;
        this.urlParser = urlParser;
    }

    private ListeningExecutorService getExecutorService(int nThreads, int queueSize, long keepAlive) {
        this.threadPoolExecutor = new ThreadPoolExecutor(
                nThreads,
                nThreads,
                keepAlive,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(queueSize, true)
        );

        return MoreExecutors.listeningDecorator(
                this.threadPoolExecutor
        );
    }

    @PostConstruct
    void setup() {
        this.executorService = getExecutorService(
                crawlManagerConfigProperties.getThreadCount(),
                crawlManagerConfigProperties.getQueueSize(),
                crawlManagerConfigProperties.getKeepAlive());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long startCrawling(String startUrl, int maxDepth) throws IOException {

        logger.info("Started Crawling...");

        this.shouldCrawlingContinue = true;

        // Clear the complete database before crawling
        this.crawlInfoRepository.deleteAll();

        // This is the maximum depth to which crawling should be done
        this.maxDepth = maxDepth;

        // Crawl the root url
        PersistData crawlData = urlParser.fetchURLInfo(startUrl);
        Long rootId = persisToDB(crawlData, 0);

        //trigger the crawling for all the child nodes recursively
        fetchAndRunNewUrls();

        return rootId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addToQueue(String urlToParse, int currentDepth) {

        logger.info("Thread status : "+this.threadPoolExecutor.toString());
        try {

            // Send the job to executor service
            CompletableFuture<PersistData> futureResponse = CompletableFuture.supplyAsync(new CrawlExecutor(
                    urlToParse, urlParser), executorService);

            // Add a callback once the job is completed
            futureResponse.thenAcceptAsync(urlInfo -> {
                try {
                    this.persistAndContinue(urlInfo, currentDepth);
                } catch (IOException e) {
                    logger.error("Json Processing exception " + e);
                }
            }, executorService);

        } catch(RejectedExecutionException ex) {
            // Nothing to do when the pool size reached.  The next free queue will take up the next link
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void persistAndContinue(PersistData parsedData, int currentDepth) throws IOException {

        // Write the parsed data to DB
        persisToDB(parsedData, currentDepth);

        // remove from cache to show that the parsing is done for this url
        cache.remove(parsedData.getUrl());

        // Recursive call to run next set of urls
        fetchAndRunNewUrls();
    }

    @Override
    public void stopCrawling() {

        shouldCrawlingContinue = false;

        logger.info("Crawling stopped...");

    }

    private Long persisToDB(PersistData urlInfo, int depth) throws JsonProcessingException {

        UrlInfoEntity entity;

        entity = new UrlInfoEntity();

        entity.setUrl(urlInfo.getUrl());
        entity.setTitle(urlInfo.getTitle());
        entity.setLinks(JsonConfiguration.JSON_MAPPER.writeValueAsString(urlInfo.getNodes()));
        entity.setDepth(depth);

        // write to DB
        crawlInfoRepository.save(entity);

        return entity.getId();
    }

    /**
     * Needs to be thread safe so that the same resource is not used by multiple threads leading to wasted parsing
     * of the same url.
     */
    private synchronized void fetchAndRunNewUrls() throws IOException {


        if(!shouldCrawlingContinue) {
            return;
        }

        // the entity which contains the links that needs to be parsed next
        UrlInfoEntity urlInfoEntity = crawlInfoRepository.findNextEligibleUrl(new PageRequest(0, 1)).get(0);

        // Starting the next level in the BFS search
        int depth = urlInfoEntity.getDepth() + 1;

        // When the max depth has reached for this executor, do not continue;
        if(depth >= maxDepth) {
            logger.info("Reached the required depth for this thread.  Stopping thread...");
            return;
        }

        Set<String> links = JsonConfiguration.JSON_MAPPER.readerFor(Set.class).readValue(urlInfoEntity.getLinks());

        for (String link : links) {

            /*
             * A new entry should be fetched only if
             * 1 : It is not in the cache (this means that the thread has started parsing but have not persisted)
             * 2 : It is not in DB (this means that an earlier thread parsed this url already)
             */
            if((!cache.contains(link) && crawlInfoRepository.findByUrl(link).isEmpty()) ) {

                // Add it to cache until it is persisted to DB
                cache.add(link);

                // ready for parsing
                addToQueue(link, depth);
            }
        }

        // all the links are parsed for this depth if it reaches here
        urlInfoEntity.setParseCompleted(true);

        logger.info("All the links parsed for " + urlInfoEntity.getUrl());

        // update the entry in the DB
        crawlInfoRepository.save(urlInfoEntity);

    }
}
