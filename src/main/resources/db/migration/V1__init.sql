CREATE SCHEMA IF NOT EXISTS crawl;

CREATE TABLE IF NOT EXISTS crawl.crawlerinfo
(
    id bigserial NOT NULL,
    url CHARACTER VARYING(2083),
    title CHARACTER VARYING(1000),
    links text,
    depth int,
    parse_completed bool,
    CONSTRAINT id_pkey PRIMARY KEY (id)
);
