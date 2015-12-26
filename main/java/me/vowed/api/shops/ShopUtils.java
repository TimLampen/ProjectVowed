package me.vowed.api.shops;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Created by JPaul on 11/9/2015.
 */
public class ShopUtils
{
    Block block;

    public static boolean isShop(Block block)
    {
        return block.hasMetadata("shop");
    }

    public static IShop getShopByPlayer(Player player)
    {
        return Shop.iShopLocaterPlayer.get(player);
    }

    public static IShop getShopByLocation(Location location)
    {
        return Shop.iShopLocaterLocation.get(location);
    }
}
