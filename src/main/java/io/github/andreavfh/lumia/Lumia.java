package io.github.andreavfh.lumia;

import io.github.andreavfh.lumia.commands.CommandHandler;
import io.github.andreavfh.lumia.commands.sub.Help;
import io.github.andreavfh.lumia.config.Config;
import io.github.andreavfh.lumia.config.LanguageConfig;
import io.github.andreavfh.lumia.integrations.LuckPermsHook;
import io.github.andreavfh.lumia.utils.LoggerWrapper;
import org.bukkit.plugin.java.JavaPlugin;

public final class Lumia extends JavaPlugin {

    private LanguageConfig languageConfig;
    private LoggerWrapper logger;
    private LuckPermsHook luckPermsHook;
    private Config config;

    @Override
    public void onEnable() {
        this.config = new Config(this);
        this.languageConfig = new LanguageConfig(this, config);
        this.logger = new LoggerWrapper(getLogger(), languageConfig.getPrefix());

        logger.info("Initializing...");

        // Inicializar integración con LuckPerms
        this.luckPermsHook = new LuckPermsHook(this);
        if (luckPermsHook.isHooked()) {
            logger.info("LuckPerms integration enabled. You can use permissions in commands and features.");
        } else {
            logger.warning("LuckPerms integration not found. Some features may not work as expected. Make sure LuckPerms is installed and enabled.");
        }

        // Comandos
        CommandHandler handler = new CommandHandler(this);
        getCommand("lumia").setExecutor(handler);
        getCommand("lumia").setTabCompleter(handler);

        handler.registerSubCommand(new Help(this, handler));

        logger.info(languageConfig.getMessage("plugin_enabled"));
    }

    @Override
    public void onDisable() {
        logger.info(languageConfig.getMessage("plugin_disabled"));
    }

    public LoggerWrapper getLumiaLogger() {
        return logger;
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
}
