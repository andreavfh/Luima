package io.github.andreavfh.lumia.utils;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.config.Config;
import org.bukkit.plugin.java.JavaPlugin;

public class SkillXPUtil {

    public static double getXPRequiredForLevel(int level) {
        JavaPlugin plugin = JavaPlugin.getPlugin(Lumia.class);
        Config config = new Config(plugin);

        if (level <= 1) return config.getBaseXp();
        return config.getBaseXp() * Math.pow(config.getProgressionXp(), level - 1);
    }
}

