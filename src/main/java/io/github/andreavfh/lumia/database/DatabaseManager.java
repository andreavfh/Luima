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

    /**
     * Constructs a new DatabaseManager for managing SQLite connections.
     *
     * @param plugin The Lumia plugin instance.
     */
    public DatabaseManager(Lumia plugin) {
        this.plugin = plugin;
    }

    /**
     * Establishes a connection to the SQLite database.
     *
     * @return True if the connection is successful, false otherwise.
     */
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

    /**
     * Retrieves the current SQLite database connection.
     *
     * @return The active database connection.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Retrieves the SQLStorage instance for managing database operations.
     *
     * @return The SQLStorage instance.
     */
    public SQLStorage getSqlStorage() {
        return sqlStorage;
    }

    /**
     * Closes the SQLite database connection.
     * Logs the status of the disconnection process.
     */
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
