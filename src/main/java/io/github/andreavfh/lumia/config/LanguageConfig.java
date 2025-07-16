package io.github.andreavfh.lumia.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Locale;

public class LanguageConfig {

    private final JavaPlugin plugin;
    private final Config config;
    private FileConfiguration langConfig;
    private String language;
    private String prefix;

    public LanguageConfig(JavaPlugin plugin, Config config) {
        this.plugin = plugin;
        this.config = config;
        loadConfigs();
    }

    private void loadConfigs() {
        this.language = config.getLanguage().toLowerCase(Locale.ROOT);

        File langFile = new File(plugin.getDataFolder(), "languages.yml");
        if (!langFile.exists()) {
            plugin.saveResource("languages.yml", false);
        }
        this.langConfig = YamlConfiguration.loadConfiguration(langFile);

        // Se puede seguir manteniendo si lo necesitas para prefijos estáticos
        this.prefix = langConfig.getString("messages." + language + ".prefix", "");
    }

    public void reload() {
        loadConfigs();
    }

    public String getRaw(String key) {
        return langConfig.getString("messages." + language + "." + key, "");
    }

    public String getLanguage() {
        return language;
    }

    public String getPrefix() {
        return prefix;
    }
}
