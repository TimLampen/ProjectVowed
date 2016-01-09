package me.vowed.api.companies.commands.help;

import me.vowed.api.companies.commands.CommandManager;
import me.vowed.api.companies.commands.sub.SubCommand;
import me.vowed.api.utils.Title;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Objects;

/**
 * Created by JPaul on 1/8/2016.
 */
public class ItemHelpCommand extends SubCommand
{
    CommandManager commandManager;

    public ItemHelpCommand(String label, CommandManager commandManager)
    {
        super(label);
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
        commandSender.sendMessage(Title.commandTitle("Vowed Item Commands"));

        for (SubCommand subCommand : commandManager.getSubcommands(commandManager.getItemCommand()))
        {
            if (!Objects.equals(subCommand.getName(), "help"))
            {
                String usage = ChatColor.DARK_AQUA + "/vowed item " + ChatColor.RESET + subCommand.getName();

                commandSender.sendMessage(usage);
            }
        }

        return true;
    }
}
