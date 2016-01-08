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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Door;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Created by JPaul on 12/28/2015.
 */
public class CompanyListener implements Listener
{

    private HashMap<Company, Hologram> column1 = new HashMap<>();
    private HashMap<Company, Hologram> column2 = new HashMap<>();
    private HashMap<Company, Hologram> column3 = new HashMap<>();
    private HashMap<Company, Hologram> bottomRow = new HashMap<>();
    HashMap<UUID, Map<Location, Material>> greenBlocks = new HashMap<>();
    HashMap<UUID, Map<Location, Byte>> greenData = new HashMap<>();
    HashMap<UUID, Map<Location, Material>> redBlocks = new HashMap<>();
    HashMap<UUID, Map<Location, Byte>> redData = new HashMap<>();
    List<UUID> handling = new ArrayList<>();
    HashMap<UUID, Location> center = new HashMap<>();
    HashMap<UUID, Boolean> handle = new HashMap<>();

    @EventHandler
    public void on(PlayerMoveEvent moveEvent)
    {

        Player player = moveEvent.getPlayer();
    }

    @EventHandler
    public void on(PlayerInteractEvent interactEvent)
    {
        Player player = interactEvent.getPlayer();
        Block clickedBlock = interactEvent.getClickedBlock();
        ProtectedRegion region = WorldGuardUtil.getRegion(player.getLocation());

        if (interactEvent.getAction() == Action.LEFT_CLICK_BLOCK && player.getItemInHand().getItemMeta().getLore() != null && player.getItemInHand().getItemMeta().getLore().contains("Selector"))
        {
            if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR && player.getItemInHand().getItemMeta().getLore() != null && player.getItemInHand().getItemMeta().getLore().contains("Selector"))
            {
                new BukkitRunnable()
                {
                    int counter = 0;
                    @Override
                    public void run()
                    {
                        if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR && player.getItemInHand().getItemMeta().getLore() != null && player.getItemInHand().getItemMeta().getLore().contains("Selector"))
                        {
                            handling.add(player.getUniqueId());
                            Location location = player.getTargetBlock((HashSet<Byte>) null, 5).getLocation();

                            if (isFlat(location, 6))
                            {
                                //removing redblocks if there are any because isFlat is true
                                if (redBlocks.get(player.getUniqueId()) != null && redData.get(player.getUniqueId()) != null && redBlocks.get(player.getUniqueId()).keySet().size() >= 1 && redData.get(player.getUniqueId()).keySet().size() >= 1)
                                {
                                    Iterator<Map.Entry<Location, Material>> blockIterator = redBlocks.get(player.getUniqueId()).entrySet().iterator();
                                    Iterator<Location> dataIterator = redData.get(player.getUniqueId()).keySet().iterator();

                                    while (blockIterator.hasNext())
                                    {
                                        Map.Entry<Location, Material> blockMap = blockIterator.next();
                                        Location blockLocation = blockMap.getKey();
                                        Location dataLocation = dataIterator.next();
                                        Block replaceBlock = blockLocation.getBlock();

                                        replaceBlock.setType(redBlocks.get(player.getUniqueId()).get(blockLocation));
                                        replaceBlock.setData(redData.get(player.getUniqueId()).get(dataLocation));

                                        blockIterator.remove();
                                        dataIterator.remove();
                                    }
                                }

                                //adding the greenblocks to the map

                                if (greenBlocks.get(player.getUniqueId()) != null && greenData.get(player.getUniqueId()) != null)
                                {
                                    if (greenBlocks.get(player.getUniqueId()).isEmpty())
                                    {
                                        if ((!greenBlocks.get(player.getUniqueId()).containsKey(location) && !greenData.get(player.getUniqueId()).containsKey(location)))
                                        {
                                            for (Location locations : getSquare(player, location, 6))
                                            {
                                                greenBlocks.get(player.getUniqueId()).put(locations, location.getBlock().getType());

                                                greenData.get(player.getUniqueId()).put(locations, location.getBlock().getData());
                                            }
                                        }
                                    }
                                } else
                                {
                                    Map<Location, Material> blockMaterial = new HashMap<>();
                                    Map<Location, Byte> blockData = new HashMap<>();
                                    for (Location locations : getSquare(player, location, 6))
                                    {
                                        if (locations.getBlock().getType() != Material.WOOL)
                                        {
                                            blockMaterial.put(locations, locations.getBlock().getType());
                                            greenBlocks.put(player.getUniqueId(), blockMaterial);

                                            blockData.put(locations, locations.getBlock().getData());
                                            greenData.put(player.getUniqueId(), blockData);
                                        }
                                    }
                                }


                                //setting the targetblock green wool to indicate isFlat = true

                                if (handle.get(player.getUniqueId()) != null)
                                {
                                    if (handle.get(player.getUniqueId()))
                                    {
                                            createSquare(location, 5, Material.WOOL, (byte) 5);
                                            Vowed.LOG.warning("CALLED");
                                    }
                                    else
                                    {
                                        Vowed.LOG.info("FALSE");
                                    }
                                }



                                //returning the block to its previous state if the map size > 1, meaning, dont remove the block the person is looking at (target block)

                                if (greenBlocks.get(player.getUniqueId()) != null && greenData.get(player.getUniqueId()) != null && !center.get(player.getUniqueId()).toString().equalsIgnoreCase(location.toString()))
                                {
                                    Iterator<Map.Entry<Location, Material>> blockIterator = greenBlocks.get(player.getUniqueId()).entrySet().iterator();
                                    Iterator<Location> dataIterator = greenData.get(player.getUniqueId()).keySet().iterator();

                                    handle.put(player.getUniqueId(), true);

                                    while (blockIterator.hasNext())
                                    {
                                        Map.Entry<Location, Material> blockMap = blockIterator.next();
                                        Location blockLocation = blockMap.getKey();

                                        Location dataLocation = dataIterator.next();
                                        Block replaceBlock = blockLocation.getBlock();

                                        replaceBlock.setType(greenBlocks.get(player.getUniqueId()).get(blockLocation));
                                        replaceBlock.setData(greenData.get(player.getUniqueId()).get(dataLocation));

                                        blockIterator.remove();
                                        dataIterator.remove();

                                        center.clear();

                                    }
                                    if (!blockIterator.hasNext())
                                    {
                                        handle.put(player.getUniqueId(), true);
                                        Vowed.LOG.severe("CALLED");
                                    }
                                    else
                                    {
                                        handle.put(player.getUniqueId(), false);
                                    }
                                }

                                /*
                                *
                                * DL
                                * Dqd
                                * qw;d
                                * qd
                                * q
                                * dww
                                * q
                                * DONT BOTHER WITH THIS SHIT TIM BROKE IT*/

                            } else
                            {
                                if (greenBlocks.get(player.getUniqueId()) != null && greenData.get(player.getUniqueId()) != null && greenBlocks.get(player.getUniqueId()).keySet().size() >= 1 && greenData.get(player.getUniqueId()).keySet().size() >= 1)
                                {
                                    Iterator<Map.Entry<Location, Material>> blockIterator = greenBlocks.get(player.getUniqueId()).entrySet().iterator();
                                    Iterator<Location> dataIterator = greenData.get(player.getUniqueId()).keySet().iterator();

                                    while (blockIterator.hasNext())
                                    {
                                        Map.Entry<Location, Material> blockMap = blockIterator.next();
                                        Location blockLocation = blockMap.getKey();
                                        Location dataLocation = dataIterator.next();
                                        Block replaceBlock = blockLocation.getBlock();

                                        replaceBlock.setType(greenBlocks.get(player.getUniqueId()).get(blockLocation));
                                        replaceBlock.setData(greenData.get(player.getUniqueId()).get(dataLocation));

                                        blockIterator.remove();
                                        dataIterator.remove();
                                    }
                                }

                                if (redBlocks.get(player.getUniqueId()) != null && redData.get(player.getUniqueId()) != null)
                                {
                                    if ((!redBlocks.get(player.getUniqueId()).containsKey(location) && !redData.get(player.getUniqueId()).containsKey(location)))
                                    {
                                        redBlocks.get(player.getUniqueId()).put(location, location.getBlock().getType());

                                        redData.get(player.getUniqueId()).put(location, location.getBlock().getData());
                                    }
                                } else
                                {
                                    Map<Location, Material> block = new HashMap<>();
                                    block.put(location, location.getBlock().getType());
                                    redBlocks.put(player.getUniqueId(), block);

                                    Map<Location, Byte> data = new HashMap<>();
                                    data.put(location, location.getBlock().getData());
                                    redData.put(player.getUniqueId(), data);
                                }

                                location.getBlock().setTypeIdAndData(Material.WOOL.getId(), (byte) 14, true);


                                if (redBlocks.get(player.getUniqueId()) != null && redData.get(player.getUniqueId()) != null && redBlocks.get(player.getUniqueId()).keySet().size() > 1)
                                {
                                    Iterator<Map.Entry<Location, Material>> blockIterator = redBlocks.get(player.getUniqueId()).entrySet().iterator();
                                    Iterator<Location> dataIterator = redData.get(player.getUniqueId()).keySet().iterator();

                                    while (blockIterator.hasNext())
                                    {
                                        Map.Entry<Location, Material> blockMap = blockIterator.next();
                                        Location blockLocation = blockMap.getKey();
                                        Location dataLocation = dataIterator.next();
                                        Block replaceBlock = blockLocation.getBlock();

                                        replaceBlock.setType(redBlocks.get(player.getUniqueId()).get(blockLocation));
                                        replaceBlock.setData(redData.get(player.getUniqueId()).get(dataLocation));

                                        blockIterator.remove();
                                        dataIterator.remove();
                                    }
                                }
                            }
                        } else
                        {
                            handling.remove(player.getUniqueId());

                            if (!handling.contains(player.getUniqueId()))
                            {
                                if (greenBlocks.get(player.getUniqueId()) != null && greenData.get(player.getUniqueId()) != null && greenBlocks.get(player.getUniqueId()).keySet().size() > 0 && greenData.get(player.getUniqueId()).keySet().size() > 0)
                                {
                                    Iterator<Map.Entry<Location, Material>> blockIterator = greenBlocks.get(player.getUniqueId()).entrySet().iterator();
                                    Iterator<Location> dataIterator = greenData.get(player.getUniqueId()).keySet().iterator();

                                    while (blockIterator.hasNext())
                                    {
                                        Map.Entry<Location, Material> blockMap = blockIterator.next();
                                        Location blockLocation = blockMap.getKey();
                                        Location dataLocation = dataIterator.next();
                                        Block replaceBlock = blockLocation.getBlock();

                                        replaceBlock.setType(greenBlocks.get(player.getUniqueId()).get(blockLocation));
                                        replaceBlock.setData(greenData.get(player.getUniqueId()).get(dataLocation));

                                        blockIterator.remove();
                                        dataIterator.remove();
                                    }
                                }
                                if (redBlocks.get(player.getUniqueId()) != null && redData.get(player.getUniqueId()) != null && redBlocks.get(player.getUniqueId()).keySet().size() > 0 && redData.get(player.getUniqueId()).keySet().size() > 0)
                                {
                                    Iterator<Map.Entry<Location, Material>> blockIterator = redBlocks.get(player.getUniqueId()).entrySet().iterator();
                                    Iterator<Location> dataIterator = redData.get(player.getUniqueId()).keySet().iterator();

                                    while (blockIterator.hasNext())
                                    {
                                        Map.Entry<Location, Material> blockMap = blockIterator.next();
                                        Location blockLocation = blockMap.getKey();
                                        Location dataLocation = dataIterator.next();
                                        Block replaceBlock = blockLocation.getBlock();

                                        replaceBlock.setType(redBlocks.get(player.getUniqueId()).get(blockLocation));
                                        replaceBlock.setData(redData.get(player.getUniqueId()).get(dataLocation));

                                        blockIterator.remove();
                                        dataIterator.remove();
                                    }
                                }
                            }

                            cancel();
                        }
                    }

                }.runTaskTimer(Vowed.getPlugin(), 1, 0);
            }
        }

        if (region != null && region.getId().startsWith("company"))
        {
            if (interactEvent.getAction() == Action.RIGHT_CLICK_BLOCK)
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
                                Hologram column2Hologram = HologramsAPI.createHologram(Vowed.getPlugin(), otherHalf.getLocation().add(otherHalf.getLocation().getDirection()).add(otherHalf.getLocation().getDirection()).add(0.5, 6, 0));
                                column2.put(company, column2Hologram);
                                column2.get(company).appendItemLine(new ItemStack(Material.WOOL, 1, (short) 14));
                                column2.get(company).appendItemLine(new ItemStack(Material.WOOL, 1, (short) 14));
                                column2.get(company).appendItemLine(new ItemStack(Material.WOOL, 1, (short) 14));

                                Hologram column1Hologram = HologramsAPI.createHologram(Vowed.getPlugin(), column2.get(company).getLocation().getBlock().getRelative(BlockFace.WEST).getLocation().add(0.5, 0, 0));
                                column1.put(company, column1Hologram);
                                column1.get(company).appendItemLine(new ItemStack(Material.WOOL, 1, (short) 14));
                                column1.get(company).appendItemLine(new ItemStack(Material.WOOL, 1, (short) 14));
                                column1.get(company).appendItemLine(new ItemStack(Material.WOOL, 1, (short) 14));

                                Hologram column3Hologram = HologramsAPI.createHologram(Vowed.getPlugin(), column2.get(company).getLocation().getBlock().getRelative(BlockFace.EAST).getLocation().add(0.5, 0, 0));
                                column3.put(company, column3Hologram);
                                column3.get(company).appendItemLine(new ItemStack(Material.WOOL, 1, (short) 14));
                                column3.get(company).appendItemLine(new ItemStack(Material.WOOL, 1, (short) 14));
                                column3.get(company).appendItemLine(new ItemStack(Material.WOOL, 1, (short) 14));

                                Hologram bottomRowHologram = HologramsAPI.createHologram(Vowed.getPlugin(), column2.get(company).getLocation().subtract(0, 2.5, 0));
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

    public List<Location> getSquare(Player player, Location center, int radius)
    {
        List<Location> locations = new ArrayList<>();

        for (int x = (int) center.getX() - radius; x <= (int) center.getX() + radius; x++)
        {
            for (int z = (int) center.getZ() - radius; z <= (int) center.getZ() + radius; z++)
            {
                Location cord = new Location(center.getWorld(), x, center.getY(), z);
                if (this.center.size() >= 1)
                {

                } else
                {
                    this.center.put(player.getUniqueId(), center);
                }
                locations.add(cord);
            }
        }

        return locations;
    }

    public void createSquare(Location center, int radius, Material material, byte data)
    {
        for (int x = (int) center.getX() - radius; x <= (int) center.getX() + radius; x++)
        {
            for (int z = (int) center.getZ() - radius; z <= (int) center.getZ() + radius; z++)
            {
                Location cord = new Location(center.getWorld(), x, center.getY(), z);
                cord.getBlock().setTypeIdAndData(material.getId(), data, true);
            }
        }
    }

    public boolean isFlat(Location center, int radius)
    {
        List<Boolean> booleans = new ArrayList<>();

        for (int x = (int) center.getX() - radius; x <= (int) center.getX() + radius; x++)
        {
            for (int z = (int) center.getZ() - radius; z <= (int) center.getZ() + radius; z++)
            {
                Location cord = new Location(center.getWorld(), x, center.getY(), z);
                Block block = cord.getBlock();
                Block blockBelow = block.getRelative(BlockFace.DOWN);
                Block blockAbove = block.getRelative(BlockFace.UP);

                if (blockBelow.getType() != Material.AIR && blockAbove.getType() == Material.AIR)
                {
                    booleans.add(true);
                } else
                {
                    booleans.add(false);
                }
            }
        }
        return !booleans.contains(false);
    }
}
