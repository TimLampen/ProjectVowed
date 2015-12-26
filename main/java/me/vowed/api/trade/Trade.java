package me.vowed.api.trade;

import me.vowed.api.plugin.Vowed;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by JPaul on 10/9/2015.
 */
public class Trade implements Listener, CommandExecutor
{
    private Map<UUID, Inventory> sourceInventory = new HashMap<>();
    private Map<UUID, Inventory> targetInventory = new HashMap<>();
    private Map<String, Integer> targetGains = new HashMap<>();
    private Map<String, Integer> targetLosses = new HashMap<>();
    private boolean inOffer;
    private Player target;
    private Player source;
    private ItemStack divider = new ItemStack(Material.THIN_GLASS);
    private int gainSlot = 4;
    private int lossSlot = -1;
    int timesClickedTarget = 0;
    int timesClickedSource = 0;
    private Inventory tradeOffer;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("create"))
        {
            if (args[0] != null)
            {
                Player target = Bukkit.getPlayerExact(args[0]);

                targetInventory.put(target.getUniqueId(), target.getInventory());
                sourceInventory.put(player.getUniqueId(), player.getInventory());

                Inventory createTrade = Bukkit.createInventory(player, 54, ChatColor.YELLOW + "Yours                Target's");
                createTrade.setItem(4, this.divider);
                createTrade.setItem(13, this.divider);
                createTrade.setItem(22, this.divider);
                createTrade.setItem(31, this.divider);
                createTrade.setItem(40, this.divider);
                createTrade.setItem(49, this.divider);

                for (ItemStack itemStack : this.targetInventory.get(target.getUniqueId()))
                {
                    if (itemStack != null)
                    {
                        createInventory(gainSlot, itemStack);
                    }
                }
                for (ItemStack itemStack : this.sourceInventory.get(player.getUniqueId()))
                {
                    if (itemStack != null)
                    {
                        createInventory(lossSlot, itemStack);
                    }
                }

                player.openInventory(createTrade);
            }
        }

        gainSlot = 4;
        lossSlot = -1;

        return false;
    }

    @EventHandler
    public void on(InventoryCloseEvent closeEvent) throws IOException
    {
        this.timesClickedTarget = 0;
        this.timesClickedSource = 0;

        if (closeEvent.getInventory().getName().toLowerCase().startsWith(ChatColor.YELLOW + "yours"))
        {
            this.source = (Player) closeEvent.getPlayer();

            this.inOffer = true;

            this.source.sendRawMessage("Who do you want to send this Trade Offer to?");

            this.tradeOffer = Bukkit.createInventory(this.source, 54, ChatColor.YELLOW + "Gains                Losses");

            this.tradeOffer.setItem(4, this.divider);
            this.tradeOffer.setItem(13, this.divider);
            this.tradeOffer.setItem(22, this.divider);
            this.tradeOffer.setItem(31, this.divider);
            this.tradeOffer.setItem(40, this.divider);
            this.tradeOffer.setItem(49, this.divider);

            if (this.targetLosses != null && this.targetGains != null)
            {
                for (String itemStackFinder : this.targetLosses.keySet())
                {
                    if (itemStackFromBase64(itemStackFinder) != null)
                    {
                        ItemStack itemStack = itemStackFromBase64(itemStackFinder);
                        itemStack.setAmount(this.targetLosses.get(itemStackFinder));

                        createInventory(gainSlot, itemStack);
                    }
                }


                for (String itemStackFinder : this.targetGains.keySet())
                {
                    Vowed.LOG.info(itemStackFinder);
                    if (itemStackFromBase64(itemStackFinder) != null)
                    {

                        ItemStack itemStack = itemStackFromBase64(itemStackFinder);
                        itemStack.setAmount(this.targetGains.get(itemStackFinder));

                        createInventory(lossSlot, itemStack);
                    }
                }
            }
        } else if (closeEvent.getInventory().getName().toLowerCase().startsWith("gains"))
        {
            //TO:DO remove from inOffer
        }
    }


    @EventHandler
    public void on(InventoryClickEvent clickEvent)
    {
        String data = itemStackToBase64(clickEvent.getCurrentItem());

        switch (clickEvent.getSlot())
        {
            case 4:
            case 13:
            case 22:
            case 31:
            case 40:
            case 49:
                clickEvent.setCancelled(true);
                break;
        }


        if (clickEvent.getInventory().getName().toLowerCase().startsWith(ChatColor.YELLOW + "yours"))
        {
            clickEvent.setCancelled(true);

            if (isTargetsInventory(clickEvent))
            {
                if (clickEvent.isLeftClick())
                {
                    this.targetLosses.put(data, clickEvent.getCurrentItem().getAmount());

                } else if (clickEvent.isRightClick())
                {
                    this.timesClickedTarget++;

                    if (this.timesClickedTarget <= clickEvent.getCurrentItem().getAmount())
                    {
                        this.targetLosses.put(data, timesClickedTarget);
                    }
                }
            } else if (isSourcesInventory(clickEvent))
            {
                if (clickEvent.isLeftClick())
                {
                    this.targetGains.put(data, clickEvent.getCurrentItem().getAmount());

                } else if (clickEvent.isRightClick())
                {
                    this.timesClickedSource++;

                    if (this.timesClickedTarget <= clickEvent.getCurrentItem().getAmount())
                    {
                        this.targetGains.put(data, this.timesClickedSource);
                    }
                }
            }


        } else if (clickEvent.getInventory().getName().toLowerCase().startsWith(ChatColor.YELLOW + "gains"))
        {
            if (clickEvent.getCurrentItem().getType() == Material.APPLE)
            {
                clickEvent.setCancelled(true);

                this.target.closeInventory();

                for (String itemStackFinder : this.targetGains.keySet())
                {
                    this.source.getInventory().remove(itemStackFromBase64(itemStackFinder));
                    this.target.getInventory().addItem(itemStackFromBase64(itemStackFinder));
                }


                for (String itemStackFinder : this.targetLosses.keySet())
                {
                    this.target.getInventory().remove(itemStackFromBase64(itemStackFinder));
                    this.source.getInventory().addItem(itemStackFromBase64(itemStackFinder));
                }

                this.targetLosses.clear();
                this.targetGains.clear();


            } else
            {
                clickEvent.setCancelled(true);
            }
        } else
        {
            clickEvent.setCancelled(true);
        }
        gainSlot = 4;
        lossSlot = -1;

    }


    @EventHandler
    public void on(PlayerChatEvent chatEvent)
    {
        if (this.inOffer)
        {

            if (chatEvent.getMessage().equalsIgnoreCase("cancel"))
            {
                this.inOffer = false;
                return;
            }
            this.target = Bukkit.getPlayerExact(chatEvent.getMessage());

            if (this.target != null)
            {
                for (ItemStack itemStack : this.tradeOffer.getContents())
                {
                    if (itemStack != null && itemStack.getType() != Material.AIR && itemStack.getType() != Material.THIN_GLASS)
                    {
                        this.tradeOffer.setItem(53, new ItemStack(Material.APPLE));
                        this.target.openInventory(this.tradeOffer);
                    }
                }
            } else
            {
                chatEvent.getPlayer().sendRawMessage("Please enter a valid " + ChatColor.BLUE + ChatColor.UNDERLINE + "username" +
                        ChatColor.RESET + ", or type " + "'" + ChatColor.BLUE + ChatColor.UNDERLINE + "cancel" + ChatColor.RESET + "'");
            }

        }
    }

    public void createInventory(int slot, ItemStack itemStack)
    {
        if (slot == gainSlot)
        {
            gainSlot++;
            if ((gainSlot > 8) && (gainSlot < 13))
            {
                gainSlot = 14;
            } else if ((gainSlot > 17) && (gainSlot < 22))
            {
                gainSlot = 23;
            } else if ((gainSlot > 26) && (gainSlot < 31))
            {
                gainSlot = 32;
            } else if ((gainSlot > 35) && (gainSlot < 40))
            {
                gainSlot = 41;
            } else if ((gainSlot > 44) && (gainSlot < 49))
            {
                gainSlot = 50;
            }
            this.tradeOffer.setItem(gainSlot, itemStack);
        }
        else if (slot == lossSlot)
        {
            lossSlot++;
            if ((lossSlot > 3) && (lossSlot < 9))
            {
                lossSlot = 9;
            } else if ((lossSlot > 12) && (lossSlot < 18))
            {
                lossSlot = 18;
            } else if ((lossSlot > 21) && (lossSlot < 27))
            {
                lossSlot = 27;
            } else if ((lossSlot > 30) && (lossSlot < 36))
            {
                lossSlot = 36;
            } else if ((lossSlot > 39) && (lossSlot < 45))
            {
                lossSlot = 45;
            }
            this.tradeOffer.setItem(lossSlot, itemStack);
        }
    }

    public boolean isTargetsInventory(InventoryClickEvent clickEvent)
    {
        int slot = clickEvent.getSlot();

        if ((slot > 4) && (slot < 9))
        {
            return true;
        }
        if ((slot > 13) && (slot < 18))
        {
            return true;
        }
        if ((slot > 22) && (slot < 27))
        {
            return true;
        }
        if ((slot > 31) && (slot < 36))
        {
            return true;
        }
        if ((slot > 40) && (slot < 45))
        {
            return true;
        }

        return false;
    }

    public boolean isSourcesInventory(InventoryClickEvent clickEvent)
    {
        int slot = clickEvent.getSlot();

        if ((slot > -1) && (slot < 4))
        {
            return true;
        }
        if ((slot > 8) && (slot < 13))
        {
            return true;
        }
        if ((slot > 17) && (slot < 22))
        {
            return true;
        }
        if ((slot > 26) && (slot < 31))
        {
            return true;
        }
        if ((slot > 35) && (slot < 40))
        {
            return true;
        }
        return false;
    }

    public static String itemStackToBase64(ItemStack items) throws IllegalStateException
    {
        try
        {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeObject(items);

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e)
        {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

/*
     * A method to serialize an inventory to Base64 string.

     * Special thanks to Comphenix in the Bukkit forums or also known
     * as aadnk on GitHub and GreyWolf336 on GitHub. Modified it a little bit myself.
*/

    public static ItemStack itemStackFromBase64(String data)
    {
        try
        {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack items = (ItemStack) dataInput.readObject();

            dataInput.close();
            return items;
        } catch (ClassNotFoundException e)
        {
            try
            {
                throw new IOException("Unable to decode class type.", e);
            } catch (IOException e1)
            {
                e1.printStackTrace();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
