package com.hw.webcrawler.core.manager;

import com.hw.webcrawler.config.JsonConfiguration;
import com.hw.webcrawler.core.parser.URLParser;
import com.hw.webcrawler.entity.UrlInfoEntity;
import com.hw.webcrawler.repository.CrawlInfoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CrawlManagerServiceImpl}
 */
@RunWith(SpringRunner.class)
public class CrawlManagerServiceImplTest {

    @InjectMocks
    private CrawlManagerServiceImpl crawlManagerService;

    @Mock
    private CrawlInfoRepository crawlInfoRepository;

    @Mock
    private URLParser urlParser;

    @Test
    public void test_startCrawling() throws IOException {

        when(urlParser.fetchURLInfo(anyString())).thenReturn(new PersistData(null, null, null));

        UrlInfoEntity entity = new UrlInfoEntity();
        entity.setDepth(1);
        entity.setLinks(JsonConfiguration.JSON_MAPPER.writeValueAsString(new ArrayList()));

        List<UrlInfoEntity> entityList = new ArrayList<>();
        entityList.add(entity);

        when(crawlInfoRepository.findNextEligibleUrl(any())).thenReturn(entityList);

        crawlManagerService.startCrawling("http://www.mockurl.com", 2);

        verify(crawlInfoRepository, times(1)).deleteAll();

        verify(crawlInfoRepository, times(1)).save(any(UrlInfoEntity.class));
    }


    @Test
    public void test_stopCrawling() throws IOException {

        crawlManagerService.stopCrawling();

        PersistData persistData = new PersistData("", "", new HashSet<>());

        // to invoke fetchAndRunNewUrls()
        crawlManagerService.persistAndContinue(persistData, 0);

        // to test that fetchAndRunNewUrls() returns after shouldCrawlingContinue check since stopCrawling
        // was called before persistAndContinue
        verify(crawlInfoRepository, times(0)).findNextEligibleUrl(any());

    }
}