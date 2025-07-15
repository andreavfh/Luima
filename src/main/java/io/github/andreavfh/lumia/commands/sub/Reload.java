package io.github.andreavfh.lumia.commands.sub;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.commands.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class Reload implements SubCommand {

    private final Lumia plugin;

    public Reload(Lumia plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reloads the plugin configuration and languages";
    }

    @Override
    public String getPermission() {
        return "lumia.reload";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        plugin.getPluginConfig().load();
        plugin.getLanguageConfig().reload();

        sender.sendMessage(plugin.getLanguageConfig().format("reload-success"));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
