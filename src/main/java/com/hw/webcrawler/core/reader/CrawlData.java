package com.hw.webcrawler.core.reader;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * The data that needs to be returned to the api
 *
 * @author Dilip Tharoor
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CrawlData {

    private String title;
    private String url;
    private List<CrawlData> nodes;

    public CrawlData(String title, String url, List<CrawlData> links) {
        this.title = title;
        this.url = url;
        this.nodes = links;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<CrawlData> getNodes() {
        return nodes;
    }

    public void setNodes(List<CrawlData> nodes) {
        this.nodes = nodes;
    }
}
