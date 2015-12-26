package me.vowed.api.money;

import me.vowed.api.money.currency.CurrencyFactory;
import me.vowed.api.player.PlayerWrapper;
import me.vowed.api.plugin.Vowed;
import me.vowed.api.race.Race;
import me.vowed.api.race.races.RaceType;
import me.vowed.api.shops.ShopItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created by JPaul on 11/13/2015.
 */
public class MoneyManager implements IMoneyManager
{
    private HashMap<Race, ItemStack> currencies = new HashMap<>();
    private HashMap<Player, IMoney> money = new HashMap<>();

    @Override
    public ItemStack getCurrency(Race race)
    {
        return currencies.get(race);
    }

    @Override
    public IMoney getMoney(Player player)
    {
        Vowed.LOG.info(money.toString());
        return money.get(player);
    }

    @Override
    public void setCurrency(Race race, ItemStack currency)
    {
        this.currencies.put(race, currency);
    }

    @Override
    public void setMoney(Player player, IMoney money)
    {
        if (!this.money.containsKey(player))
        {
            this.money.put(player, money);
        }
    }

    @Override
    public Integer handleSellerMoney(PlayerWrapper sellerWrapper, ShopItem shopItem)
    {
        //dwarfs
        if (sellerWrapper.getRace().getType() == RaceType.DWARF)
        {
            return shopItem.getDwarfINTPrice();
        }
        else if (sellerWrapper.getRace().getType() == RaceType.HUMAN)
        {
            return shopItem.getHumanINTPrice();
        }
        else if (sellerWrapper.getRace().getType() == RaceType.ELF)
        {
            return shopItem.getElfINTPrice();
        }

        return null; //really want it to make an exception
    }

    @Override
    public Integer handleBuyerMoney(PlayerWrapper buyerWrapper, ShopItem shopItem)
    {
        //dwarfs
        if (buyerWrapper.getRace().getType() == RaceType.ELF)
        {
            return shopItem.getElfINTPrice();
        }
        else if (buyerWrapper.getRace().getType() == RaceType.HUMAN)
        {
            return shopItem.getHumanINTPrice();
        }
        else if (buyerWrapper.getRace().getType() == RaceType.DWARF)
        {
            return shopItem.getDwarfINTPrice();
        }

        return null; //really want it to make an exception
    }
}
