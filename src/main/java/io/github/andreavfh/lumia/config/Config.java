package io.github.andreavfh.lumia.config;

import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class Config {

    private final JavaPlugin plugin;
    private String language;

    public Config(JavaPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    /**
     * Loads the configuration file and initializes necessary settings.
     */
    public void load() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.language = plugin.getConfig().getString("language", "en").toLowerCase(Locale.ROOT);
    }

    public String getLanguage() {
        return language;
    }

    /**
     * Gets the base XP for a specific skill.
     */
    public double getBaseXp(SkillType skill) {
        return plugin.getConfig().getDouble("skill." + skill.getKey() + ".base-xp", 100.0);
    }

    /**
     * Gets the progression multiplier for a specific skill.
     */
    public double getProgression(SkillType skill) {
        return plugin.getConfig().getDouble("skill." + skill.getKey() + ".progression", 1.4);
    }

    /**
     * Gets the XP per action for a specific skill.
     * This can return a flat value or 0.0 if the skill uses material-based XP.
     */
    public double getXpPerAction(SkillType skill) {
        return plugin.getConfig().getDouble("skill." + skill.getKey() + ".xp-per-action", 0.0);
    }

    /**
     * Gets XP per material block for mining.
     * Only applies to skills like MINING that use block-specific XP.
     */
    public double getXpForMaterial(SkillType skill, Material material) {
        if (skill != SkillType.MINING) return 0.0;

        String path = "skill.mining.xp-per-action." + material.name();
        return plugin.getConfig().getDouble(path, 0.0);
    }

    /**
     * Returns all materials configured with XP for a skill like MINING.
     */
    public Set<String> getConfiguredMaterialKeys(SkillType skill) {
        if (skill != SkillType.MINING) return Set.of();

        ConfigurationSection section = plugin.getConfig().getConfigurationSection("skill.mining.xp-per-action");
        return section != null ? section.getKeys(false) : Set.of();
    }

    /**
     * Returns a map of all material-XP pairs for a material-based skill like MINING.
     */
    public Map<String, Object> getAllXpMaterials(SkillType skill) {
        if (skill != SkillType.MINING) return Map.of();

        ConfigurationSection section = plugin.getConfig().getConfigurationSection("skill.mining.xp-per-action");
        return section != null ? section.getValues(false) : Map.of();
    }

}
