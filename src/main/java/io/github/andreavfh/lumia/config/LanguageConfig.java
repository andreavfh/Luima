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

        this.prefix = translateColors(getMessage("prefix"));
    }

    public void reload() {
        loadConfigs();
    }

    public String translateColors(String input) {
        return input == null ? "" : input.replace("&", "§");
    }

    public String formatRaw(String key) {
        return langConfig.getString("messages." + language + "." + key, "");
    }

    public String getPrefix() {
        return prefix;
    }

    public String format(String key) {
        return prefix + " " + getMessage(key);
    }

    public String getMessage(String key) {
        return translateColors(formatRaw(key));
    }
}
