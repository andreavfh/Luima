package io.github.andreavfh.lumia.utils;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.config.Config;
import io.github.andreavfh.lumia.skill.SkillType;
import org.bukkit.plugin.java.JavaPlugin;

public class SkillXPUtil {

    public static double getXPRequiredForLevel(int level, SkillType skill) {
        JavaPlugin plugin = JavaPlugin.getPlugin(Lumia.class);
        Config config = new Config(plugin);

        if (level <= 1) return config.getBaseXp(skill);
        return config.getBaseXp(skill) * Math.pow(config.getProgression(skill), level - 1);
    }
}

