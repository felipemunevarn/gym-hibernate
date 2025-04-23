package config;

import jakarta.persistence.EntityManagerFactory;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import com.epam.config.AppConfig;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

class AppConfigTest {

    private AppConfig config;

    @BeforeEach
    void setUp() {
        config = new AppConfig();
    }

    @Test
    void testDataSource() {
        DataSource ds = config.dataSource();
        assertNotNull(ds);
        assertTrue(ds instanceof JdbcDataSource);
    }

    @Test
    void testEntityManagerFactoryBeanCreation() {
        DataSource ds = config.dataSource();
        LocalContainerEntityManagerFactoryBean factoryBean = config.entityManagerFactory(ds);
        assertNotNull(factoryBean);
        assertEquals(ds, factoryBean.getDataSource());
        assertNotNull(factoryBean.getJpaVendorAdapter());
    }

    @Test
    void testTransactionManager() {
        // Create actual EntityManagerFactory for real object
        LocalContainerEntityManagerFactoryBean emfBean = config.entityManagerFactory(config.dataSource());
        emfBean.afterPropertiesSet(); // Important: initialize the bean

        EntityManagerFactory emf = emfBean.getObject();
        assertNotNull(emf);

        JpaTransactionManager txManager = config.transactionManager(emf);
        assertNotNull(txManager);
        assertEquals(emf, txManager.getEntityManagerFactory());
    }

    // Uncomment this if you later enable passwordEncoder()
    /*
    @Test
    void testPasswordEncoder() {
        PasswordEncoder encoder = config.passwordEncoder();
        assertNotNull(encoder);
        String raw = "password123";
        String encoded = encoder.encode(raw);
        assertTrue(encoder.matches(raw, encoded));
    }
    */
}

