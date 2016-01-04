package me.vowed.api.test;

import me.vowed.api.plugin.Vowed;
import me.vowed.api.plugin.VowedPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by JPaul on 12/25/2015.
 */
public class TransactionUtil
{
    static VowedPlugin p;
    public TransactionUtil(VowedPlugin p){
        this.p = p;
    }

    public static List<String> sendNames(String uuid)
    {
        List<String> names = new ArrayList<>();

        try
        {
            URL name = new URL("https://api.mojang.com/user/profiles/" + uuid.replaceAll("-", "") + "/names");
            BufferedReader in = new BufferedReader(new InputStreamReader(name.openStream()));
            String string = in.readLine();
            in.close();
            JSONParser parser = new JSONParser();
            try
            {
                JSONArray array = (JSONArray) parser.parse(string);
                if (array != null)
                {
                    if (array.size() != 1)
                    {
                        for (int i = 0; i < array.size(); i++)
                        {
                            JSONObject obj = (JSONObject) array.get(i);
                            if (i == 0)
                            {
                                names.add("Original name: " + obj.get("name") + "\nChanges: \n");
                            } else if (i == array.size() - 1)
                            {
                                names.add(obj.get("name").toString());
                            } else
                            {
                                names.add(obj.get("name").toString() + ", ");
                            }
                        }
                    } else
                    {
                        names.add("This player has only had one name: " + ((JSONObject) array.get(0)).get("name"));
                    }
                } else
                {
                    Vowed.LOG.debug("BITCHES");
                }

            } catch (Exception e)
            {
                e.printStackTrace();
            }
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return names;
    }

    public static String getName(String UUID)
    {
        List<String> playerNames = new ArrayList<>();

        try
        {
            URL name = new URL("https://api.mojang.com/user/profiles/" + UUID.replaceAll("-", "") + "/names");
            BufferedReader in = new BufferedReader(new InputStreamReader(name.openStream()));
            String string = in.readLine();
            in.close();
            JSONParser parser = new JSONParser();
            try
            {
                JSONArray array = (JSONArray) parser.parse(string);
                if (array != null)
                {
                    if (array.size() != 1)
                    {
                        for (int i = 0; i < array.size(); i++)
                        {
                            JSONObject obj = (JSONObject) array.get(i);
                            if (i == array.size() - 1)
                            {
                                playerNames.add(obj.get("name").toString());
                            }
                        }
                    } else
                    {
                        playerNames.add(((JSONObject) array.get(0)).get("name").toString());
                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }


        for (String name : playerNames)
        {
            return name;
        }
        return null;
    }

    public static List<List<String>> getTransactions(UUID uuid) throws IOException
    {
        File file = new File(p.getDataFolder() + "Transactions\\" + uuid.toString());

        List<List<String>> files = new ArrayList<>();
        if (file.listFiles() != null)
        {
            for (File fileFinder : file.listFiles())
            {
                FileReader fileReader = new FileReader(fileFinder);
                BufferedReader reader = new BufferedReader(fileReader);

                String line;
                List<String> lines = new ArrayList<>();
                try
                {
                    while ((line = reader.readLine()) != null)
                    {
                        lines.add(line + "\n");
                    }
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                lines.add(lines.size() - 1, fileFinder.getName().substring(fileFinder.getName().indexOf("_") + 1, fileFinder.getName().lastIndexOf(".")));
                files.add(lines);
            }
        }
        return files;
    }
}
