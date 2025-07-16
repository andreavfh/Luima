package io.github.andreavfh.lumia.commands.sub;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.commands.ManualRegistration;
import io.github.andreavfh.lumia.commands.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

@ManualRegistration
public class Help implements SubCommand {

    private final Lumia plugin;
    private final SubCommandProvider provider;

    public Help(Lumia plugin, SubCommandProvider provider) {
        this.plugin = plugin;
        this.provider = provider;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Show available commands";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        var lang = plugin.getLanguageConfig();
        var formatter = plugin.getMessageFormatter();

        // Título del listado
        formatter.sendMessage(sender, lang.getRaw("help-title"));

        // Para cada subcomando, mostramos línea formateada
        provider.getSubCommands().forEach(sub -> {
            String raw = lang.getRaw("help-entry")
                    .replace("{command}", sub.getName())
                    .replace("{description}", sub.getDescription());
            formatter.sendMessage(sender, raw);
        });
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
