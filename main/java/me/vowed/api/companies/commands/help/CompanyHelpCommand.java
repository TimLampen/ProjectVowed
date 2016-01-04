package me.vowed.api.companies.commands.help;

import me.vowed.api.companies.commands.CommandManager;
import me.vowed.api.companies.commands.parent.CompanyCommand;
import me.vowed.api.companies.commands.parent.ParentCommand;
import me.vowed.api.companies.commands.sub.SubCommand;
import me.vowed.api.utils.Title;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Objects;

/**
 * Created by JPaul on 1/2/2016.
 */
public class CompanyHelpCommand extends SubCommand
{
    CommandManager commandManager;

    public CompanyHelpCommand(String name, CommandManager commandManager)
    {
        super(name);
        this.commandManager = commandManager;
    }

    @Override
    public int getMinimumArguments()
    {
        return 0;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args)
    {
        commandSender.sendMessage("");
        commandSender.sendMessage(Title.commandTitle("Vowed Company Commands"));

        for (SubCommand subCommand : commandManager.getSubcommands(commandManager.getCompanyCommand()))
        {
            if (!Objects.equals(subCommand.getName(), "help"))
            {
                String usage = ChatColor.DARK_AQUA + "/vowed company " + ChatColor.RESET + subCommand.getName();

                commandSender.sendMessage(usage);
            }
        }

        return true;
    }
}
