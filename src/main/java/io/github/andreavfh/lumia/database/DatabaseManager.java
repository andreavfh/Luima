package io.github.andreavfh.lumia.database;

import io.github.andreavfh.lumia.Lumia;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private final Lumia plugin;
    private Connection connection;
    private SQLStorage sqlStorage;


    public DatabaseManager(Lumia plugin) {
        this.plugin = plugin;
    }

    public boolean connect() {
        try {
            File dataFolder = plugin.getDataFolder();

            File dbFile = new File(dataFolder, "lumia.db");
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();

            connection = DriverManager.getConnection(url);

            plugin.getLogger().info("SQLite connection established at " + dbFile.getAbsolutePath());

            this.sqlStorage = new SQLStorage(this);
            sqlStorage.createTable();

            return true;

        } catch (SQLException e) {
            plugin.getLogger().severe("Could not connect to SQLite: " + e.getMessage());
            return false;
        }
    }


    public Connection getConnection() {
        return connection;
    }

    public SQLStorage getSqlStorage() {
        return sqlStorage;
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                plugin.getLogger().info("SQLite connection closed.");
            } catch (SQLException e) {
                plugin.getLogger().severe("Error closing SQLite connection: " + e.getMessage());
            }
        }
    }
}
