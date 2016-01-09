package me.vowed.api.companies.commands.sub.company;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import me.vowed.api.companies.Company;
import me.vowed.api.companies.CompanyListener;
import me.vowed.api.companies.commands.sub.SubCommand;
import me.vowed.api.companies.types.CompanyType;
import me.vowed.api.plugin.Vowed;
import me.vowed.api.utils.bukkit.BukkitUtil;
import me.vowed.api.utils.worldedit.WorldEditUtil;
import me.vowed.api.utils.worldguard.WorldGuardUtil;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by JPaul on 1/2/2016.
 */
public class CreateCommand extends SubCommand
{
    public static String name;
    public static CompanyType type;

    public CreateCommand(String label)
    {
        super(label);
    }

    @Override
    public int getMinimumArguments()
    {
        return 2;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args)
    {
        WorldEditPlugin worldEdit = WorldEditUtil.getWorldEdit();
        WorldGuardPlugin worldGuard = WorldGuardUtil.getWorldGuard();
        assert worldGuard != null;
        assert worldEdit != null;

        name = args[2];
        type = CompanyType.valueOf(args[3].toUpperCase());

        ItemStack selector = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = selector.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD.toString() + ChatColor.BOLD + "Selector");
        selector.setItemMeta(meta);
        ((Player) commandSender).setItemInHand(selector);
        ((Player) commandSender).updateInventory();

        return true;
    }
}
