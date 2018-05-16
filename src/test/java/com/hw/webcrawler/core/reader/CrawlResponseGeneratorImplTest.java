package com.hw.webcrawler.core.reader;

import com.hw.webcrawler.config.JsonConfiguration;
import com.hw.webcrawler.entity.UrlInfoEntity;
import com.hw.webcrawler.exception.WebCrawlerException;
import com.hw.webcrawler.repository.CrawlInfoRepository;
import com.hw.webcrawler.response.CrawlReadResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link CrawlResponseGeneratorImpl}
 */
@RunWith(SpringRunner.class)
public class CrawlResponseGeneratorImplTest {

    @InjectMocks
    private
    CrawlResponseGeneratorImpl crawlResponseGenerator;

    @Mock
    private CrawlInfoRepository crawlInfoRepository;

    @Test
    public void test_generateResponse() throws IOException, WebCrawlerException {

        List<UrlInfoEntity> urlInfoEntityList = new ArrayList<>();
        UrlInfoEntity entity = new UrlInfoEntity();
        entity.setTitle("title");
        entity.setUrl("url");

        List<String> links = new ArrayList<>();
        links.add("link1");

        entity.setLinks(JsonConfiguration.JSON_MAPPER.writeValueAsString(links));
        urlInfoEntityList.add(entity);

        when(crawlInfoRepository.findById(1L)).thenReturn(urlInfoEntityList);

        CrawlReadResponse crawlReadResponse = crawlResponseGenerator.generateResponse(1L);

        CrawlData crawlData = crawlReadResponse.getCrawlDataResponse();

        Assert.assertEquals(crawlData.getTitle(), "title");
        Assert.assertEquals(crawlData.getUrl(), "url");

        Assert.assertEquals(crawlData.getNodes().get(0).getUrl(), "link1");
        Assert.assertNull(crawlData.getNodes().get(0).getTitle());
        Assert.assertNull(crawlData.getNodes().get(0).getNodes());

    }
}