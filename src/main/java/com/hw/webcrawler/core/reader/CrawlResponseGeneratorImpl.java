package com.hw.webcrawler.core.reader;

import com.hw.webcrawler.config.JsonConfiguration;
import com.hw.webcrawler.entity.UrlInfoEntity;
import com.hw.webcrawler.exception.WebCrawlerException;
import com.hw.webcrawler.repository.CrawlInfoRepository;
import com.hw.webcrawler.response.CrawlReadResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * {@inheritDoc}
 *
 * @author Dilip Tharoor
 */
@Service
public class CrawlResponseGeneratorImpl implements CrawlResponseGenerator{

    private static final Logger logger = LoggerFactory.getLogger(CrawlResponseGeneratorImpl.class);

    private final CrawlInfoRepository crawlInfoRepository;

    /**
     * To hold all the ids already read
     */
    private final Set<Long> readIds = new HashSet<>();

    private CrawlResponseGeneratorImpl(CrawlInfoRepository crawlInfoRepository) {
        this.crawlInfoRepository = crawlInfoRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CrawlReadResponse generateResponse(Long id) throws IOException, WebCrawlerException {

        logger.info("Started reading crawled data from DB ...");

        readIds.clear();

        Optional<UrlInfoEntity> urlInfoEntities = crawlInfoRepository.findById(id);

        if(!urlInfoEntities.isPresent()) {
            throw new WebCrawlerException("The id:"+id+" provided was not correct");
        }

        return new CrawlReadResponse(fetchCrawlData(urlInfoEntities.get(), 1));

    }

    private CrawlData fetchCrawlData(UrlInfoEntity urlInfoEntity, int depth) throws IOException {

        readIds.add(urlInfoEntity.getId());

        List<String> nodesList = JsonConfiguration.JSON_MAPPER.readerFor(List.class).readValue(urlInfoEntity.getLinks());

        List<CrawlData> nodes = new ArrayList<>();

        // Go through all the links and then call fetchCrawlData recursively
        for (String link : nodesList) {

            // Check the link in DB
            List<UrlInfoEntity> idList = crawlInfoRepository.findByUrl(link);

            // If it is there in DB, need to fetch all the links for that entry
            if (!idList.isEmpty()) {

                // Get the id for each link
                UrlInfoEntity entity = idList.get(0);

                if(entity.getDepth() != depth) {
                    continue;
                }

                // If the link is already read then do not call recursively
                if (readIds.contains(entity.getId())) {
                    continue;
                }

                // call the link recursively
                nodes.add(fetchCrawlData(crawlInfoRepository.findById(entity.getId()).get(), depth+1));

            }
        }

        return new CrawlData(
                urlInfoEntity.getTitle(),
                urlInfoEntity.getUrl(),
                nodes
        );

    }

}
