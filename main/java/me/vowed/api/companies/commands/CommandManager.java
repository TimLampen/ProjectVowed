package me.vowed.api.companies.commands;

import me.vowed.api.companies.commands.help.CompanyHelpCommand;
import me.vowed.api.companies.commands.parent.CompanyCommand;
import me.vowed.api.companies.commands.parent.ItemCommand;
import me.vowed.api.companies.commands.parent.ParentCommand;
import me.vowed.api.companies.commands.help.MainHelpCommand;
import me.vowed.api.companies.commands.sub.SubCommand;
import me.vowed.api.companies.commands.sub.company.*;
import me.vowed.api.plugin.Vowed;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.*;

/**
 * Created by JPaul on 1/2/2016.
 */
public class CommandManager implements CommandExecutor
{
    CompanyCommand companyCommand = new CompanyCommand("company");
    ItemCommand itemCommand = new ItemCommand("item");
    MainHelpCommand mainHelpCommand = new MainHelpCommand("help", this);

    public final Map<ParentCommand, List<SubCommand>> commands = new HashMap<>();
    {
        commands.put(companyCommand, Arrays.asList(new SuicideCommand("suicide"),
                new CompanyHelpCommand("help", this),
                new EchoCommand("echo"),
                new CreateCommand("create"),
                new CreateHologramCommand("hologram"),
                new DeleteCommand("delete"),
                new GetList("list")));
    }

    public ParentCommand getParentCommand(String name)
    {
        switch (name.toLowerCase())
        {
            case "company":
                return companyCommand;
            case "item":
                return itemCommand;
        }

        return null;
    }

    public List<SubCommand> getSubcommands(ParentCommand parentCommand)
    {
        return commands.get(parentCommand);
    }

    public CompanyCommand getCompanyCommand()
    {
        return companyCommand;
    }

    public ItemCommand getItemCommand()
    {
        return itemCommand;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args)
    {

        if (args.length == 0 || args[0].equalsIgnoreCase("help") || getParentCommand(args[0]) == null)
        {
            return mainHelpCommand.onCommand(commandSender, args);
        }

        for (SubCommand sub : commands.get(getParentCommand(args[0])))
        {
            if (args.length - 1 >= 1)
            {
                if (sub.getName().equalsIgnoreCase(args[1]))
                {
                    int argsLength = args.length - 2;

                    if (argsLength >= sub.getMinimumArguments())
                    {
                        return sub.onCommand(commandSender, args);
                    } else
                    {
                        commandSender.sendMessage("Please enter the correct amount of arguments");
                        return false;
                    }
                }
            }
            else if (sub.getName().equalsIgnoreCase("help"))
            {
                return sub.onCommand(commandSender, args);
            }
        }
        // If no subcommands could handle this.
        commandSender.sendMessage("Invalid command");
        return false;
    }
}
