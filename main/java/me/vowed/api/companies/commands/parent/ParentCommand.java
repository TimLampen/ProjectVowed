package me.vowed.api.companies.commands.parent;

import me.vowed.api.companies.commands.sub.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Created by JPaul on 1/2/2016.
 */
public abstract class ParentCommand
{
    String label;

    public ParentCommand(String name)
    {
        this.label = name;
    }

    public String getName()
    {
        return label;
    }

    public abstract boolean onCommand(CommandSender commandSender, String[] args);
}
