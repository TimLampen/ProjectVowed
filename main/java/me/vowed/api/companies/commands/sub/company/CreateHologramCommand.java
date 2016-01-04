package me.vowed.api.companies.commands.sub.company;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.vowed.api.companies.CompanyListener;
import me.vowed.api.companies.commands.sub.SubCommand;
import me.vowed.api.plugin.Vowed;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by JPaul on 1/2/2016.
 */
public class CreateHologramCommand extends SubCommand
{

    public CreateHologramCommand(String label)
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

        return true;
    }
}
