package integration;

import org.gotaatr.Main;
import org.gotaatr.database.ConnectionHolder;
import org.gotaatr.database.TimeRecordEntity;
import org.gotaatr.database.TimeRecordRepository;
import org.gotaatr.infra.properties.ConnectionProperties;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {
        Main.class,
        ConnectionProperties.class,
        ConnectionHolder.class,
        TimeRecordRepository.class,
        LiquibaseTestConfig.class})
@ActiveProfiles("test")
public class TimeRecordRepositoryIntegrationTest {
    @Autowired
    private ConnectionProperties connectionProperties;
    @Autowired
    private TimeRecordRepository timeRecordRepository;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer("postgres:13.3")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");



    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("connection.url", postgreSQLContainer::getJdbcUrl);
        registry.add("connection.username", postgreSQLContainer::getUsername);
        registry.add("connection.password", postgreSQLContainer::getPassword);
    }


    @Test
    public void testSaveAll() {
        TimeRecordEntity entity = new TimeRecordEntity("1", "2023-10-01T10:00:00");
        timeRecordRepository.saveAll(List.of(entity));

        List<TimeRecordEntity> result = timeRecordRepository.fetchAll();
        assertEquals(1, result.size());
        assertEquals("2023-10-01T10:00:00", result.get(0).time());
        timeRecordRepository.clearTable();
    }

    @Test
    public void testFetchAll() {
        TimeRecordEntity entity1 = new TimeRecordEntity("2", "2023-10-01T10:00:00");
        TimeRecordEntity entity2 = new TimeRecordEntity("3","2023-10-02T10:00:00");
        timeRecordRepository.saveAll(List.of(entity1, entity2));

        List<TimeRecordEntity> result = timeRecordRepository.fetchAll();
        assertEquals(2, result.size());
        timeRecordRepository.clearTable();
    }

    @Test
    public void testFetchWithPagination() {
        TimeRecordEntity entity1 = new TimeRecordEntity("1","2023-10-01T10:00:00");
        TimeRecordEntity entity2 = new TimeRecordEntity("2","2023-10-02T10:00:00");
        timeRecordRepository.saveAll(List.of(entity1, entity2));

        List<TimeRecordEntity> result = timeRecordRepository.fetch(0, 1);
        assertEquals(1, result.size());
        assertEquals("2023-10-01T10:00:00", result.get(0).time());
        timeRecordRepository.clearTable();
    }

    @Test
    public void testSaveAllWithRetry() {
        TimeRecordEntity entity = new TimeRecordEntity("1", "2023-10-01T10:00:00");
        postgreSQLContainer.stop();
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.schedule(() -> {
            postgreSQLContainer.start();
            connectionProperties.setUrl(postgreSQLContainer.getJdbcUrl());
            connectionProperties.setUsername(postgreSQLContainer.getUsername());
            connectionProperties.setPassword(postgreSQLContainer.getPassword());
            try {
                postgreSQLContainer.createConnection("").createStatement().execute(createTable);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 4, TimeUnit.SECONDS);



        timeRecordRepository.saveAll(List.of(entity));
        List<TimeRecordEntity> timeRecordEntities = timeRecordRepository.fetchAll();
        assertEquals(1, timeRecordEntities.size());
    }

    private String createTable = "CREATE TABLE timetable (id SERIAL PRIMARY KEY, time VARCHAR(255));";
}