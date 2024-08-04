package integration;

import integration.TimeRecordRepositoryIntegrationTest;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import liquibase.integration.spring.SpringLiquibase;
import javax.sql.DataSource;

@TestConfiguration
public class LiquibaseTestConfig {

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:/changelog-master-test.yaml");
        liquibase.setContexts("test");
        return liquibase;
    }

    @Bean
    public DataSource dataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(TimeRecordRepositoryIntegrationTest.postgreSQLContainer.getJdbcUrl());
        dataSource.setUser(TimeRecordRepositoryIntegrationTest.postgreSQLContainer.getUsername());
        dataSource.setPassword(TimeRecordRepositoryIntegrationTest.postgreSQLContainer.getPassword());
        return dataSource;
    }
}
