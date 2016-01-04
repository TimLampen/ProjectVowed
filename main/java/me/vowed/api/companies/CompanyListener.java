package me.vowed.api.companies;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.vowed.api.plugin.Vowed;
import me.vowed.api.utils.worldguard.WorldGuardUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Door;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by JPaul on 12/28/2015.
 */
public class CompanyListener implements Listener
{
    private Hologram column1Hologram;
    private Hologram column2Hologram;
    private Hologram column3Hologram;
    private Hologram bottomRowHologram;

    private HashMap<Company, Hologram> column1 = new HashMap<>();
    private HashMap<Company, Hologram> column2 = new HashMap<>();
    private HashMap<Company, Hologram> column3 = new HashMap<>();
    private HashMap<Company, Hologram> bottomRow = new HashMap<>();



    @EventHandler
    public void on(PlayerInteractEvent interactEvent)
    {
        Player player = interactEvent.getPlayer();
        Block clickedBlock = interactEvent.getClickedBlock();
        ProtectedRegion region = WorldGuardUtil.getRegion(player.getLocation());

        if (region != null && region.getId().startsWith("company"))
        {
            if (isDoor(interactEvent) && WorldGuardUtil.isOwner(player, region))
            {

                Door door = new Door(0, clickedBlock.getData());
                Block otherHalf;

                if (door.isTopHalf())
                {
                    otherHalf = clickedBlock.getRelative(BlockFace.DOWN);
                } else
                {
                    otherHalf = clickedBlock;
                }

                Door rightDoor = new Door(0, otherHalf.getData());

                int[] centerXZ = WorldGuardUtil.getCenter(region);
                Location center = new Location(player.getWorld(), centerXZ[0], region.getMinimumPoint().getBlockY(), centerXZ[1]);

                Company company = Vowed.getCompanyManager().getCompany(center);

                List<Hologram> holograms;

                if (column2.get(company) == null)
                {
                    switch (company.getType())
                    {
                        case ARMOURY:
                            column2Hologram = HologramsAPI.createHologram(Vowed.getPlugin(), otherHalf.getLocation().add(otherHalf.getLocation().getDirection()).add(otherHalf.getLocation().getDirection()).add(0.5, 6, 0));
                            column2.put(company, column2Hologram);
                            column2.get(company).appendItemLine(new ItemStack(Material.WOOL, 1, (short) 14));
                            column2.get(company).appendItemLine(new ItemStack(Material.WOOL, 1, (short) 14));
                            column2.get(company).appendItemLine(new ItemStack(Material.WOOL, 1, (short) 14));

                            column1Hologram = HologramsAPI.createHologram(Vowed.getPlugin(), column2.get(company).getLocation().getBlock().getRelative(BlockFace.WEST).getLocation().add(0.5, 0, 0));
                            column1.put(company, column1Hologram);
                            column1.get(company).appendItemLine(new ItemStack(Material.WOOL, 1, (short) 14));
                            column1.get(company).appendItemLine(new ItemStack(Material.WOOL, 1, (short) 14));
                            column1.get(company).appendItemLine(new ItemStack(Material.WOOL, 1, (short) 14));

                            column3Hologram = HologramsAPI.createHologram(Vowed.getPlugin(), column2.get(company).getLocation().getBlock().getRelative(BlockFace.EAST).getLocation().add(0.5, 0, 0));
                            column3.put(company, column3Hologram);
                            column3.get(company).appendItemLine(new ItemStack(Material.WOOL, 1, (short) 14));
                            column3.get(company).appendItemLine(new ItemStack(Material.WOOL, 1, (short) 14));
                            column3.get(company).appendItemLine(new ItemStack(Material.WOOL, 1, (short) 14));

                            bottomRowHologram = HologramsAPI.createHologram(Vowed.getPlugin(), column2.get(company).getLocation().subtract(0, 2.5, 0));
                            bottomRow.put(company, bottomRowHologram);
                            bottomRow.get(company).appendTextLine(ChatColor.RED.toString() + ChatColor.BOLD + "CLOSED");

                            holograms = new ArrayList<>();
                            holograms.add(column2.get(company));
                            holograms.add(column1.get(company));
                            holograms.add(column3.get(company));
                            holograms.add(bottomRow.get(company));

                            company.setHolograms(holograms);

                            break;

                        case FOOD:
                            Location loc1 = new Location(player.getWorld(), region.getMaximumPoint().getBlockX(), region.getMaximumPoint().getBlockY(), region.getMaximumPoint().getBlockZ());
                            Location loc2 = new Location(player.getWorld(), region.getMinimumPoint().getBlockX(), region.getMinimumPoint().getBlockY(), region.getMinimumPoint().getBlockZ());
                            int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
                            int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
                            int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
                            int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
                            int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
                            int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

                            for (int x = minX; x <= maxX; x++)
                            {
                                for (int y = minY; y <= maxY; y++)
                                {
                                    for (int z = minZ; z <= maxZ; z++)
                                    {
                                        Block block = player.getWorld().getBlockAt(x, y, z);
                                        if (block.getType() == Material.THIN_GLASS)
                                        {
                                            Vowed.LOG.debug("TRIFAEWF");
                                            column2Hologram = HologramsAPI.createHologram(Vowed.getPlugin(), block.getLocation().add(0, .85, 0.5));
                                            column2.put(company, column2Hologram);
                                            column2.get(company).appendItemLine(new ItemStack(Material.WOOL, 1, (short) 14));
                                            column2.get(company).appendItemLine(new ItemStack(Material.WOOL, 1, (short) 14));

                                            column1Hologram = HologramsAPI.createHologram(Vowed.getPlugin(), column2.get(company).getLocation().getBlock().getRelative(BlockFace.SOUTH).getLocation().add(0, 0.85, 0.5));
                                            column1.put(company, column1Hologram);
                                            column1.get(company).appendItemLine(new ItemStack(Material.WOOL, 1, (short) 14));
                                            column1.get(company).appendItemLine(new ItemStack(Material.WOOL, 1, (short) 14));

                                            column3Hologram = HologramsAPI.createHologram(Vowed.getPlugin(), column2.get(company).getLocation().getBlock().getRelative(BlockFace.NORTH).getLocation().add(0, 0.85, 0.5));
                                            column3.put(company, column3Hologram);
                                            column3.get(company).appendItemLine(new ItemStack(Material.WOOL, 1, (short) 14));
                                            column3.get(company).appendItemLine(new ItemStack(Material.WOOL, 1, (short) 14));

                                            bottomRowHologram = HologramsAPI.createHologram(Vowed.getPlugin(), column2.get(company).getLocation().subtract(0, 1.5, 0));
                                            bottomRow.put(company, bottomRowHologram);
                                            bottomRow.get(company).appendTextLine(ChatColor.RED.toString() + ChatColor.BOLD + "CLOSED");

                                            holograms = new ArrayList<>();
                                            holograms.add(column2.get(company));
                                            holograms.add(column1.get(company));
                                            holograms.add(column3.get(company));
                                            holograms.add(bottomRow.get(company));

                                            company.setHolograms(holograms);
                                        }
                                    }
                                }
                            }
                            break;
                    }
                } else
                {
                    if (!rightDoor.isOpen())
                    {
                        for (int i = 0; i < column1.get(company).size(); i++)
                        {
                            column1.get(company).removeLine(i);
                            column1.get(company).insertItemLine(i, new ItemStack(Material.WOOL, 1, (short) 5));
                        }

                        for (int i = 0; i < column2.get(company).size(); i++)
                        {
                            column2.get(company).removeLine(i);
                            column2.get(company).insertItemLine(i, new ItemStack(Material.WOOL, 1, (short) 5));
                        }

                        for (int i = 0; i < column3.get(company).size(); i++)
                        {
                            column3.get(company).removeLine(i);
                            column3.get(company).insertItemLine(i, new ItemStack(Material.WOOL, 1, (short) 5));
                        }

                        bottomRow.get(company).clearLines();
                        bottomRow.get(company).appendTextLine(ChatColor.GREEN.toString() + ChatColor.BOLD + "OPEN");

                        company.setOpen(true);
                    } else
                    {
                        for (int i = 0; i < column1.get(company).size(); i++)
                        {
                            column1.get(company).removeLine(i);
                            column1.get(company).insertItemLine(i, new ItemStack(Material.WOOL, 1, (short) 14));
                        }

                        for (int i = 0; i < column2.get(company).size(); i++)
                        {
                            column2.get(company).removeLine(i);
                            column2.get(company).insertItemLine(i, new ItemStack(Material.WOOL, 1, (short) 14));
                        }

                        for (int i = 0; i < column3.get(company).size(); i++)
                        {
                            column3.get(company).removeLine(i);
                            column3.get(company).insertItemLine(i, new ItemStack(Material.WOOL, 1, (short) 14));
                        }

                        bottomRow.get(company).clearLines();
                        bottomRow.get(company).appendTextLine(ChatColor.RED.toString() + ChatColor.BOLD + "CLOSED");

                        company.setOpen(false);
                    }
                }

                Vowed.LOG.debug(door.isOpen());
                Vowed.LOG.debug(rightDoor.isOpen());
            } else
            {
                interactEvent.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void on(BlockPlaceEvent placeEvent)
    {
        if (placeEvent.getBlockPlaced().getType() == Material.CHEST || placeEvent.getBlockPlaced().getType() == Material.COBBLESTONE)
        {
        }
    }

    public boolean isDoor(PlayerInteractEvent interactEvent)
    {
        if (interactEvent.getClickedBlock() != null &&
                interactEvent.getClickedBlock().getType() != Material.AIR &&
                interactEvent.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            if (interactEvent.getClickedBlock().getType() == Material.ACACIA_DOOR ||
                    interactEvent.getClickedBlock().getType() == Material.JUNGLE_DOOR ||
                    interactEvent.getClickedBlock().getType() == Material.IRON_DOOR ||
                    interactEvent.getClickedBlock().getType() == Material.DARK_OAK_DOOR ||
                    interactEvent.getClickedBlock().getType() == Material.BIRCH_DOOR ||
                    interactEvent.getClickedBlock().getType() == Material.SPRUCE_DOOR ||
                    interactEvent.getClickedBlock().getType() == Material.WOODEN_DOOR)
            {
                return true;
            }
        }

        return false;
    }
}
