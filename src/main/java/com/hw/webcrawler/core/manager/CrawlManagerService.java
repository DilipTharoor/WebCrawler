package com.hw.webcrawler.core.manager;

import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Service to manager the crawling.  The crawling is implemented using worker threads.  Each finished thread persists
 * the data into DB so that the next free thread can continue parsing until it reaches the required maximum depth
 *
 * @author Dilip Tharoor
 */
@Service
public interface CrawlManagerService {

    /**
     * This is the starting point for the crawling which returns the root id from the DB,
     * so that it can be used to gets the crawl data
     * @param startUrl holds the starting point for the crawling
     * @param maxDepth holds depth to which the crawling should be done.
     * @return the id from the DB where the starting url is stored
     */
    Long startCrawling(String startUrl, int maxDepth) throws IOException;

    /**
     * Adds the next url to parse to the queue
     * @param urlToParse to parse
     * @param currentDepth of the current url
     */
    void addToQueue(String urlToParse, int currentDepth);

    /**
     * This is called once the queue has parsed the url.  The function persists the data and then invokes the next
     * set of urls to parse
     * @param parsedData
     * @param currentDepth
     */
    void persistAndContinue(PersistData parsedData, int currentDepth) throws IOException;

    /**
     * Stop the current crawling process
     */
    void stopCrawling();
}
