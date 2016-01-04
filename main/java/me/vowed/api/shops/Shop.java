package me.vowed.api.shops;

import me.vowed.api.player.PlayerWrapper;
import me.vowed.api.plugin.Vowed;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
    private ShopManager manager;
    private Location block1;
    private Location block2;


    public static HashMap<Player, IShop> iShopLocaterPlayer = new HashMap<>();
    public static HashMap<Location, IShop> iShopLocaterLocation = new HashMap<>();
    private HashMap<IShop, List<ShopItem>> shopInventory = new HashMap<>();



    public Shop(Player player, ShopManager manager, String name, Location location, ShopType shopType)
    {
        this.owner = player.getUniqueId();
        this.name = name;
        this.contents = new ArrayList<>();
        this.location = location;
        manager.shop.put(player.getUniqueId(), this);
        this.shopType = shopType;
        this.manager = manager;
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
        return manager.shop.get(owner);
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
        } else
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
        } else
        {
            this.armourStand.setCustomName(ChatColor.RED + name);
        }
    }

    @Override
    public void showName()
    {
        this.armourStand = (ArmorStand) this.location.getWorld().spawnEntity(armourStandLocation, EntityType.ARMOR_STAND);

        EntityArmorStand nmsarmourstand = ((CraftArmorStand) armourStand).getHandle();

        armourStand.setMarker(true);
        armourStand.setGravity(false);
        armourStand.setVisible(false);
        armourStand.setCustomName(ChatColor.RED + getName());
        armourStand.setCustomNameVisible(true);
        armourStand.setSmall(true);
        armourStand.setBasePlate(false);
    }

    @Override
    public void changeName(String name)
    {
        Vowed.LOG.debug(name);
        Vowed.LOG.debug(isOpen);
        this.name = name;
        if (this.isOpen)
        {
            this.armourStand.setCustomName(ChatColor.GREEN + name);
        } else
        {
            this.armourStand.setCustomName(ChatColor.RED + name);
        }
    }

    @Override
    public void createShop()
    {
        Block block = this.location.getBlock();
        Block blockRelative = block.getRelative(BlockFace.WEST);

        block.setType(Material.CHEST);
        blockRelative.setType(Material.CHEST);

        block1 = block.getLocation();
        block2 = blockRelative.getLocation();

        block.setMetadata("shop", new FixedMetadataValue(Vowed.getPlugin(), true));
        blockRelative.setMetadata("shop", new FixedMetadataValue(Vowed.getPlugin(), true));


        if (block.getZ() < 0)
        {
            this.armourStandLocation = new Location(this.location.getWorld(), block.getX(), block.getY() + 0.7, block.getZ() + 0.5);
        } else if (block.getZ() >= 0)
        {
            this.armourStandLocation = new Location(this.location.getWorld(), block.getX(), block.getY() + 0.7, block.getZ() + 0.5);
        }

        showName();
    }


    @Override
    public void saveContents()
    {
        this.shopInventory.put(manager.shop.get(owner), this.getContents());
    }

    @Override
    public void destroyShop(Player requester){
            if (!isOpen()) {
                if (manager.isOwner(this, requester)) {
                    manager.shops.remove(this);
                    manager.locations.remove(block1);
                    manager.locations.remove(block2);
                    manager.shop.remove(getOwnerUUID());
                    Vowed.LOG.debug(manager.shop.toString());
                    block1.getBlock().setType(Material.AIR);
                    block2.getBlock().setType(Material.AIR);
                    armourStand.remove();

                    for(ShopItem item : contents) {
                        Vowed.LOG.debug(item.getShopItem().toString());
                        item.removePrice();
                        Vowed.LOG.debug(item.getShopItem().toString());
                    }
                    requester.sendMessage(ChatColor.GREEN + "You have removed your shop");
                }
                else{
                    requester.sendMessage(ChatColor.RED + "You are not the owner of this shop!");
                }
            }
            else {
                requester.sendMessage(ChatColor.RED + "The shop has to be closed for this action to be done!");
            }
    }

}
