package io.github.andreavfh.lumia.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface SubCommand {
    /**
     * Returns the name of the subcommand.
     *
     * @return The name of the subcommand.
     */
    String getName();

    /**
     * Returns the description of the subcommand.
     *
     * @return The description of the subcommand.
     */
    String getDescription();

    /**
     * Returns the required permission to execute the subcommand.
     *
     * @return The permission string.
     */
    String getPermission();

    /**
     * Executes the subcommand with the given sender and arguments.
     *
     * @param sender The sender executing the command.
     * @param args   The arguments provided with the command.
     */
    void execute(CommandSender sender, String[] args);

    /**
     * Provides tab completion suggestions for the subcommand.
     *
     * @param sender The sender requesting tab completion.
     * @param args   The arguments provided so far.
     * @return A list of tab completion suggestions.
     */
    List<String> tabComplete(CommandSender sender, String[] args);
}
