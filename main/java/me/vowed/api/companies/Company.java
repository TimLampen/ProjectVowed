package me.vowed.api.companies;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.vowed.api.companies.types.CompanyType;
import me.vowed.api.plugin.Vowed;
import me.vowed.api.utils.worldguard.WorldGuardUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import javax.swing.text.PlainDocument;
import java.util.Iterator;
import java.util.List;

/**
 * Created by JPaul on 12/28/2015.
 */
public class Company
{
    String name;
    CompanyType type;
    Player owner;
    Location centerLocation;
    ProtectedRegion region;
    List<Hologram> holograms;
    boolean open;

    public Company(String name, CompanyType type, Player owner, Location center, ProtectedRegion region)
    {
        this.name = name;
        this.type = type;
        this.owner = owner;
        this.centerLocation = center;
        this.region = region;
    }

    public String getName()
    {
        return name;
    }

    public CompanyType getType()
    {
        return type;
    }

    public Player getOwner()
    {
        return owner;
    }

    public boolean isOpen()
    {
        return open;
    }

    public Location getLocation()
    {
        return centerLocation;
    }

    public ProtectedRegion getRegion()
    {
        return region;
    }

    public List<Hologram> getHolograms()
    {
        return holograms;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setType(CompanyType type)
    {
        this.type = type;
    }

    public void setOwner(Player owner)
    {
        this.owner = owner;
    }

    public void setOpen(boolean open)
    {
        this.open = open;
    }

    public void setLocation(Location centerLocation)
    {
        this.centerLocation = centerLocation;
    }

    public void setRegion(ProtectedRegion region)
    {
        this.region = region;
    }

    public void setHolograms(List<Hologram> holograms)
    {
        this.holograms = holograms;
    }

    public void removeCompany()
    {
        Location loc1 = new Location(getLocation().getWorld(), getRegion().getMaximumPoint().getBlockX(), getRegion().getMaximumPoint().getBlockY(), getRegion().getMaximumPoint().getBlockZ());
        Location loc2 = new Location(getLocation().getWorld(), getRegion().getMinimumPoint().getBlockX(), getRegion().getMinimumPoint().getBlockY(), getRegion().getMinimumPoint().getBlockZ());
        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        for(int x = minX; x <= maxX; x++){
            for(int y = minY; y <= maxY; y++){
                for(int z = minZ; z <= maxZ; z++){
                    Block block = getLocation().getWorld().getBlockAt(x, y, z);
                    block.setType(Material.AIR);
                }
            }
        }
        WorldGuardUtil.getWorldGuard().getRegionManager(getLocation().getWorld()).removeRegion(getRegion().getId());

        if (getHolograms() != null)
        {
            for (Hologram hologram : getHolograms())
            {
                hologram.delete();
            }
        }

        Vowed.LOG.severe(Vowed.getCompanyManager().companyOwner.get(getOwner().getUniqueId()).size());

        Vowed.getCompanyManager().companyLocation.remove(getLocation().toString());
        Vowed.getCompanyManager().companies.remove(this);

        if (Vowed.getCompanyManager().companyOwner.get(getOwner().getUniqueId()).size() >= 2)
        {
            Iterator<Company> companyIterator = Vowed.getCompanyManager().companyOwner.get(getOwner().getUniqueId()).iterator();
            while (companyIterator.hasNext())
            {
                Company company = companyIterator.next();


                if (company.getName().equals(getName()))
                {
                    companyIterator.remove();
                    Vowed.LOG.warning(Vowed.getCompanyManager().companyOwner.toString());
                    Vowed.getCompanyManager().companyOwner.get(getOwner().getUniqueId()).remove(company);
                    Vowed.LOG.severe(Vowed.getCompanyManager().companyOwner.toString());
                }
            }
        }
        else
        {
            Vowed.getCompanyManager().companyOwner.remove(getOwner().getUniqueId());
        }
    }
}
