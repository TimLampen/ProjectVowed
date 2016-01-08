package me.vowed.api.companies.commands.sub.company;

import me.vowed.api.companies.Company;
import me.vowed.api.companies.commands.sub.SubCommand;
import me.vowed.api.plugin.Vowed;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by JPaul on 1/3/2016.
 */
public class DeleteCommand extends SubCommand
{
    public DeleteCommand(String label)
    {
        super(label);
    }

    @Override
    public int getMinimumArguments()
    {
        return 1;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args)
    {
        Company company = Vowed.getCompanyManager().getCompany((Player) commandSender, args[2]);

        if (company != null)
        {
            company.removeCompany();
            return true;
        }

        Vowed.LOG.debug("ok?");
        return false;
    }
}
