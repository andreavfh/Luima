package io.github.andreavfh.lumia.database;

import io.github.andreavfh.lumia.skill.ISkill;
import io.github.andreavfh.lumia.skill.PlayerSkillHolder;
import io.github.andreavfh.lumia.skill.SkillType;

import java.sql.*;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class SQLStorage {

    private final DatabaseManager dbManager;

    public SQLStorage(DatabaseManager dbManager) {
        this.dbManager = dbManager;
        try {
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable() throws SQLException {
        try (Statement st = dbManager.getConnection().createStatement()) {
            // SQLite no soporta REPLACE INTO pero sí INSERT OR REPLACE
            st.executeUpdate("CREATE TABLE IF NOT EXISTS player_skills (" +
                    "uuid TEXT NOT NULL," +
                    "skill TEXT NOT NULL," +
                    "xp REAL NOT NULL DEFAULT 0," +
                    "level INTEGER NOT NULL DEFAULT 0," +
                    "PRIMARY KEY (uuid, skill)" +
                    ")");
        }
    }

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
