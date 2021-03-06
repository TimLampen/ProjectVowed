package me.vowed.api.shops;

import me.vowed.api.plugin.Vowed;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by JPaul on 11/13/2015.
 */
public class ShopManager implements IShopManager
{
    HashMap<UUID, Shop> shop = new HashMap<>();
    ArrayList<IShop> shops = new ArrayList<>();
    HashMap<Location, IShop> locations = new HashMap<>();

    public ArrayList<IShop> getShops()
    {
        return shops;
    }

    @Override
    public IShop createShop(Player owner, Location location, ShopType shopType)
    {
        IShop shop = new Shop(owner, this, owner.getName(), location, shopType);
        shops.add(shop);
        Location blockLocation = new Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
        Location blockRelativeLocation = blockLocation.getBlock().getRelative(BlockFace.WEST).getLocation();
        locations.put(blockLocation, shop);
        locations.put(blockRelativeLocation, shop);
        return shop;
    }

    @Override
    public IShop getShop(Player player)
    {
        for (IShop iShop : shops)
        {
            if (iShop.getOwnerUUID() == player.getUniqueId())
            {
                return iShop;
            }
        }

        return null;
    }

    @Override
    public IShop getShop(Location location)
    {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        Location shopLocation = new Location(location.getWorld(), x, y, z);

        if (locations.containsKey(shopLocation))
        {
            return locations.get(shopLocation);
        }

        return null;
    }

    @Override
    public boolean isOwner(IShop shop, Player player)
    {
        return shop.getOwnerUUID() == player.getUniqueId();
    }
}
