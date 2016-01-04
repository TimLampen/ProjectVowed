package me.vowed.api.companies.commands.help;

import com.gmail.filoghost.holographicdisplays.commands.Strings;
import me.vowed.api.companies.commands.CommandManager;
import me.vowed.api.companies.commands.parent.ParentCommand;
import me.vowed.api.companies.commands.sub.SubCommand;
import me.vowed.api.plugin.Vowed;
import me.vowed.api.utils.Title;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Objects;

/**
 * Created by JPaul on 1/2/2016.
 */
public class MainHelpCommand extends ParentCommand
{
    CommandManager commandManager;

    public MainHelpCommand(String label, CommandManager commandManager)
    {
        super(label);
        this.commandManager = commandManager;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args)
    {
        commandSender.sendMessage("");
        commandSender.sendMessage(Title.commandTitle("Vowed Commands"));


        for (ParentCommand parentCommand : commandManager.commands.keySet())
        {
            if (!Objects.equals(parentCommand.getName(), "help"))
            {
                String usage = ChatColor.DARK_AQUA + "/vowed " + ChatColor.RESET + parentCommand.getName();

                commandSender.sendMessage(usage);
            }
        }

        return true;
    }
}
