package com.hw.webcrawler.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * CrawlRequest holds the request to be sent to the api to trigger the web crawl
 *
 * @author Dilip Tharoor
 */
@ApiModel(
        description = "Request for the crawl operation"
)
public class CrawlRequest {

    @ApiModelProperty("Starting url for the crawl operation")
    private
    String url;

    @ApiModelProperty("Depth to which crawling needs to be done")
    private
    int depth;

    public CrawlRequest(@JsonProperty("url") String url, @JsonProperty("depth") int depth) {
        this.url = url;
        this.depth = depth;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
