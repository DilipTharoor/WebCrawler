package com.hw.webcrawler.api;

import com.hw.webcrawler.core.manager.CrawlManagerService;
import com.hw.webcrawler.core.reader.CrawlResponseGenerator;
import com.hw.webcrawler.exception.WebCrawlerException;
import com.hw.webcrawler.request.CrawlRequest;
import com.hw.webcrawler.response.CrawlResponse;
import com.hw.webcrawler.response.ErrorResponse;
import com.hw.webcrawler.response.ResponseBase;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Rest Controller for Crawl requests
 *
 * @author Dilip Tharoor
 */
@RestController
@RequestMapping("/api/url")
class CrawlController {

    private static final Logger logger = LoggerFactory.getLogger(CrawlController.class);

    private CrawlManagerService crawlManagerService;
    private CrawlResponseGenerator crawlResponseGenerator;

    private CrawlController(CrawlManagerService crawlManagerService, CrawlResponseGenerator crawlResponseGenerator) {
        this.crawlManagerService = crawlManagerService;
        this.crawlResponseGenerator = crawlResponseGenerator;
    }

    /**
     * Start the web crawler for the specific url and end at depth level specified
     *
     * @return ResponseEntity
     */
    @ApiOperation(
            tags = "Crawl",
            value = "Start crawling the web, starting at the specified URL",
            notes = "Start crawling the web, starting at the specified URL",
            nickname = "startWEBCrawlWithURLUsingPOST")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully started the web crawler", response = ResponseBase.class),
            @ApiResponse(code = 400, message = "Supplied request is invalid"),
            @ApiResponse(code = 500, message = "Crawl error")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ResponseBase> startCrawling(@RequestBody CrawlRequest body) {

        ResponseBase response;
        HttpStatus responseStatus;
        try {
            Long id = this.crawlManagerService.startCrawling(body.getUrl(), body.getDepth());
            response = new CrawlResponse(id);
            responseStatus = HttpStatus.OK;

        } catch (IOException e) {
            logger.error("Crawl Error : " + e.getMessage());
            response = new ErrorResponse("Crawl Error : " + e.getMessage());
            responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(response, responseStatus);
    }

    /**
     * Retrieves the crawled data from DB
     *
     * @return ResponseEntity
     */
    @ApiOperation(
            tags = "Crawl",
            value = "Get the crawled results",
            notes = "Get the crawled results",
            nickname = "getCrawledResultsByURLUsingGET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieves the crawled results", response = ResponseBase.class),
            @ApiResponse(code = 400, message = "Supplied url is invalid"),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @GetMapping(value = "/{urlId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ResponseBase> fetchContent(@ApiParam(value = "ID", required = true) @PathVariable Long urlId) {

        ResponseBase response;
        HttpStatus responseStatus;
        try {
            response = this.crawlResponseGenerator.generateResponse(urlId);
            responseStatus = HttpStatus.OK;
        } catch (IOException | WebCrawlerException e) {
            logger.error("Fetch Error : " + e.getMessage());
            response = new ErrorResponse("Fetch Error : " + e.getMessage());
            responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(response, responseStatus);

    }

    /**
     * Stops the crawling
     *
     * @return ResponseEntity
     */
    @ApiOperation(
            tags = "Crawl",
            value = "Stop the crawling",
            notes = "Stop the crawling",
            nickname = "stopCrawlingUsingPost")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully stopped the crawling", response = ResponseBase.class),
            @ApiResponse(code = 400, message = ""),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @PostMapping(value = "/stop")
    public ResponseEntity<ResponseBase> stopCrawling() {

        this.crawlManagerService.stopCrawling();
        return new ResponseEntity<>(HttpStatus.OK);

    }

}
