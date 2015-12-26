package me.vowed.api.shops;

import me.vowed.api.money.currency.CurrencyFactory;
import me.vowed.api.player.PlayerWrapper;
import me.vowed.api.plugin.Vowed;
import net.minecraft.server.v1_8_R3.Item;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by JPaul on 11/12/2015.
 */
public class ShopItem
{
    private ItemStack shopItem;
    private UniqueItem uniqueItem;
    private ItemMeta itemMeta;
    private String newName;
    private String oldName;
    private List<String> oldLore;
    private String dwarfPrice;
    private String elfPrice;
    private String humanPrice;
    private BigDecimal dwarfINTPrice;
    private BigDecimal elfINTPrice;
    private BigDecimal humanINTPrice;
    private String uuid;

    public ShopItem(UniqueItem shopItem)
    {
        this.shopItem = shopItem.getItemStack();
        this.itemMeta = shopItem.getItemStack().getItemMeta();
        this.uuid = shopItem.getUUID();
        this.uniqueItem = shopItem;
        this.oldName = shopItem.getItemStack().getItemMeta().getDisplayName();

        if (shopItem.getItemStack().getItemMeta().getLore() == null)
        {
            List<String> lore = new ArrayList<>();
            shopItem.getItemStack().getItemMeta().setLore(lore);
            shopItem.getItemStack().setItemMeta(shopItem.getItemStack().getItemMeta());
            this.oldLore = shopItem.getItemStack().getItemMeta().getLore();
        } else
        {
            this.oldLore = shopItem.getItemStack().getItemMeta().getLore();
        }

        if (shopItem.getItemStack().getType() == Material.STAINED_GLASS_PANE)
        {
            setButton();
        } else
        {
            if (shopItem.getItemStack().getItemMeta().getDisplayName() != null)
            {
                setName(ChatColor.RESET.toString() + ChatColor.GREEN + "WTS: " + ChatColor.RESET + shopItem.getItemStack().getItemMeta().getDisplayName() + " x" + shopItem.getItemStack().getAmount());
            } else
            {
                String name = shopItem.getItemStack().getType().name().toLowerCase();
                setName(ChatColor.RESET.toString() + ChatColor.GREEN + "WTS: " + ChatColor.RESET + name.substring(0, 1).toUpperCase() + name.substring(1) + " x" + shopItem.getItemStack().getAmount());
            }
        }
    }

    public ItemStack getShopItem()
    {
        return this.shopItem;
    }

    void setShopItem(ItemStack item)
    {
        this.shopItem = item;
    }

    public ItemMeta getItemMeta()
    {
        return this.itemMeta;
    }

    public String getDwarfPrice()
    {
        return dwarfPrice;
    }

    public int getDwarfINTPrice()
    {
        return this.dwarfINTPrice.intValue();
    }

    public String getElfPrice()
    {
        return elfPrice;
    }

    public int getElfINTPrice()
    {
        return this.elfINTPrice.intValue();
    }

    public String getHumanPrice()
    {
        return humanPrice;
    }

    public int getHumanINTPrice()
    {
        return this.humanINTPrice.intValue();
    }

    public String getUUID()
    {
        return this.uuid;
    }

    public String getNewName()
    {
        return this.newName;
    }

    public UniqueItem getUniqueItem()
    {
        return this.uniqueItem;
    }

    public void addPrice(int price, IShop shop)
    {
        this.dwarfINTPrice = CurrencyFactory.getInstance(Vowed.getRaceManager().getRace(shop.getOwner()).getName()).convertCurrencyToDWARF(BigDecimal.valueOf(price));
        this.elfINTPrice = CurrencyFactory.getInstance(Vowed.getRaceManager().getRace(shop.getOwner()).getName()).convertCurrencyToELF(BigDecimal.valueOf(price));
        this.humanINTPrice = CurrencyFactory.getInstance(Vowed.getRaceManager().getRace(shop.getOwner()).getName()).convertCurrencyToHUMAN(BigDecimal.valueOf(price));

        this.dwarfPrice = ChatColor.RESET.toString() +
                ChatColor.DARK_RED +
                ChatColor.BOLD +
                "Dwarf " +
                ChatColor.GREEN +
                "Price: " + ChatColor.RESET + dwarfINTPrice.intValue() + "$";

        this.elfPrice = ChatColor.RESET.toString() +
                ChatColor.DARK_GREEN +
                ChatColor.BOLD +
                "Elf " +
                ChatColor.GREEN +
                "Price: " + ChatColor.RESET + elfINTPrice.intValue() + "$";

        this.humanPrice = ChatColor.RESET.toString() +
                ChatColor.DARK_AQUA +
                ChatColor.BOLD +
                "Human " +
                ChatColor.GREEN +
                "Price: " + ChatColor.RESET + humanINTPrice.intValue() + "$";

        if (this.itemMeta.getLore() == null)
        {
            Vowed.LOG.info("no lore");
            List<String> lore = Arrays.asList(new String[7]);
            lore.set(1, ChatColor.RESET.toString() + ChatColor.STRIKETHROUGH + "-------------------");
            lore.set(2, this.elfPrice);
            lore.set(3, this.dwarfPrice);
            lore.set(4, this.humanPrice);
            lore.set(5, ChatColor.RESET.toString() + ChatColor.STRIKETHROUGH + "-------------------");
            lore.set(6, ChatColor.RESET.toString() + ChatColor.AQUA + this.uuid + ChatColor.RESET);
            this.itemMeta.setDisplayName(this.newName);
            this.itemMeta.setLore(lore);
            this.shopItem.setItemMeta(this.itemMeta);
        } else
        {
            Vowed.LOG.info("lore");
            List<String> lore = this.itemMeta.getLore();
            lore.add(ChatColor.RESET.toString() + ChatColor.STRIKETHROUGH + "-------------------");
            lore.add(this.elfPrice);
            lore.add(this.dwarfPrice);
            lore.add(this.humanPrice);
            lore.add(ChatColor.RESET.toString() + ChatColor.STRIKETHROUGH + "-------------------");
            lore.add(ChatColor.RESET.toString() + ChatColor.AQUA + this.uuid);
            this.itemMeta.setDisplayName(this.newName);
            this.itemMeta.setLore(lore);
            this.shopItem.setItemMeta(this.itemMeta);
        }

        Vowed.LOG.info(String.valueOf(dwarfINTPrice.intValue() + " " + elfINTPrice.intValue() + " " + humanINTPrice.intValue()));
    }

    public void removePrice()
    {
        this.itemMeta.setLore(this.oldLore);
        this.itemMeta.setDisplayName(this.oldName);
        this.shopItem.setItemMeta(this.itemMeta);
    }

    public void setName(String name)
    {
        this.newName = name;
        ItemMeta itemMeta = this.itemMeta;
        itemMeta.setDisplayName(name);
        this.shopItem.setItemMeta(this.itemMeta);
    }

    public void addLore(String line)
    {
        if (this.itemMeta.getLore() == null)
        {
            List<String> lore = Arrays.asList(new String[1]);
            lore.set(0, line);
            this.itemMeta.setLore(lore);
            this.shopItem.setItemMeta(this.itemMeta);
        } else
        {
            List<String> lore = this.itemMeta.getLore();

            if (lore.contains(line))
            {
                lore.remove(line);
                lore.add(line);
            } else
            {
                lore.add(line);
            }
            this.itemMeta.setLore(lore);
            this.shopItem.setItemMeta(this.itemMeta);
        }
    }

    public static DyeColor getColour(ItemStack glassPane)
    {
        return DyeColor.getByData(glassPane.getData().getData());
    }

    public void setButton()
    {
        if (Objects.equals(getColour(shopItem), DyeColor.RED))
        {
            setName(ChatColor.RESET.toString() + ChatColor.GREEN + ChatColor.BOLD + "Open your shop!");
            addLore(ChatColor.RESET.toString() + ChatColor.STRIKETHROUGH + "-------------------");
            addLore(ChatColor.RESET.toString() + ChatColor.AQUA + this.uuid);
        } else if (Objects.equals(getColour(shopItem), DyeColor.LIME))
        {
            setName(ChatColor.RESET.toString() + ChatColor.RED + ChatColor.BOLD + "Close your shop!");
            addLore(ChatColor.RESET.toString() + ChatColor.STRIKETHROUGH + "-------------------");
            addLore(ChatColor.RESET.toString() + ChatColor.AQUA + this.uuid);
        }
    }

    public UniqueItem getElfInstance(ItemStack shopItem)
    {
        ItemStack fakeItem = shopItem.clone();
        ItemMeta itemMeta = fakeItem.getItemMeta();
        List<String> lore = itemMeta.getLore();
        lore.remove(this.humanPrice);
        lore.remove(this.dwarfPrice);

        String yourPrice = ChatColor.RESET.toString() +
                ChatColor.RED +
                "Your " +
                ChatColor.GREEN +
                "Price: " + ChatColor.RESET + this.elfINTPrice.intValue() + "$";

        lore.set(lore.size() - 3, yourPrice);
        itemMeta.setLore(lore);
        fakeItem.setItemMeta(itemMeta);

        return new UniqueItem(fakeItem, this.uuid);
    }

    public UniqueItem getDwarfInstance(ItemStack shopItem)
    {
        ItemStack fakeItem = shopItem.clone();
        ItemMeta itemMeta = fakeItem.getItemMeta();
        List<String> lore = itemMeta.getLore();
        lore.remove(this.humanPrice);
        lore.remove(this.elfPrice);

        String yourPrice = ChatColor.RESET.toString() +
                ChatColor.RED +
                "Your " +
                ChatColor.GREEN +
                "Price: " + ChatColor.RESET + this.dwarfINTPrice.intValue() + "$";

        lore.set(lore.size() - 3, yourPrice);
        itemMeta.setLore(lore);
        fakeItem.setItemMeta(itemMeta);

        return new UniqueItem(fakeItem, this.uuid);
    }

    public UniqueItem getHumanInstance(ItemStack shopItem)
    {
        ItemStack fakeItem = shopItem.clone();
        ItemMeta itemMeta = fakeItem.getItemMeta();
        List<String> lore = itemMeta.getLore();
        lore.remove(this.dwarfPrice);
        lore.remove(this.elfPrice);

        String yourPrice = ChatColor.RESET.toString() +
                ChatColor.RED +
                "Your " +
                ChatColor.GREEN +
                "Price: " + ChatColor.RESET + this.humanINTPrice.intValue() + "$";

        lore.set(lore.size() - 3, yourPrice);
        itemMeta.setLore(lore);
        fakeItem.setItemMeta(itemMeta);

        return new UniqueItem(fakeItem, this.uuid);
    }
}
