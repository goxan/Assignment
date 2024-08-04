package org.gotaatr.database;

import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@AllArgsConstructor
@Log4j2
public class TimeRecordRepository {
    private final ConnectionHolder connectionHolder;
    @SneakyThrows
    public void saveAll(List<TimeRecordEntity> timeEntities) {
        if (timeEntities.size() == 0) {
            return;
        }
        @Cleanup PreparedStatement statement = connectionHolder.getInsertIntoTimeTablePS();
        timeEntities.forEach(timeEntity -> {
            try {
                statement.setString(1, timeEntity.time());
                statement.addBatch();
            } catch (Exception e) {
                log.error("Error while saving TimeEntity: {}", e.getMessage());
            }
        });
        statement.executeBatch();
        log.info("Saved {} TimeEntities", timeEntities.size());
    }

    public List<TimeRecordEntity> fetchAll() {
        return connectionHolder.getAll();
    }

    public void clearTable(){
        connectionHolder.clearTable();
    }

    public List<TimeRecordEntity> fetch(int pageNumber, int pageSize) {
        return connectionHolder.get(pageNumber, pageSize);
    }

}
