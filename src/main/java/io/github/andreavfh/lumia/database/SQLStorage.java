package io.github.andreavfh.lumia.database;

import io.github.andreavfh.lumia.skill.ISkill;
import io.github.andreavfh.lumia.skill.PlayerSkillHolder;
import io.github.andreavfh.lumia.skill.SkillType;

import java.sql.*;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

/**
 * Handles database operations for storing and retrieving player skill data.
 * Provides methods to create tables, save player skills, and load skill levels and XP.
 */
public class SQLStorage {

    private final DatabaseManager dbManager;

    /**
     * Constructs a new SQLStorage instance.
     *
     * @param dbManager The DatabaseManager instance for managing database connections.
     */
    public SQLStorage(DatabaseManager dbManager) {
        this.dbManager = dbManager;
        try {
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the database table for storing player skills if it does not already exist.
     *
     * @throws SQLException If an error occurs while creating the table.
     */
    public void createTable() throws SQLException {
        try (Statement st = dbManager.getConnection().createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS player_skills (" +
                    "uuid TEXT NOT NULL," +
                    "skill TEXT NOT NULL," +
                    "xp REAL NOT NULL DEFAULT 0," +
                    "level INTEGER NOT NULL DEFAULT 0," +
                    "PRIMARY KEY (uuid, skill)" +
                    ")");
        }
    }

    /**
     * Saves the skill data of a player to the database.
     *
     * @param uuid   The UUID of the player.
     * @param holder The PlayerSkillHolder containing the player's skills.
     */
    public void savePlayerSkills(UUID uuid, PlayerSkillHolder holder) {
        String sql = "INSERT OR REPLACE INTO player_skills (uuid, skill, xp, level) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = dbManager.getConnection().prepareStatement(sql)) {
            for (SkillType type : SkillType.values()) {
                ISkill skill = holder.getSkill(type);
                ps.setString(1, uuid.toString());
                ps.setString(2, type.name());
                ps.setDouble(3, skill.getCurrentXP());
                ps.setInt(4, skill.getLevel());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the skill levels of a player from the database.
     *
     * @param uuid The UUID of the player.
     * @return A map of skill types to their corresponding levels.
     */
    public Map<SkillType, Integer> loadPlayerSkillLevels(UUID uuid) {
        Map<SkillType, Integer> levels = new EnumMap<>(SkillType.class);
        String sql = "SELECT skill, level FROM player_skills WHERE uuid = ?";
        try (PreparedStatement ps = dbManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SkillType type = SkillType.valueOf(rs.getString("skill"));
                int level = rs.getInt("level");
                levels.put(type, level);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return levels;
    }

    /**
     * Loads the skill XP of a player from the database.
     *
     * @param uuid The UUID of the player.
     * @return A map of skill types to their corresponding XP values.
     */
    public Map<SkillType, Double> loadPlayerSkillXP(UUID uuid) {
        Map<SkillType, Double> xpMap = new EnumMap<>(SkillType.class);
        String sql = "SELECT skill, xp FROM player_skills WHERE uuid = ?";
        try (PreparedStatement ps = dbManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SkillType type = SkillType.valueOf(rs.getString("skill"));
                double xp = rs.getDouble("xp");
                xpMap.put(type, xp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return xpMap;
    }
}