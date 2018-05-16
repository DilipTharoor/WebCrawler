package com.hw.webcrawler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * PersistenceContext contains the database configuration.
 *
 * @author Dilip Tharoor
 */
@Configuration
@EnableJpaRepositories(basePackages = {"com.hw.webcrawler.repository"})
@EnableTransactionManagement
class PersistenceContext {
    private static final String[] ENTITY_PACKAGES = {"com.hw.webcrawler.entity"};

    private final PersistenceConfigProperties persistenceConfigProperties;

    public PersistenceContext(PersistenceConfigProperties persistenceConfigProperties) {
        this.persistenceConfigProperties = persistenceConfigProperties;
    }

    /**
     * The data source for the database.
     *
     * @return DataSource
     */
    @Bean
    DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(persistenceConfigProperties.getDbDriverClass());
        dataSource.setUrl(persistenceConfigProperties.getDbUrl());
        dataSource.setUsername(persistenceConfigProperties.getDbUser());
        dataSource.setPassword(persistenceConfigProperties.getDbPassword());

        return dataSource;
    }

    /**
     * The entity manager factory.
     *
     * @param dataSource
     * @return LocalContainerEntityManagerFactoryBean
     */
    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setPackagesToScan(ENTITY_PACKAGES);

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", persistenceConfigProperties.getHibernateDialect());
        jpaProperties.put("hibernate.hbm2ddl.auto", persistenceConfigProperties.getHibernateHbm2DDL());
        jpaProperties.put("hibernate.show_sql", persistenceConfigProperties.getHibernateShowSql());

        entityManagerFactoryBean.setJpaProperties(jpaProperties);

        return entityManagerFactoryBean;
    }

    /**
     * The JPA transaction manager.
     *
     * @param entityManagerFactory
     * @return JpaTransactionManager
     */
    @Bean
    JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}