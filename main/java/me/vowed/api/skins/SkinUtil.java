package me.vowed.api.skins;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.vowed.api.plugin.Vowed;
import me.vowed.api.plugin.VowedPlugin;
import me.vowed.api.race.RaceListener;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by JPaul on 11/18/2015.
 */
public class SkinUtil
{
    public static HashMap<UUID, Skin> skins = new HashMap<>();

    public static void setSkin(Player player, Skin skin)
    {
        GameProfile gameProfile = ((CraftPlayer) player).getProfile();
        gameProfile.getProperties().clear();
        gameProfile.getProperties().put(skin.getName(), new Property(skin.getName(), skin.getValue(), skin.getSignature()));

        for (Player onlinePlayers : Vowed.getPlugin().getServer().getOnlinePlayers())
        {
            onlinePlayers.hidePlayer(player);
        }

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for (Player onlinePlayers : Vowed.getPlugin().getServer().getOnlinePlayers())
                {
                    onlinePlayers.showPlayer(player);
                }
            }
        }.runTaskLater(Vowed.getPlugin(), 20);
    }

    public static void setSkin(Player player, String name)
    {
        Skin skin = getSkinFromString(name);

        GameProfile gameProfile = ((CraftPlayer) player).getProfile();
        gameProfile.getProperties().clear();
        Vowed.LOG.info(skin.getName());
        Vowed.LOG.info(skin.getValue());
        Vowed.LOG.info(skin.getSignature());
        gameProfile.getProperties().put(skin.getName(), new Property(skin.getName(), skin.getValue(), skin.getSignature()));

        for (Player onlinePlayers : Vowed.getPlugin().getServer().getOnlinePlayers())
        {
            onlinePlayers.hidePlayer(player);
        }

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for (Player onlinePlayers : Vowed.getPlugin().getServer().getOnlinePlayers())
                {
                    onlinePlayers.showPlayer(player);
                }
            }
        }.runTaskLater(Vowed.getPlugin(), 20);

        skins.put(player.getUniqueId(), skin);
    }

    public static void addToEntry(Player player, Skin skin)
    {
        if (!skins.containsKey(player.getUniqueId()))
        {
            skins.put(player.getUniqueId(), skin);
        }
        else
        {
            skins.remove(player.getUniqueId());
            skins.put(player.getUniqueId(), skin);
        }
    }

    public static void createFile()
    {
        File skinManager = new File("C:\\Users\\JPaul\\Desktop\\Server\\plugins\\VowedCore\\SkinManager");
        skinManager.mkdirs();

        File items = new File("C:\\Users\\JPaul\\Desktop\\Server\\plugins\\VowedCore\\Items");
        items.mkdirs();
    }

    public static void saveSkins()
    {
        for (Map.Entry<UUID, Skin> entry : skins.entrySet())
        {
            UUID uuid = entry.getKey();
            Skin skin = entry.getValue();

            Vowed.LOG.info("entry not null....");
            try
            {
                File file = new File("C:\\Users\\JPaul\\Desktop\\Server\\plugins\\VowedCore\\SkinManager\\" + uuid.toString() + ".skinData");
                file.createNewFile();
                FileWriter fileWriter = new FileWriter(file);
                PrintWriter writer = new PrintWriter(fileWriter);
                writer.println("Name: " + skin.getName());
                writer.println("Value: " + skin.getValue());
                writer.println("Signature: " + skin.getSignature());
                writer.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static Skin getSkinFromString(String name){
        OfflinePlayer player = Vowed.getPlugin().getServer().getOfflinePlayer(name);
        return new Skin(player.getUniqueId());
    }

    public static Skin getSkinFromUUID(UUID uuid)
    {
        return new Skin(uuid);
    }
}
