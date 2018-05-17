package com.hw.webcrawler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuration class to dynamically create a Swagger API description for
 * Web Crawler Application using Springfox.
 *
 * This is available at http://localhost:<port number>/swagger-ui.html
 *
 * @author Dilip Tharoor
 */
@Configuration
@EnableSwagger2
class SwaggerConfig {

    /**
     * Entry point for the complete Swagger ui generation
     */
    @Bean
    public Docket webCrawlerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.hw.webcrawler.api"))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/")
                .apiInfo(metadata());
    }

    /**
     * Support the Springfox configuration for Swagger with additional information.
     *
     * @return A configured Springfox ApiInfo object.
     */
    private ApiInfo metadata() {
        return new ApiInfoBuilder()
                .title("Web Crawler REST API")
                .description("The REST API provided by the Web Crawler application.")
                .version("1.0.0")
                .contact(new Contact("Dilip Tharoor", "https://github.com/DilipTharoor", "tharoor.dilip@gmail.com"))
                .build();
    }
}