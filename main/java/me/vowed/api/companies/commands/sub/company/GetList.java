package me.vowed.api.companies.commands.sub.company;

import me.vowed.api.companies.Company;
import me.vowed.api.companies.commands.sub.SubCommand;
import me.vowed.api.plugin.Vowed;
import net.citizensnpcs.npc.ai.speech.Chat;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by JPaul on 1/3/2016.
 */
public class GetList extends SubCommand
{
    HashMap<Integer, List<String[]>> infoPages = new HashMap<>();

    public GetList(String label)
    {
        super(label);
    }

    @Override
    public int getMinimumArguments()
    {
        return 0;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args)
    {

        List<String[]> infoPage = new ArrayList<>();

        for (Company company : Vowed.getCompanyManager().getCompanies())
        {
            ChatColor randomColour = getRandomColor();

            String[] info = {randomColour + "  " + fixFontSize(company.getName(), 10) +
                    fixFontSize(company.getType().name(), 12) +
                    fixFontSize(ChatColor.WHITE + "X: " + randomColour + company.getLocation().getBlockX() + ChatColor.WHITE + " Y: " + randomColour + company.getLocation().getBlockY() + ChatColor.WHITE + " Z: " + randomColour + company.getLocation().getBlockZ(), 17) +
                    fixFontSize(String.valueOf(company.isOpen()).toUpperCase(), 6)};
            int counter = 0;
            for (int i = 0; i < Vowed.getCompanyManager().getCompanies().size(); i++)
            {
                if (i % 5 == 0)
                {
                    counter++;
                }
            }

            infoPage.add(info);
            infoPages.put(counter, infoPage);
        }

        for (int i = 1; i <= infoPages.keySet().size(); i++)
        {
            commandSender.sendMessage(ChatColor.BOLD.toString() + ChatColor.GOLD + "           ----------- Company List ------- Page: " + i + "/" + infoPages.keySet().size());
            commandSender.sendMessage(ChatColor.WHITE + "  NAME            TYPE                LOCATION                      OPEN");

            for (String[] info : infoPages.get(i))
            {
                StringBuilder stringBuilder = new StringBuilder();

                for (String string : info)
                {
                    stringBuilder.append(string);
                }

                commandSender.sendMessage(stringBuilder.toString());
            }
        }

        return true;
    }

    public static String fixFontSize(String string, int size)
    {

        String upperCase = string.toUpperCase();


        for (int i = 0; i < string.length(); i++)
        {
            if (string.charAt(i) == 'I' || string.charAt(i) == ' ')
            {
                upperCase += " ";
            }
        }

        int spaces = size - string.length();
        spaces = (spaces * 2);

        for (int i = 0; i < spaces; i++)
        {
            upperCase += " ";
        }

        return upperCase;
    }

    public ChatColor getRandomColor(){
        ChatColor[] colors = ChatColor.values();
        int randomColor = new Random().nextInt(colors.length);
        if(!(colors[randomColor] == ChatColor.UNDERLINE || colors[randomColor] == ChatColor.ITALIC || colors[randomColor] == ChatColor.STRIKETHROUGH || colors[randomColor] == ChatColor.MAGIC || colors[randomColor] == ChatColor.BOLD || colors[randomColor] == ChatColor.BLACK))
        {
            return colors[randomColor];
        }

        return getRandomColor();
    }
}
