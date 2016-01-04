package me.vowed.api.companies.commands.sub.company;

import me.vowed.api.companies.commands.sub.SubCommand;
import me.vowed.api.plugin.Vowed;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * Created by JPaul on 1/2/2016.
 */
public class EchoCommand extends SubCommand
{
    public EchoCommand(String label)
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
        commandSender.sendMessage(Arrays.toString(args));

        return true;
    }
}
