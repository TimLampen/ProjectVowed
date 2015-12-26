package me.vowed.api.enchants;

/**
 * Created by JPaul on 10/23/2015.
 */

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import me.vowed.api.plugin.Vowed;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HideEnchantsListener
{
    private final Server server;
    private final Logger logger;

    public HideEnchantsListener(Server server, Logger logger)
    {
        this.server = server;
        this.logger = logger;
    }

    public void addListener(ProtocolManager protocolManager, JavaPlugin plugin)
    {
        protocolManager.addPacketListener(new PacketAdapter(plugin, PacketType.Play.Server.SET_SLOT, PacketType.Play.Server.WINDOW_ITEMS,
                PacketType.Play.Server.NAMED_SOUND_EFFECT, PacketType.Play.Server.ENTITY_METADATA)
        {
            @Override
            public void onPacketSending(PacketEvent event)
            {
                PacketContainer packet = event.getPacket();

                try
                {
                    // Item packets
                    if (event.getPacketType() == PacketType.Play.Server.SET_SLOT)
                    {
                        removeEnchantments(packet.getItemModifier().read(0));
                    }
                    else if (event.getPacketType() == PacketType.Play.Server.WINDOW_ITEMS)
                    {
                        ItemStack[] elements = packet.getItemArrayModifier().read(0);

                        for (ItemStack element : elements)
                        {
                            if (element != null)
                            {
                                removeEnchantments(element);
                            }
                        }
                    }
                    else if (event.getPacketType() == PacketType.Play.Server.NAMED_SOUND_EFFECT)
                    {
                        event.setCancelled(true);
                    }
                    else if (event.getPacketType() == PacketType.Play.Server.ENTITY_METADATA)
                    {
                        if (packet.getEntityModifier(event).read(0) instanceof Item)
                        {
                            WrappedDataWatcher watcher = new WrappedDataWatcher(packet.getWatchableCollectionModifier().read(0));
                            ItemStack stack = watcher.getItemStack(10);

                            if (stack != null)
                            {
                                watcher.setObject(10, processItemStack(stack));

                                packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
                            }
                        }
                    }
                } catch (FieldAccessException e)
                {
                    logger.log(Level.SEVERE, "Couldn't access field.", e);
                }
            }
        });
    }

    public Server getServer()
    {
        return server;
    }

    public void removeListener(ProtocolManager protocolManager, JavaPlugin plugin)
    {
        protocolManager.removePacketListeners(plugin);
    }

    private void removeEnchantments(ItemStack stack)
    {
        if (stack == null)
            return;

        Set<Enchantment> enchantments = stack.getEnchantments().keySet();
        Enchantment[] copy = enchantments.toArray(new Enchantment[enchantments.size()]);
        for (Enchantment enchantment : copy)
        {
            if (!Objects.equals(enchantment.getName(), "Glow"))
            {
                stack.removeEnchantment(enchantment);
            }
        }
    }

    private ItemStack processItemStack(ItemStack stack)
    {
        // Otherwise you'll modify the stack on the server
        ItemStack cloned = stack.clone();
        for (Enchantment enchantment : cloned.getEnchantments().keySet())
        {
            if (!Objects.equals(enchantment.getName(), "Glow"))
            {
                cloned.removeEnchantment(enchantment);
            }
        }

        cloned.setType(stack.getType());
        return cloned;
    }
}
