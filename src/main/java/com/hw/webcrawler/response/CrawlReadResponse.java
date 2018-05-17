package com.hw.webcrawler.response;

import com.hw.webcrawler.core.reader.CrawlData;

/**
 * Response object to return to the api for the crawl fetch request
 *
 * @author Dilip Tharoor
 */
public class CrawlReadResponse implements ResponseBase {

    private CrawlData crawledData;

    public CrawlReadResponse(CrawlData urlInfo) {
        this.crawledData = urlInfo;
    }

    public CrawlData getCrawledData() {
        return crawledData;
    }

    public void setCrawledData(CrawlData crawledData) {
        this.crawledData = crawledData;
    }
}
