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
    HashMap<UUID, Company> companyOwner = new HashMap<>();
    HashMap<String, Company> companyLocation = new HashMap<>();
    List<Company> companies = new ArrayList<>();

    public Company getInstance(String name, CompanyType type, Player player, Location center, ProtectedRegion region)
    {
        Company company = new Company(name, type, player, center, region);

        companyOwner.put(player.getUniqueId(), company);
        companyLocation.put(center.toString(), company);
        companies.add(company);

        return company;
    }

    public Company getCompany(Player player)
    {
        return companyOwner.get(player.getUniqueId());
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
