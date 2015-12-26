package me.vowed.api.player;

import me.vowed.api.plugin.Vowed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 * Created by JPaul on 11/22/2015.
 */
public class PlayerWrapperListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(PlayerLoginEvent joinEvent)
    {
        PlayerWrapperManager.setWrappedPlayer(joinEvent.getPlayer(), new PlayerWrapper(joinEvent.getPlayer()));
    }
}
