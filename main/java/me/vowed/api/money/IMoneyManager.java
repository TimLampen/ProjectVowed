package me.vowed.api.money;

import me.vowed.api.player.PlayerWrapper;
import me.vowed.api.race.Race;
import me.vowed.api.shops.ShopItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by JPaul on 11/14/2015.
 */
public interface IMoneyManager
{
    IMoney getMoney(Player player);

    ItemStack getCurrency(Race race);

    void setCurrency(Race race, ItemStack currency);

    void setMoney(Player player, IMoney money);

    Integer handleSellerMoney(PlayerWrapper playerWrapper, ShopItem shopItem);

    Integer handleBuyerMoney(PlayerWrapper playerWrapper, ShopItem shopItem);
}
