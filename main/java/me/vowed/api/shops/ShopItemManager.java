package me.vowed.api.shops;

import me.vowed.api.plugin.Vowed;
import net.minecraft.server.v1_8_R3.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by JPaul on 11/14/2015.
 */
public class ShopItemManager
{

    private static HashMap<IShop, List<ShopItem>> shopItems = new HashMap<>();

    public static List<ShopItem> getShopItems(IShop shop)
    {
        return shopItems.get(shop);
    }

    public static void setShopItems(IShop shop, List<ShopItem> shopItem)
    {
        shopItems.put(shop, shopItem);
    }

    public static void addShopItems(IShop shop, ShopItem shopItem)
    {
        if (shopItems.get(shop) == null)
        {
            List<ShopItem> shopItems = new ArrayList<>();
            shopItems.add(shopItem);
            ShopItemManager.shopItems.put(shop, shopItems);
        } else
        {
            shopItems.get(shop).add(shopItem);
        }
    }

    public static ShopItem getShopItem(IShop shop, UniqueItem uniqueItem)
    {
        for (ShopItem shopItemFinder : shop.getContents())
        {
            Vowed.LOG.debug(shopItemFinder.getUUID());
            Vowed.LOG.debug(uniqueItem.toString());
            if (shopItemFinder.getUUID().equals(uniqueItem.getUUID()))
            {
                Vowed.LOG.severe("ok wtf");
                return shopItemFinder;
            }

        }
        return null;
    }

    public static ShopItem getShopItemFrom(IShop shop, ItemStack itemStack)
    {
        return null;
    }
}
