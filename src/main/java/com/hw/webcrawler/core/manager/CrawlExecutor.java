package com.hw.webcrawler.core.manager;

import com.hw.webcrawler.core.parser.URLParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.function.Supplier;


/**
 * Supplier object to fetch each url and return the parsed data.
 * This is used by the executor service to run in the assigned worker thread
 *
 * @author Dilip Tharoor
 */
class CrawlExecutor implements Supplier<PersistData> {

    private static final Logger logger = LoggerFactory.getLogger(CrawlExecutor.class);

    private final String url;
    private final URLParser urlParser;

    CrawlExecutor(String url, URLParser urlParser) {
        this.url = url;
        this.urlParser = urlParser;
    }

    /**
     * This gets called when the queue is ready to execute
     * @return the parsed content
     */
    @Override
    public PersistData get(){
        PersistData data = null;
        try {
            data = urlParser.fetchURLInfo(this.url);
        } catch (IOException e) {
            logger.error("URL parsing error : " + e);
        }
        return data;
    }
}
