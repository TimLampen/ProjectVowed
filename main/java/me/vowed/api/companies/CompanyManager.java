package me.vowed.api.companies;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.vowed.api.companies.types.CompanyType;
import me.vowed.api.plugin.Vowed;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by JPaul on 12/28/2015.
 */
public class CompanyManager
{
    HashMap<UUID, List<Company>> companyOwner = new HashMap<>();
    HashMap<String, Company> companyLocation = new HashMap<>();
    List<Company> companies = new ArrayList<>();

    public Company getInstance(String name, CompanyType type, Player player, Location center, ProtectedRegion region)
    {
        Company company = new Company(name, type, player, center, region);

        if (companyOwner.get(player.getUniqueId()) != null)
        {
            companyOwner.get(player.getUniqueId()).add(company);
            Vowed.LOG.debug(companyOwner.toString());
        }
        else
        {
            List<Company> companies = new ArrayList<>();
            companies.add(company);
            companyOwner.put(player.getUniqueId(), companies);
        }
        companyLocation.put(center.toString(), company);
        companies.add(company);

        return company;
    }

    public Company getCompany(Player player, String name)
    {
        Vowed.LOG.debug(companyOwner.toString());
        for (Company company : companyOwner.get(player.getUniqueId()))
        {
            Vowed.LOG.debug("Company name: " + company.getName() + " Name: " + name);
            if (company.getName().equalsIgnoreCase(name))
            {
                return company;
            }
        }

        return null;
    }

    public Company getNewestCompany(Player player)
    {
        return companyOwner.get(player.getUniqueId()).get(companyOwner.get(player.getUniqueId()).size() - 1);
    }

    public Company getCompany(Location location)
    {
        if (companyLocation.containsKey(location.toString()))
        {
            return companyLocation.get(location.toString());
        }

        return null;
    }

    public List<Company> getCompanies()
    {
        return companies;
    }
}
