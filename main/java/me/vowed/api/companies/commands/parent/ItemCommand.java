package me.vowed.api.companies.commands.parent;

import me.vowed.api.companies.commands.sub.SubCommand;
import org.bukkit.command.CommandSender;

/**
 * Created by JPaul on 1/8/2016.
 */
public class ItemCommand extends ParentCommand
{
    public ItemCommand(String name)
    {
        super(name);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args)
    {
        return false;
    }
}
