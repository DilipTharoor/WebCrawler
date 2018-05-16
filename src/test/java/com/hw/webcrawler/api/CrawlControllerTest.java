package com.hw.webcrawler.api;

import com.hw.webcrawler.config.JsonConfiguration;
import com.hw.webcrawler.core.manager.CrawlManagerService;
import com.hw.webcrawler.core.reader.CrawlData;
import com.hw.webcrawler.core.reader.CrawlResponseGenerator;
import com.hw.webcrawler.request.CrawlRequest;
import com.hw.webcrawler.response.CrawlReadResponse;
import com.hw.webcrawler.response.CrawlResponse;
import com.hw.webcrawler.response.ErrorResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for {@link CrawlController}
 */
@RunWith(SpringRunner.class)
public class CrawlControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CrawlManagerService crawlManagerService;

    @Mock
    private CrawlResponseGenerator crawlResponseGenerator;

    @InjectMocks
    private CrawlController crawlController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(crawlController).build();
    }

    @Test
    public void test_startCrawling() throws Exception {

        String url = "http://www.mockurl.com";

        CrawlRequest crawlRequest = new CrawlRequest(url, 1);
        CrawlResponse crawlResponse = new CrawlResponse(999L);

        String request = JsonConfiguration.JSON_MAPPER.writeValueAsString(crawlRequest);
        String response = JsonConfiguration.JSON_MAPPER.writeValueAsString(crawlResponse);

        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/api/url");
        postRequest.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        postRequest.content(request);

        when(crawlManagerService.startCrawling(url, 1)).thenReturn(999L);

        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(content().string(response));

    }

    @Test
    public void test_startCrawling_Fail() throws Exception {

        String url = "http://www.mockurl.com";

        CrawlRequest crawlRequest = new CrawlRequest(url, 1);
        ErrorResponse errorResponse = new ErrorResponse("Crawl Error : null");

        String request = JsonConfiguration.JSON_MAPPER.writeValueAsString(crawlRequest);
        String response = JsonConfiguration.JSON_MAPPER.writeValueAsString(errorResponse);

        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/api/url");
        postRequest.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        postRequest.content(request);

        when(crawlManagerService.startCrawling(url, 1)).thenThrow(new IOException());

        mockMvc.perform(postRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(response));

    }

    @Test
    public void test_fetchContent() throws Exception {

        CrawlData crawlDataResponse = new CrawlData("title", "url", null);
        CrawlReadResponse crawlReadResponse = new CrawlReadResponse(crawlDataResponse);

        String response = JsonConfiguration.JSON_MAPPER.writeValueAsString(crawlReadResponse);

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/api/url/1");

        when(crawlResponseGenerator.generateResponse(1L)).thenReturn(crawlReadResponse);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(content().string(response));

    }

    @Test
    public void test_fetchContent_fail() throws Exception {

        ErrorResponse error = new ErrorResponse("Fetch Error : null");

        String errorResponse = JsonConfiguration.JSON_MAPPER.writeValueAsString(error);

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/api/url/1");

        when(crawlResponseGenerator.generateResponse(1L)).thenThrow(new IOException());

        mockMvc.perform(getRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(errorResponse));

    }

    @Test
    public void test_stopCrawling() throws Exception {

        MockHttpServletRequestBuilder postRequest = MockMvcRequestBuilders.post("/api/url/stop");
        mockMvc.perform(postRequest)
                .andExpect(status().isOk());

    }

}