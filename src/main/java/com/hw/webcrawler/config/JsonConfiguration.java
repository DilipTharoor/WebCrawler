package com.hw.webcrawler.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;

/**
 * ApplicationContext contains the application-wide configuration.
 *
 * @author Dilip Tharoor
 */
@Configuration
public class JsonConfiguration {

    public static final ObjectMapper JSON_MAPPER = new ObjectMapper();
}