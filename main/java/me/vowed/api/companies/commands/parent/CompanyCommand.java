package me.vowed.api.companies.commands.parent;

import me.vowed.api.companies.commands.sub.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Created by JPaul on 1/2/2016.
 */
public class CompanyCommand extends ParentCommand
{
    public CompanyCommand(String name)
    {
        super(name);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args)
    {
        return false;
    }
}
