package me.vowed.api.companies.commands.sub.company;

import me.vowed.api.companies.commands.parent.ParentCommand;
import me.vowed.api.companies.commands.sub.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by JPaul on 1/2/2016.
 */
public class SuicideCommand extends SubCommand
{
    public SuicideCommand(String label)
    {
        super(label);
    }

    @Override
    public int getMinimumArguments()
    {
        return 0;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args)
    {
        if (commandSender instanceof Player)
        {
            commandSender.sendMessage("Killing you");
            ((Player) commandSender).setHealth(0D);
            // Command handled
            return true;
        }

        // Not a player ;(
        return false;
    }
}
