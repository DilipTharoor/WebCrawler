package com.hw.webcrawler.core.reader;

import com.hw.webcrawler.exception.WebCrawlerException;
import com.hw.webcrawler.response.CrawlReadResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Generator to convert the data stored in DB to a {@link CrawlData} object
 *
 * @author Dilip Tharoor
 */
@Service
public interface CrawlResponseGenerator {


    /**
     * Start creating the response starting from this id.
     * @param id
     * @return
     */
    CrawlReadResponse generateResponse(Long id) throws IOException, WebCrawlerException;
}
