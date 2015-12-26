package me.vowed.api.shops;

import me.vowed.api.player.PlayerWrapper;
import me.vowed.api.utils.Logger;
import net.minecraft.server.v1_8_R3.Item;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by JPaul on 10/15/2015.
 */
public interface IShop
{
    IShop getShop();

    Inventory getOwnerInventory();

    Inventory getElfInventory();

    Inventory getDwarfInventory();

    Inventory getHumanInventory();

    Inventory getInventory(PlayerWrapper player);

    String getName();

    List<ShopItem> getContents();

    List<ShopItem> getSavedContents();

    Location getLocation();

    Player getOwner();

    UUID getOwnerUUID();

    ShopType getType();

    boolean isOpen();

    void setName(String name);

    void setContents(List<ShopItem> contents);

    void addItemWithPrice(ShopItem item);

    void addItem(ShopItem item);

    void addItem(ShopItem item, int slot);

    void removeItemWithPrice(ShopItem item);

    void removeItem(ShopItem item);

    void setLocation(Location location);

    void setOwner(UUID newOwner);

    void setType(ShopType shopType);

    void setOpen(Boolean isOpen);

    void showName();

    void changeName(String name);

    void createShop();

    void saveContents();
}
