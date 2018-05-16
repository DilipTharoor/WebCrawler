package com.hw.webcrawler.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for postgres
 *
 * @author Dilip Tharoor
 */
@Component
class PersistenceConfigProperties {

    @Value("${db.driver.class}")
    private String dbDriverClass;

    @Value("${db.url}")
    private String dbUrl;

    @Value("${db.user}")
    private String dbUser;

    @Value("${db.password}")
    private String dbPassword;

    @Value("${hibernate.dialect}")
    private String hibernateDialect;

    @Value("${hibernate.hbm2ddl.auto}")
    private String hibernateHbm2DDL;

    @Value("${hibernate.show_sql}")
    private String hibernateShowSql;

    public String getDbDriverClass() {
        return dbDriverClass;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getHibernateDialect() {
        return hibernateDialect;
    }

    public String getHibernateHbm2DDL() {
        return hibernateHbm2DDL;
    }

    public String getHibernateShowSql() {
        return hibernateShowSql;
    }
}
