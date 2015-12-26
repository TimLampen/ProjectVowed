package me.vowed.api.stocks;

import me.vowed.api.plugin.Vowed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by JPaul on 11/12/2015.
 */
public class Test implements Listener
{
    @EventHandler
    public void on(AsyncPlayerChatEvent chatEvent)
    {
        if (chatEvent.getMessage().equals("FB"))
        {
            Stock facebook = StockFetcher.getStock("FB");
            Vowed.LOG.info(String.valueOf(facebook.getPrice()));
            Vowed.LOG.info(String.valueOf(facebook.getWeek52high()));
        }
    }
}
