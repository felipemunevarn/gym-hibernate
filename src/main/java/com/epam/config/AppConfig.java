package com.epam.config;

import jakarta.persistence.EntityManagerFactory;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
// Import PasswordEncoder if you add the bean
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Spring Core configuration class using Java-based configuration.
 * Sets up DataSource, JPA EntityManagerFactory with Hibernate,
 * and Transaction Management.
 */
@Configuration
@ComponentScan(basePackages = "com.epam")
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.epam.repository")
public class AppConfig {

    /**
     * Configures the DataSource bean for H2 in-memory database.
     * DB_CLOSE_DELAY=-1 keeps the database alive as long as the JVM runs.
     *
     * @return Configured DataSource bean.
     */
    @Bean
    public DataSource dataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:~/gym;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        dataSource.setUser("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.epam.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Properties props = new Properties();
        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        em.setJpaProperties(props);

        em.setEntityManagerFactoryInterface(jakarta.persistence.EntityManagerFactory.class);

        return em;
    }

    /**
     * Configures the PlatformTransactionManager bean for JPA.
     * Enables the use of @Transactional annotations on service methods.
     *
     * @return Configured JpaTransactionManager bean.
     */
    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    /**
     * Bean for password encoding. Use a strong encoder like BCrypt.
     * Inject this bean into services where password handling is needed.
     * NOTE: Requires spring-security-crypto dependency. Uncomment if needed.
     *
     * @return PasswordEncoder instance.
     */
    /*
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    */
}


