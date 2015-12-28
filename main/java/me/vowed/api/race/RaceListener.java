package me.vowed.api.race;

import me.vowed.api.player.PlayerWrapper;
import me.vowed.api.player.PlayerWrapperManager;
import me.vowed.api.plugin.Vowed;
import me.vowed.api.race.races.Gender;
import me.vowed.api.skins.Skin;
import me.vowed.api.skins.SkinUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by JPaul on 11/17/2015.
 */
public class RaceListener implements Listener
{
    private HashMap<UUID, Boolean> isHandling = new HashMap<>();
    private List<String> raceNames = Arrays.asList("Dwarf", "Elf", "Human");

    @EventHandler
    public void on(PlayerJoinEvent joinEvent) throws SQLException, IOException
    {
        Player player = joinEvent.getPlayer();

        PreparedStatement setFirstLogin = Vowed.getDatabase().prepareStatement("INSERT INTO player_info ('UUID', 'first_login') VALUES ('" + player.getUniqueId().toString() + "', 'FALSE');");
        setFirstLogin.executeUpdate();

        PreparedStatement getRace = Vowed.getDatabase().prepareStatement("SELECT UUID FROM player_info");
        getRace.executeUpdate();
        ResultSet resultSet = Vowed.getDatabase().getResultSet(getRace);

        if (resultSet.next())
        {
            Vowed.LOG.debug(resultSet.toString());
        }

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                player.sendRawMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Enter your desired Race + Gender");
            }
        }.runTaskLater(Vowed.getPlugin(), 30);
    }

    @EventHandler
    public void on(PlayerEggThrowEvent eggThrowEvent)
    {
        Player player = eggThrowEvent.getPlayer();
        this.isHandling.put(player.getUniqueId(), true);
    }

    @EventHandler
    public void on(AsyncPlayerChatEvent chatEvent)
    {
        Player player = chatEvent.getPlayer();
        PlayerWrapper playerWrapper = PlayerWrapperManager.getPlayerWrapper(player);
        String[] args = chatEvent.getMessage().split(" ");
        List<String> argList = Arrays.asList(args);


        if (this.isHandling.get(player.getUniqueId()) != null)
        {
            if (this.isHandling.get(player.getUniqueId()))
            {
                chatEvent.setCancelled(true);
                int oneTime = 0;

                for (String raceChecker : raceNames)
                {
                    oneTime++;

                    if (oneTime == 1)
                    {
                        if (containsCaseInsensitive(raceChecker, argList) || StringUtils.containsIgnoreCase(chatEvent.getMessage(), "male") ||
                                StringUtils.containsIgnoreCase(chatEvent.getMessage(), "female"))
                        {
                            if (StringUtils.containsIgnoreCase(chatEvent.getMessage(), argList.get(0)))
                            {
                                playerWrapper.setRace(Vowed.getRaceManager().getRace(argList.get(0).toUpperCase(), Gender.valueOf(argList.get(1).toUpperCase())));
                                Skin skin = SkinUtil.getSkinFromUUID(playerWrapper.getRace().getSkin());
                                SkinUtil.addToEntry(player, skin);
                                SkinUtil.saveSkins();

                                if (skin.getResponseCode() != null && skin.getResponseCode() != 429)
                                {
                                    Bukkit.getScheduler().runTask(Vowed.getPlugin(), () -> player.kickPlayer(ChatColor.AQUA.toString() + ChatColor.BOLD + "Sorry, this is a one time thing for Skin Handling"));
                                    this.isHandling.put(player.getUniqueId(), false);
                                } else
                                {
                                    Bukkit.getScheduler().runTask(Vowed.getPlugin(), () -> player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "No proxy available, please wait 30 - 60 seconds then enter your desired race + gender again"));
                                }
                            }
                        } else
                        {
                            String race = ChatColor.DARK_RED.toString() + ChatColor.BOLD + ChatColor.UNDERLINE + "Race" + ChatColor.RESET + ChatColor.DARK_RED + ChatColor.BOLD;
                            String gender = ChatColor.DARK_RED.toString() + ChatColor.BOLD + ChatColor.UNDERLINE + "Gender";
                            player.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Please enter your desired " + race + " then " + gender);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void on(PlayerQuitEvent quitEvent)
    {
        SkinUtil.saveSkins();
    }

    public boolean containsCaseInsensitive(String s, List<String> l)
    {
        for (String string : l)
        {
            if (string.equalsIgnoreCase(s))
            {
                return true;
            }
        }
        return false;
    }
}
