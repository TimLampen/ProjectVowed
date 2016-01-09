package me.vowed.api.items;

import me.vowed.api.companies.commands.sub.SubCommand;
import me.vowed.api.plugin.Vowed;
import me.vowed.api.tier.ArmourTier;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Primary on 1/8/2016.
 */
public class ItemGiveCommand extends SubCommand{
    public ItemGiveCommand(String label){
        super(label);
    }

    @Override
    public int getMinimumArguments(){
        return 3;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args){
        if(args[0].equalsIgnoreCase("give")){
            if(Bukkit.getPlayer(args[1])!=null){
                Player target = Bukkit.getPlayer(args[1]);
                Armour a = new Armour(new ItemStack(Material.LEATHER_CHESTPLATE, 1), Color.BLACK, "Hope you die", ArmourTier.TIER5);
                a.make("chestplate");
                target.getInventory().addItem(a.getArmour());
            }
            else{
                sender.sendMessage(Vowed.getPrefix() + ChatColor.RED + "Cannot find the target player");
            }
        }

        return false;
    }
}
