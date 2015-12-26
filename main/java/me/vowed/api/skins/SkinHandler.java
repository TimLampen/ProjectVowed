package me.vowed.api.skins;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JPaul on 11/20/2015.
 */
public class SkinHandler implements Listener
{
    @EventHandler
    public void onRaceLogin(PlayerJoinEvent joinEvent) throws SQLException, FileNotFoundException
    {
        Player player = joinEvent.getPlayer();

        File file = new File("C:\\Users\\JPaul\\Desktop\\Server\\plugins\\VowedCore\\SkinManager\\" + player.getUniqueId().toString() + ".skinData");
        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);

        String line;
        List<String> cool = new ArrayList<>();
        try
        {
            while ((line = reader.readLine()) != null)
            {

                if (line.startsWith("Name"))
                {
                    String name = line.substring(6);
                    cool.add(name);
                } else if (line.startsWith("Value"))
                {
                    String value = line.substring(7);
                    cool.add(value);
                } else if (line.startsWith("Signature"))
                {
                    String signature = line.substring(11);
                    cool.add(signature);
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        Skin skin = new Skin(cool.get(0), cool.get(1), cool.get(2));

        SkinUtil.setSkin(player, skin);
    }
}
