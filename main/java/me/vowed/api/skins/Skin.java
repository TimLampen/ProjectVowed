package me.vowed.api.skins;

import me.vowed.api.plugin.Vowed;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.UUID;


/**
 * Created by JPaul on 11/18/2015.
 */
public class Skin
{
    String uuid;
    UUID asUUID;
    String name;
    String value;
    String signatur;
    String playername;
    Integer responseCode;

    public Skin(UUID uuid)
    {
        this.uuid = uuid.toString().replace("-", "");
        this.asUUID = uuid;
        load();
    }

    public Skin(String name, String value, String signature)
    {
        this.name = name;
        this.value = value;
        this.signatur = signature;
    }


    private void load()
    {
        try
        {
            // Get the name from Mojang

            OfflinePlayer offP = Bukkit.getServer().getOfflinePlayer(asUUID);
            playername = offP.getName();

            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            HttpURLConnection connection = (HttpURLConnection) setupConnection(url);

            this.responseCode = connection.getResponseCode();

            if (responseCode == 429)
            {
                Vowed.LOG.warning("No proxy available to handle skin changing");
                return;
            }
            // Parse it
            @SuppressWarnings("resource")
            String json = IOUtils.toString(connection.getInputStream(), StandardCharsets.UTF_8);
            IOUtils.closeQuietly(connection.getInputStream());
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);
            String username = (String) jsonObject.get("name");
            JSONArray properties = (JSONArray) (jsonObject).get("properties");
            for (int i = 0; i < properties.size(); i++)
            {
                try
                {
                    JSONObject property = (JSONObject) properties.get(i);
                    String name = (String) property.get("name");
                    String value = (String) property.get("value");
                    String signature = property.containsKey("signature") ? (String) property.get("signature") : null;


                    this.name = name;
                    this.value = value;
                    this.signatur = signature;


                } catch (Exception e)
                {
                    Vowed.LOG.warning("Failed to apply auth property " + e);
                }
            }
            connection.setConnectTimeout(0);
            connection.getInputStream().close();
        } catch (ParseException | IOException e)
        {
            e.printStackTrace();
        }
    }

    public String getValue()
    {
        return value;
    }

    public String getName()
    {
        return name;
    }

    public String getSignature()
    {
        return signatur;
    }

    public String getUUID()
    {
        return uuid;
    }

    public UUID getAsUUID()
    {
        return asUUID;
    }

    public String getPlayerName()
    {
        return playername;
    }

    public Integer getResponseCode()
    {
        return responseCode;
    }

    private static URLConnection setupConnection(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        return connection;
    }


}
