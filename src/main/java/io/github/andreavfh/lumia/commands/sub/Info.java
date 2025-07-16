package io.github.andreavfh.lumia.commands.sub;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.commands.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class Info implements SubCommand {

    private final Lumia plugin;

    public Info(Lumia plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Displays plugin information";
    }

    @Override
    public String getPermission() {
        return "lumia.info";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        var lang = plugin.getLanguageConfig();
        var formatter = plugin.getMessageFormatter();

        formatter.sendMessage(sender, lang.getRaw("info-title"));
        formatter.sendMessage(sender, lang.getRaw("info-name"));
        formatter.sendMessage(sender, lang.getRaw("info-version"));
        formatter.sendMessage(sender, lang.getRaw("info-author"));
        formatter.sendMessage(sender, lang.getRaw("info-description"));
        formatter.sendMessage(sender, lang.getRaw("info-website"));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
