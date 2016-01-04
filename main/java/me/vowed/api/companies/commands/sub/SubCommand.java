package me.vowed.api.companies.commands.sub;

import me.vowed.api.companies.commands.parent.ParentCommand;
import org.bukkit.command.CommandSender;

/**
 * Created by JPaul on 1/2/2016.
 */
public abstract class SubCommand
{
    private String label;

    public SubCommand(String label)
    {
        this.label = label;
    }

    public String getName()
    {
        return this.label;
    }

    public abstract int getMinimumArguments();

    public abstract boolean onCommand(CommandSender commandSender, String[] args);
}
