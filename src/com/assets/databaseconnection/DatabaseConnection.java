package com.assets.databaseconnection;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DatabaseConnection {

    private static DatabaseConnection instance;

    private final String url = "jdbc:mysql://localhost:3306/personalagenda?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&createDatabaseIfNotExist=true";
    private final String username = "root";
    private final String password = "admin";

    private final Connection connection;

    private DatabaseConnection() throws SQLException {
        this.connection = DriverManager.getConnection(url, username, password);
    }

    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
            createTables(instance.getConnection());
        }

        if (instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private static void createTables(Connection connection) {
        String delimiter = ";";
        try (Scanner scanner = new Scanner(new File("create-table.sql"))) {
            scanner.useDelimiter(delimiter);
            while (scanner.hasNext()) {
                String rawStatement = scanner.next() + delimiter;
                if (rawStatement.trim().length() < 2) {
                    continue;
                }
                System.out.println("Executing statement: " + rawStatement);

                try (Statement currentStatement = connection.createStatement()) {
                    currentStatement.execute(rawStatement);
                    System.out.println("Operatie executata cu succes!");
                } catch (SQLException e) {
                    System.out.println("Operatia nu se poate executa: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Nu s-a putut incarca fisierul: 'tables.sql'!");
        }
    }
}