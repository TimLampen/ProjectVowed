package me.vowed.api.player;

import me.vowed.api.health.Health;
import me.vowed.api.health.IHealth;
import me.vowed.api.money.IMoney;
import me.vowed.api.money.Money;
import me.vowed.api.plugin.Vowed;
import me.vowed.api.race.Race;
import me.vowed.api.race.races.Dwarf;
import me.vowed.api.shops.IShop;
import me.vowed.api.skins.Skin;
import me.vowed.api.skins.SkinUtil;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by JPaul on 11/13/2015.
 */
public class PlayerWrapper
{
    Player player;
    public PlayerWrapper(Player player)
    {
        this.player = player;
        Vowed.getMoneyManager().setMoney(player, new Money(1000));
        Vowed.getHealthManager().setHealth(player, new Health());
    }

    public IMoney getMoney()
    {
        return Vowed.getMoneyManager().getMoney(this.player);
    }

    public Race getRace()
    {
        return Vowed.getRaceManager().getRace(this.player);
    }

    public IShop getShop()
    {
        return Vowed.getShopManager().getShop(this.player);
    }

    public IHealth getHealth()
    {
        return Vowed.getHealthManager().getHealth(this.player);
    }

    public void setMoney(Player player, double amount)
    {
        Vowed.getMoneyManager().setMoney(player, new Money((int) amount));
    }

    public void setRace(Race race)
    {
        Vowed.getRaceManager().setRace(this.player, race);
    }

    public Player getPlayer()
    {
        return player;
    }
}
