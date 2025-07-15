package io.github.andreavfh.lumia.commands.sub;

import io.github.andreavfh.lumia.commands.SubCommand;

import java.util.Collection;

public interface SubCommandProvider {
    Collection<SubCommand> getSubCommands();
}
