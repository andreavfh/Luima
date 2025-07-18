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

    @Override
    public void onDisable() {
        if (adventure != null) {
            adventure.close();
            adventure = null;
        }

        // Cerrar conexión SQL
        if (databaseManager != null) {
            databaseManager.disconnect();
        }

        logger.info(languageConfig.getRaw("plugin_disabled"));
    }

    public BukkitAudiences adventure() {
        if (adventure == null) {
            throw new IllegalStateException("Adventure not initialized");
        }
        return adventure;
    }

    public LoggerWrapper getLumiaLogger() {
        return logger;
    }

    public MessageFormatter getMessageFormatter() {
        return messageFormatter;
    }

    public Config getPluginConfig() {
        return config;
    }

    public LanguageConfig getLanguageConfig() {
        return languageConfig;
    }

    public LuckPermsHook getLuckPermsHook() {
        return luckPermsHook;
    }

    // NUEVOS GETTERS
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public SkillBossBarManager getBossBarManager() {
        return bossBarManager;
    }

    public SkillManager getSkillManager() {
        return skillManager;
    }
}
