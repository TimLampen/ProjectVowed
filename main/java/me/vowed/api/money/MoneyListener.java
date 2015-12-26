package me.vowed.api.money;

import me.vowed.api.money.currency.CurrencyFactory;
import me.vowed.api.player.PlayerWrapper;
import me.vowed.api.player.PlayerWrapperManager;
import me.vowed.api.plugin.Vowed;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.math.BigDecimal;

/**
 * Created by JPaul on 11/14/2015.
 */
public class MoneyListener implements Listener
{
    @EventHandler
    public void on(PlayerJoinEvent joinEvent)
    {
        Player player = joinEvent.getPlayer();
        PlayerWrapper playerWrapper = PlayerWrapperManager.getPlayerWrapper(player);
        playerWrapper.setMoney(player, 1000);

        Vowed.LOG.info(String.valueOf(CurrencyFactory.getInstance("DWARF").convertCurrencyToELF(BigDecimal.valueOf(playerWrapper.getMoney().getAmount()))));
        Vowed.LOG.info(String.valueOf(CurrencyFactory.getInstance("DWARF").convertCurrencyToHUMAN(BigDecimal.valueOf(playerWrapper.getMoney().getAmount()))));
        Vowed.LOG.info(String.valueOf(CurrencyFactory.getInstance("DWARF").convertCurrencyToDWARF(BigDecimal.valueOf(playerWrapper.getMoney().getAmount()))));
    }
}
