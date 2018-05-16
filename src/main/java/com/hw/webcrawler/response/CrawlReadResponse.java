package com.hw.webcrawler.response;

import com.hw.webcrawler.core.reader.CrawlData;

/**
 * Response object to return to the api for the crawl fetch request
 *
 * @author Dilip Tharoor
 */
public class CrawlReadResponse implements ResponseBase {

    private CrawlData crawlDataResponse;

    public CrawlReadResponse(CrawlData urlInfo) {
        this.crawlDataResponse = urlInfo;
    }

    public CrawlData getCrawlDataResponse() {
        return crawlDataResponse;
    }

    public void setCrawlDataResponse(CrawlData crawlDataResponse) {
        this.crawlDataResponse = crawlDataResponse;
    }
}
