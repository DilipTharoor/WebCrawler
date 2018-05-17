package com.hw.webcrawler.entity;

import javax.persistence.*;

/**
 * Entity to store into DB for the parsed data for each URL
 *
 * @author Dilip Tharoor
 */
@Entity
@Table(name = "crawlerinfo", schema = "crawl")
public class UrlInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "url", length = 50)
    private String url;

    @Column(name = "title", length = 300)
    private String title;

    @Column(name = "links")
    private String links;

    @Column(name = "depth")
    private int depth;

    @Column(name = "parse_completed")
    private boolean parseCompleted;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public boolean isParseCompleted() {
        return parseCompleted;
    }

    public void setParseCompleted(boolean parseCompleted) {
        this.parseCompleted = parseCompleted;
    }

}
