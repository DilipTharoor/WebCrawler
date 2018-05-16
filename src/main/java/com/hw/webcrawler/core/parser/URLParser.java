package com.hw.webcrawler.core.parser;

import com.hw.webcrawler.core.manager.PersistData;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Parses URLs and returns the required content
 * @author Dilip Tharoor
 */
@Service
public interface URLParser {

    /**
     * Read and parse any url and return the required content
     * @param url to parse
     * @return the parsed content
     */
    PersistData fetchURLInfo(String url) throws IOException;
}
