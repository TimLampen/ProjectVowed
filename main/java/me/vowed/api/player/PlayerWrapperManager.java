package me.vowed.api.player;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by JPaul on 11/14/2015.
 */
public class PlayerWrapperManager
{
    private static HashMap<UUID, PlayerWrapper> wrappedPlayers = new HashMap<>();

    public static PlayerWrapper getPlayerWrapper(Player player)
    {
        return wrappedPlayers.get(player.getUniqueId());
    }

    public static void setWrappedPlayer(Player player, PlayerWrapper playerWrapper)
    {
        if (!wrappedPlayers.containsKey(player.getUniqueId()))
        {
            wrappedPlayers.put(player.getUniqueId(), playerWrapper);
        }
    }
}
