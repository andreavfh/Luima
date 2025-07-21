package io.github.andreavfh.lumia.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Locale;

/**
 * Handles the loading and management of language-specific configurations for the plugin.
 * Provides methods to retrieve localized messages and prefixes based on the configured language.
 */
public class LanguageConfig {

    private final JavaPlugin plugin;
    private final Config config;
    private FileConfiguration langConfig;
    private String language;
    private String prefix;

    /**
     * Constructs a new LanguageConfig instance.
     *
     * @param plugin The JavaPlugin instance associated with this configuration.
     * @param config The main configuration instance for the plugin.
     */
    public LanguageConfig(JavaPlugin plugin, Config config) {
        this.plugin = plugin;
        this.config = config;
        loadConfigs();
    }

    /**
     * Loads the language configuration file and initializes language-specific settings.
     * If the language file does not exist, it is created from the plugin's resources.
     */
    private void loadConfigs() {
        this.language = config.getLanguage().toLowerCase(Locale.ROOT);

        File langFile = new File(plugin.getDataFolder(), "languages.yml");
        if (!langFile.exists()) {
            plugin.saveResource("languages.yml", false);
        }
        this.langConfig = YamlConfiguration.loadConfiguration(langFile);

        // Loads the prefix for the configured language.
        this.prefix = langConfig.getString("messages." + language + ".prefix", "");
    }

    /**
     * Reloads the language configuration, refreshing all settings.
     */
    public void reload() {
        loadConfigs();
    }

    /**
     * Retrieves a raw message string from the language configuration.
     *
     * @param key The key of the message to retrieve.
     * @return The localized message, or an empty string if the key is not found.
     */
    public String getRaw(String key) {
        return langConfig.getString("messages." + language + "." + key, "");
    }

    /**
     * Retrieves the currently configured language.
     *
     * @return The language code as a string.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Retrieves the prefix for the currently configured language.
     *
     * @return The prefix string.
     */
    public String getPrefix() {
        return prefix;
    }
}