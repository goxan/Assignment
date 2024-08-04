package org.gotaatr.database;

public class DatabaseConnectException extends RuntimeException {
    public DatabaseConnectException() {
        super("Error occurred while connecting to the database");
    }
}
