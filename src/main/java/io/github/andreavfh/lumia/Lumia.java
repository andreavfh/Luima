package io.github.andreavfh.lumia;

import io.github.andreavfh.lumia.commands.CommandHandler;
import io.github.andreavfh.lumia.commands.sub.Help;
import io.github.andreavfh.lumia.config.Config;
import io.github.andreavfh.lumia.config.LanguageConfig;
import io.github.andreavfh.lumia.database.DatabaseManager;
import io.github.andreavfh.lumia.integrations.LuckPermsHook;
import io.github.andreavfh.lumia.skill.SkillManager;
import io.github.andreavfh.lumia.utils.LoggerWrapper;
import io.github.andreavfh.lumia.utils.MessageFormatter;
import io.github.andreavfh.lumia.utils.SkillBossBarManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class for the Lumia plugin.
 * Handles initialization, configuration, and management of plugin components.
 */
public final class Lumia extends JavaPlugin {

    private BukkitAudiences adventure;
    private LanguageConfig languageConfig;
    private LoggerWrapper logger;
    private LuckPermsHook luckPermsHook;
    private MessageFormatter messageFormatter;
    private Config config;

    private DatabaseManager databaseManager;
    private SkillManager skillManager;
    private SkillBossBarManager bossBarManager;

    /**
     * Called when the plugin is enabled.
     * Initializes configurations, integrations, and plugin components.
     */
    @Override
    public void onEnable() {
        this.config = new Config(this);
        this.languageConfig = new LanguageConfig(this, config);
        this.messageFormatter = new MessageFormatter(this);
        this.logger = new LoggerWrapper(getLogger(), languageConfig.getPrefix());
        this.adventure = BukkitAudiences.create(this);

        logger.info("Initializing...");

        this.luckPermsHook = new LuckPermsHook(this);
        if (luckPermsHook.isHooked()) {
            logger.info("LuckPerms integration enabled. You can use permissions in commands and features.");
        } else {
            logger.warning("LuckPerms integration not found. Some features may not work as expected. Make sure LuckPerms is installed and enabled.");
        }

        this.databaseManager = new DatabaseManager(this);
        if (!databaseManager.connect()) {
            logger.info("Database connection failed. Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.skillManager = new SkillManager(databaseManager.getSqlStorage());

        CommandHandler handler = new CommandHandler(this);
        getCommand("lumia").setExecutor(handler);
        getCommand("lumia").setTabCompleter(handler);

        handler.registerSubCommand(new Help(this, handler));

        RegisterListeners listeners = new RegisterListeners();
        listeners.registerAllListeners(this, skillManager);

        this.bossBarManager = new SkillBossBarManager(this);

        logger.info(languageConfig.getRaw("plugin_enabled"));
    }

    /**
     * Called when the plugin is disabled.
     * Cleans up resources and closes connections.
     */
    @Override
    public void onDisable() {
        if (adventure != null) {
            adventure.close();
            adventure = null;
        }

        if (databaseManager != null) {
            databaseManager.disconnect();
        }

        logger.info(languageConfig.getRaw("plugin_disabled"));
    }

    /**
     * Returns the Adventure API instance for the plugin.
     *
     * @return The BukkitAudiences instance.
     * @throws IllegalStateException If Adventure is not initialized.
     */
    public BukkitAudiences adventure() {
        if (adventure == null) {
            throw new IllegalStateException("Adventure not initialized");
        }
        return adventure;
    }

    /**
     * Returns the logger wrapper for the plugin.
     *
     * @return The logger wrapper instance.
     */
    public LoggerWrapper getLumiaLogger() {
        return logger;
    }

    /**
     * Returns the message formatter for the plugin.
     *
     * @return The message formatter instance.
     */
    public MessageFormatter getMessageFormatter() {
        return messageFormatter;
    }

    /**
     * Returns the plugin's configuration instance.
     *
     * @return The configuration instance.
     */
    public Config getPluginConfig() {
        return config;
    }

    /**
     * Returns the language configuration instance.
     *
     * @return The language configuration instance.
     */
    public LanguageConfig getLanguageConfig() {
        return languageConfig;
    }

    /**
     * Returns the LuckPerms integration hook.
     *
     * @return The LuckPermsHook instance.
     */
    public LuckPermsHook getLuckPermsHook() {
        return luckPermsHook;
    }

    /**
     * Returns the database manager for the plugin.
     *
     * @return The database manager instance.
     */
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    /**
     * Returns the boss bar manager for the plugin.
     *
     * @return The boss bar manager instance.
     */
    public SkillBossBarManager getBossBarManager() {
        return bossBarManager;
    }

    /**
     * Returns the skill manager for the plugin.
     *
     * @return The skill manager instance.
     */
    public SkillManager getSkillManager() {
        return skillManager;
    }
}