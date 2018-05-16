package com.hw.webcrawler.core.parser;

import com.hw.webcrawler.core.manager.PersistData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@inheritDoc}
 *
 * @author Dilip Tharoor
 */
@Service
public class URLParserImpl implements URLParser{

    /**
     * {@inheritDoc}
     */
    @Override
    public PersistData fetchURLInfo(String url) throws IOException {

        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");

        Set<String> linksString = links.stream()
                .filter(link -> !link.attr("abs:href").isEmpty())
                .map(link -> link.attr("abs:href"))
                .collect(Collectors.toSet());

        return new PersistData(doc.title(), url, linksString);
    }
}
