package com.hw.webcrawler.core.reader;

import com.hw.webcrawler.config.JsonConfiguration;
import com.hw.webcrawler.entity.UrlInfoEntity;
import com.hw.webcrawler.exception.WebCrawlerException;
import com.hw.webcrawler.repository.CrawlInfoRepository;
import com.hw.webcrawler.response.CrawlReadResponse;
import org.hibernate.service.spi.InjectService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link CrawlResponseGeneratorImpl}
 */
@RunWith(SpringRunner.class)
public class CrawlResponseGeneratorImplTest {

    @InjectMocks
    private CrawlResponseGeneratorImpl crawlResponseGenerator;

    @Mock
    private CrawlInfoRepository crawlInfoRepository;

    @Test
    public void test_generateResponse() throws IOException, WebCrawlerException {

        Optional<UrlInfoEntity> urlInfoEntity;
        UrlInfoEntity entity = new UrlInfoEntity();
        entity.setTitle("title");
        entity.setUrl("url");

        List<String> links = new ArrayList<>();
        links.add("link1");

        entity.setLinks(JsonConfiguration.JSON_MAPPER.writeValueAsString(links));
        urlInfoEntity = Optional.of(entity);

        when(crawlInfoRepository.findById(any())).thenReturn(urlInfoEntity);

        CrawlReadResponse crawlReadResponse = crawlResponseGenerator.generateResponse(1L);

        CrawlData crawlData = crawlReadResponse.getCrawlDataResponse();

        Assert.assertEquals(crawlData.getTitle(), "title");
        Assert.assertEquals(crawlData.getUrl(), "url");

        Assert.assertEquals(crawlData.getNodes().get(0).getUrl(), "link1");
        Assert.assertNull(crawlData.getNodes().get(0).getTitle());
        Assert.assertNull(crawlData.getNodes().get(0).getNodes());

    }
}