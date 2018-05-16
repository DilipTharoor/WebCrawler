package com.hw.webcrawler.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
        description = "Root id for the crawl."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CrawlResponse implements ResponseBase {

    @ApiModelProperty("Root id to trigger the read once the crawling is completed")
    @JsonProperty
    private final Long value;

    public CrawlResponse(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }
}
