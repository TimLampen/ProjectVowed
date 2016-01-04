package me.vowed.api.companies.commands.sub.company;

import me.vowed.api.companies.Company;
import me.vowed.api.companies.commands.sub.SubCommand;
import me.vowed.api.plugin.Vowed;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by JPaul on 1/3/2016.
 */
public class GetList extends SubCommand
{
    HashMap<Integer, List<String[]>> info = new HashMap<>();

    public GetList(String label)
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

        List<String[]> infoPages = new ArrayList<>();

        for (Company company : Vowed.getCompanyManager().getCompanies())
        {
            for (int i = 0; i < Vowed.getCompanyManager().getCompanies().size(); i++)
            {
                String[] infoPage = {company.getName(), company.getType().name(), company.getLocation().toString(), String.valueOf(company.isOpen()).toUpperCase()};
                if (i % 5 == 0)
                {
                    int counter = 0;
                    counter++;
                    info.put(counter, infoPages);
                }

                infoPages.add(infoPage);

            }
        }

        for (int i = 0; i < infoPages.size(); i++)
        {
            for (String[] info : infoPages)
            {
                Vowed.LOG.debug(Arrays.toString(info));
            }
        }

        return true;
    }
}
