package com.hw.webcrawler.exception;

/**
 * WebCrawlerException extends {@Exception} and is the base Exception of the UI Delegate.
 *
 * @author  Dilip Tharoor
 */
public class WebCrawlerException extends Exception {

    public WebCrawlerException(String message) {
        super(message);
    }

    public WebCrawlerException(Throwable throwable) {
        super(throwable);
    }

    public WebCrawlerException(String message, Throwable cause) {
        super(message, cause);
    }

}
