package org.gotaatr.database;

import lombok.extern.log4j.Log4j2;
import org.gotaatr.infra.properties.ConnectionProperties;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class ConnectionHolder {
    private Connection connection;
    private final ConnectionProperties connectionProperties;

    public ConnectionHolder(ConnectionProperties connectionProperties) {
        this.connectionProperties = connectionProperties;
    }

    @Retryable(
            retryFor = {DatabaseConnectException.class},
            listeners = "retryDatabaseListener", backoff = @Backoff(delay = 5000))
    private Connection connect() {
        try {
            if (connection != null && !connection.isClosed() && connection.isValid(1)) {
                return connection;
            }
            connection = DriverManager.getConnection(
                    connectionProperties.getUrl(),
                    connectionProperties.getUsername(),
                    connectionProperties.getPassword()
            );
            return connection;

        } catch (SQLException e) {
            throw new DatabaseConnectException();
        }
    }

    @Retryable(
            retryFor = {DatabaseConnectException.class},
            listeners = "retryDatabaseListener", backoff = @Backoff(delay = 5000))
    public PreparedStatement getInsertIntoTimeTablePS() {
        try {
            String INSERT_INTO_TIME_TABLE = "INSERT INTO timetable (time) VALUES ( ?)";
            return connect().prepareStatement(INSERT_INTO_TIME_TABLE);
        } catch (SQLException e) {
            throw new DatabaseConnectException();
        }
    }

    public List<TimeRecordEntity> getAll() {
        String selectSql = "SELECT id, time FROM timetable";
        List<TimeRecordEntity> result = new ArrayList<>();

        try (Statement ps = connect().createStatement()) {
            ResultSet resultSet = ps.executeQuery(selectSql);
            while (resultSet.next()) {
                var row = new TimeRecordEntity(resultSet.getString("id"), resultSet.getString("time"));
                result.add(row);
            }
            return result;
        } catch (SQLException e) {
            throw new DatabaseConnectException();
        }
    }


    public List<TimeRecordEntity> get(int offset, int pageSize) {
        String selectSql = "SELECT id, time FROM timetable offset " + offset + " limit " + pageSize;
        List<TimeRecordEntity> result = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement("SELECT id, time FROM timetable offset ? limit ?")) {
            ps.setInt(1, offset);
            ps.setInt(2, pageSize);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                var row = new TimeRecordEntity(resultSet.getString("id"), resultSet.getString("time"));
                result.add(row);
            }
            return result;
        } catch (SQLException e) {
            throw new DatabaseConnectException();
        }
    }

    @Retryable(retryFor = {DatabaseConnectException.class}, listeners = "retryDatabaseListener", backoff = @Backoff(delay = 5000))
    public void retryConnect() {
        log.info("Retrying to connect to the database");
        closeConnection();
        Connection newConnection;
        try {
            newConnection = DriverManager.getConnection(
                    connectionProperties.getUrl(),
                    connectionProperties.getUsername(),
                    connectionProperties.getPassword()
            );
            connection = newConnection;
            log.info("Successfully connected to the database");
        } catch (SQLException e) {
            log.error("Error while connecting to the database: {}", e.getMessage());
        }
    }

    public void clearTable() {
        String clearTableSql = "DELETE FROM timetable";
        try (Statement ps = connect().createStatement()) {
            ps.execute(clearTableSql);
        } catch (SQLException e) {
            throw new DatabaseConnectException();
        }
    }

    private void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("Error while closing connection: {}", e.getMessage());
            }
        }
    }
}
