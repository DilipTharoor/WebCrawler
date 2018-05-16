package com.hw.webcrawler.core.manager;

import java.util.Set;

/**
 * Data to store in the DB
 *
 * @author Dilip Tharoor
 */
public class PersistData {

    private String title;
    private String url;
    private Set<String> nodes;

    public PersistData(String title, String url, Set<String> links) {
        this.title = title;
        this.url = url;
        this.nodes = links;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<String> getNodes() {
        return nodes;
    }

    public void setNodes(Set<String> nodes) {
        this.nodes = nodes;
    }
}
