package io.github.andreavfh.lumia.config;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

public class Config {

    private final JavaPlugin plugin;
    private String language;

    public Config(JavaPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.language = plugin.getConfig().getString("language", "en").toLowerCase(Locale.ROOT);

    }

    public String getLanguage() {
        return language;
    }


}
