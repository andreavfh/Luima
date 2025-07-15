package io.github.andreavfh.lumia.commands;

import io.github.andreavfh.lumia.Lumia;
import io.github.andreavfh.lumia.commands.sub.SubCommandProvider;
import io.github.andreavfh.lumia.config.LanguageConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.*;

public class CommandHandler implements CommandExecutor, TabCompleter, SubCommandProvider {

         private final Map<String, SubCommand> subCommands = new LinkedHashMap<>();


        private final Lumia plugin;

        public CommandHandler(Lumia plugin) {
            this.plugin = plugin;
            registerSubCommands(plugin);
        }


    private void registerSubCommands(Lumia plugin) {
        Reflections reflections = new Reflections("io.github.andreavfh.lumia.commands.sub");

        Set<Class<? extends SubCommand>> commandClasses = reflections.getSubTypesOf(SubCommand.class);
        for (Class<? extends SubCommand> clazz : commandClasses) {

            if (clazz.isAnnotationPresent(ManualRegistration.class)) continue;

            try {
                Constructor<? extends SubCommand> ctor = clazz.getDeclaredConstructor(Lumia.class);
                SubCommand cmd = ctor.newInstance(plugin);
                registerSubCommand(cmd);
            } catch (Exception e) {
                plugin.getLogger().warning("Could not load subcommand: " + clazz.getName());
                e.printStackTrace();
            }
        }
    }


    public void registerSubCommand(SubCommand cmd) {
        subCommands.put(cmd.getName().toLowerCase(), cmd);
    }

    public Collection<SubCommand> getSubCommands() {
        return subCommands.values();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        LanguageConfig lang = plugin.getLanguageConfig();

        if (args.length == 0) {
            sender.sendMessage(lang.format("use-help"));
            return true;
        }


        SubCommand sub = subCommands.get(args[0].toLowerCase());
        if (sub == null) {
            sender.sendMessage(lang.format("command-not-found"));
            return true;
        }

        if (sub.getPermission() != null && !sender.hasPermission(sub.getPermission())) {
            sender.sendMessage(lang.format("no-permission"));
            return true;
        }

        sub.execute(sender, Arrays.copyOfRange(args, 1, args.length));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            List<String> completions = new ArrayList<>();
            for (SubCommand sub : subCommands.values()) {
                if (sub.getName().startsWith(partial)) {
                    completions.add(sub.getName());
                }
            }
            return completions;
        }

        SubCommand sub = subCommands.get(args[0].toLowerCase());
        if (sub != null) {
            return sub.tabComplete(sender, Arrays.copyOfRange(args, 1, args.length));
        }

        return Collections.emptyList();
    }
}
