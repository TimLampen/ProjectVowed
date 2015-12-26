package me.vowed.api.shops;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * Created by JPaul on 11/13/2015.
 */
public interface IShopManager
{
    IShop createShop(Player owner, Location location, ShopType shopType);

    IShop getShop(Player player);

    IShop shopFromLocation(Location location);

    boolean isOwner(IShop shop, Player player);
}
