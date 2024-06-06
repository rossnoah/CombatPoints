package dev.noah.combatpoints.util;

import dev.noah.combatpoints.CombatPoints;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SQLite {

    private CombatPoints plugin;
    private String fileName;
    private Connection connection;

    public SQLite(CombatPoints plugin) {
        this.plugin = plugin;
        fileName = plugin.getConfig().getString("database.file", "combatpoints.db");
        File file = new File(plugin.getDataFolder(), fileName);

        try {
            Class.forName("org.sqlite.JDBC");
            plugin.getLogger().info("Loaded SQLite driver.");
            connection = DriverManager.getConnection("jdbc:sqlite:" + file);
        } catch (ClassNotFoundException e) {
            plugin.getLogger().severe("Failed to load SQLite driver.");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to establish SQLite connection.");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }



    public void createTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS combatpoints (uuid TEXT PRIMARY KEY, points INTEGER)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setPoints(String uuid, int points) {
        String query = "REPLACE INTO combatpoints (uuid, points) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, uuid);
            statement.setInt(2, points);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getPoints(String uuid) {
        String query = "SELECT points FROM combatpoints WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("points");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
