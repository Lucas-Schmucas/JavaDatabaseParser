package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseCreation {
    public static void main(String[] args) {
        new DatabaseCreation();
    }
    private final String databaseName = "javatest";

    private final String useQuery = "USE " + databaseName;

    public DatabaseCreation() {
        createStructure();
    }



    private void createStructure() throws RuntimeException {

            String query0 = "CREATE DATABASE IF NOT EXISTS " + databaseName;

            String query1 = "USE " + databaseName;

            String query2 = "CREATE TABLE IF NOT EXISTS `javatesttabelle` ("
                    + "`id` INT NOT NULL AUTO_INCREMENT,"
                    + "`eintrag` text NOT NULL DEFAULT '',"
                    + " PRIMARY KEY (id))";

            Statement statement;

            try {
                Connection dbConnection = createConnection();
                statement = dbConnection.createStatement();
                dbConnection.setAutoCommit(false);
                statement.addBatch(query0);
                statement.addBatch(query1);
                statement.addBatch(query2);
                statement.executeBatch();
                dbConnection.commit();
                dbConnection.setAutoCommit(true);
                statement.close();
                dbConnection.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
            private Connection createConnection() {
                String url = "jdbc:mariadb://localhost/";
                String userName = "user";
                String password = "user";
                Connection dbConnection = null;
                try {
                    dbConnection = DriverManager.getConnection(
                            url,
                            userName,
                            password);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            return dbConnection;
    }
}
