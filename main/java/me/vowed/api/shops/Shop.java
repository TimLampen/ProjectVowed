package me.vowed.api.shops;

import com.comphenix.protocol.wrappers.collection.ConvertedMultimap;
import me.vowed.api.player.PlayerWrapper;
import me.vowed.api.plugin.Vowed;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftChest;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryDoubleChest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by JPaul on 10/15/2015.
 */
public class Shop implements IShop
{
    private String name;
    private List<ShopItem> contents;
    private Location location;
    private Location armourStandLocation;
    private ShopType shopType;
    private boolean isOpen;
    private UUID owner;
    private Inventory elfInventory;
    private Inventory dwarfInventory;
    private Inventory humanInventory;
    private Inventory ownerInventory;
    private ArmorStand armourStand;

    public static HashMap<Player, IShop> iShopLocaterPlayer = new HashMap<>();
    public static HashMap<Location, IShop> iShopLocaterLocation = new HashMap<>();
    private HashMap<IShop, List<ShopItem>> shopInventory = new HashMap<>();
    HashMap<UUID, IShop> shop = new HashMap<>();


    public Shop(Player player, String name, Location location, ShopType shopType)
    {
        this.owner = player.getUniqueId();
        this.name = name;
        this.contents = new ArrayList<>();
        this.location = location;
        this.shop.put(player.getUniqueId(), this);
        this.shopType = shopType;
        this.elfInventory = Bukkit.createInventory(player, 18, ChatColor.YELLOW.toString() + ChatColor.BOLD + player.getName() + "'s Shop - Elf");
        this.dwarfInventory = Bukkit.createInventory(player, 18, ChatColor.YELLOW.toString() + ChatColor.BOLD + player.getName() + "'s Shop - Dwarf");
        this.humanInventory = Bukkit.createInventory(player, 18, ChatColor.YELLOW.toString() + ChatColor.BOLD + player.getName() + "'s Shop - Human");
        this.ownerInventory = Bukkit.createInventory(player, 18, ChatColor.YELLOW.toString() + ChatColor.BOLD + player.getName() + "'s Shop - Owner");
        UniqueItem uniqueItem = new UniqueItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14), UUID.randomUUID().toString().toUpperCase().substring(0, 6));
        ShopItem shopItem = new ShopItem(uniqueItem);
        ShopItemManager.addShopItems(this, shopItem);
        ShopListener.uniqueItems.put(shopItem.getShopItem(), uniqueItem);
        this.addItem(shopItem, 8);
    }

    @Override
    public IShop getShop()
    {
        return this.shop.get(owner);
    }

    @Override
    public Inventory getOwnerInventory()
    {
        return this.ownerInventory;
    }

    @Override
    public Inventory getElfInventory()
    {
        return this.elfInventory;
    }

    @Override
    public Inventory getDwarfInventory()
    {
        return this.dwarfInventory;
    }

    @Override
    public Inventory getHumanInventory()
    {
        return this.humanInventory;
    }

    @Override
    public Inventory getInventory(PlayerWrapper player)
    {
        if (player.getPlayer().getUniqueId().equals(getOwnerUUID()))
        {
            return getOwnerInventory();
        }
        else
        {
            switch (player.getRace().getType())
            {
                case ELF:
                    return getElfInventory();

                case DWARF:
                    return getDwarfInventory();

                case HUMAN:
                    return getHumanInventory();
            }
        }

        return null;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public List<ShopItem> getContents()
    {
        return this.contents;
    }

    @Override
    public List<ShopItem> getSavedContents()
    {
        return this.shopInventory.get(this);
    }

    @Override
    public Location getLocation()
    {
        return this.location;
    }

    @Override
    public UUID getOwnerUUID()
    {
        return this.owner;
    }

    @Override
    public ShopType getType()
    {
        return this.shopType;
    }

    @Override
    public boolean isOpen()
    {
        return this.isOpen;
    }

    @Override
    public Player getOwner()
    {
        return Bukkit.getPlayer(this.owner);
    }

    @Override
    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public void setContents(List<ShopItem> contents)
    {
        this.contents = contents;
    }

    @Override
    public void addItemWithPrice(ShopItem item)
    {
        this.contents.add(item);

        this.ownerInventory.addItem(item.getShopItem());
        this.elfInventory.addItem(item.getElfInstance(item.getShopItem()).getItemStack());
        this.dwarfInventory.addItem(item.getDwarfInstance(item.getShopItem()).getItemStack());
        this.humanInventory.addItem(item.getHumanInstance(item.getShopItem()).getItemStack());

        ShopListener.uniqueItems.put(item.getElfInstance(item.getShopItem()).getItemStack(), item.getElfInstance(item.getShopItem()));
        ShopListener.uniqueItems.put(item.getDwarfInstance(item.getShopItem()).getItemStack(), item.getDwarfInstance(item.getShopItem()));
        ShopListener.uniqueItems.put(item.getHumanInstance(item.getShopItem()).getItemStack(), item.getHumanInstance(item.getShopItem()));
    }

    @Override
    public void addItem(ShopItem item)
    {
        this.contents.add(item);

        this.ownerInventory.addItem(item.getShopItem());
        this.elfInventory.addItem(item.getShopItem());
        this.dwarfInventory.addItem(item.getShopItem());
        this.humanInventory.addItem(item.getShopItem());
    }

    @Override
    public void addItem(ShopItem item, int slot)
    {
        this.contents.add(item);

        this.ownerInventory.setItem(slot, item.getShopItem());
        this.elfInventory.setItem(slot, item.getShopItem());
        this.dwarfInventory.setItem(slot, item.getShopItem());
        this.humanInventory.setItem(slot, item.getShopItem());
    }

    @Override
    public void removeItemWithPrice(ShopItem item)
    {
        this.contents.remove(item);

        this.ownerInventory.remove(item.getShopItem());
        this.elfInventory.remove(item.getElfInstance(item.getShopItem()).getItemStack());
        this.dwarfInventory.remove(item.getDwarfInstance(item.getShopItem()).getItemStack());
        this.humanInventory.remove(item.getHumanInstance(item.getShopItem()).getItemStack());
    }

    @Override
    public void removeItem(ShopItem item)
    {
        this.contents.remove(item);

        this.ownerInventory.remove(item.getShopItem());
        this.elfInventory.remove(item.getShopItem());
        this.dwarfInventory.remove(item.getShopItem());
        this.humanInventory.remove(item.getShopItem());
    }

    @Override
    public void setLocation(Location location)
    {
        this.location = location;
    }

    @Override
    public void setOwner(UUID newOwner)
    {
        this.owner = newOwner;
    }

    @Override
    public void setType(ShopType shopType)
    {
        this.shopType = shopType;
    }

    @Override
    public void setOpen(Boolean isOpen)
    {
        this.isOpen = isOpen;
        if (isOpen)
        {
            this.armourStand.setCustomName(ChatColor.GREEN + name);
        }
        else
        {
            this.armourStand.setCustomName(ChatColor.RED + name);
        }
    }

    @Override
    public void showName()
    {
        this.armourStand = (ArmorStand) this.location.getWorld().spawnEntity(armourStandLocation, EntityType.ARMOR_STAND);

        armourStand.setVisible(false);
        armourStand.setCustomName(ChatColor.RED + getName());
        armourStand.setCustomNameVisible(true);
        armourStand.setSmall(true);
        armourStand.setBasePlate(false);

        EntityArmorStand nmsarmourstand = ((CraftArmorStand) armourStand).getHandle();

        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nmsarmourstand.c(nbtTagCompound);
        nbtTagCompound.setBoolean("Marker", true); //destroys stand if set after "setgravity"
        nmsarmourstand.f(nbtTagCompound);

        armourStand.setGravity(false);
    }

    @Override
    public void changeName(String name)
    {
        if (this.isOpen)
        {
            this.armourStand.setCustomName(ChatColor.GREEN + name);
        }
        else
        {
            this.armourStand.setCustomName(ChatColor.RED + name);
        }
    }

    @Override
    public void createShop()
    {
        if (this.location.getZ() < 0)
        {
            this.armourStandLocation = new Location(this.location.getWorld(), this.location.getX() - 0.47, this.location.getY() + 0.7, this.location.getZ());
        } else if (this.location.getZ() >= 0)
        {
            this.armourStandLocation = new Location(this.location.getWorld(), this.location.getX(), this.location.getY() + 0.7, this.location.getZ() + 0.5);
        }

        Block block = this.location.getBlock();
        Block blockRelative = block.getRelative(BlockFace.WEST);

        block.setType(Material.CHEST);
        blockRelative.setType(Material.CHEST);

        block.setMetadata("shop", new FixedMetadataValue(Vowed.getPlugin(), true));
        blockRelative.setMetadata("shop", new FixedMetadataValue(Vowed.getPlugin(), true));

        showName();
    }

    @Override
    public void saveContents()
    {
        this.shopInventory.put(shop.get(owner), this.getContents());
    }

}