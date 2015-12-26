package me.vowed.api.items;

import me.vowed.api.plugin.Vowed;
import me.vowed.api.tier.ArmourTier;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by JPaul on 10/4/2015.
 */
public class Armour implements IArmour
{
    static HashMap<UUID, IArmour> UUIDLocator = new HashMap<>();

    private IArmour armourManger;
    private ItemStack armour;
    private List<String> lore;
    private String name;
    private Color colour;
    private UUID uuid;
    private ArmourTier armourTier;

    public Armour(ItemStack armour, Color armourColor, String armourName, ArmourTier armourTier)
    {
        this.armour = armour;
        this.colour = armourColor;
        this.name = armourName;
        this.armourTier = armourTier;
        this.armourManger = this;
        UUIDLocator.put(UUID.randomUUID(), this);
    }

    public void make(String armourType)
    {
        switch (armourType.toLowerCase())
        {
            case "boots":
                generateBoots();
                break;
            case "leggings":
                generateLeggings(ArmourTier.TIER1, getLore(), getName());
                break;
            case "chestPlate":
                generateChestPlate(ArmourTier.TIER1, getLore(), getName());
                break;
            case "helmet":
                generateHelmet(ArmourTier.TIER1, getLore(), getName());
                break;
            default:
                Vowed.LOG.severe("Could not make Armour, NULL");
                break;
        }
    }

    public void applyStats(Player player)
    {
        int health = 0;
        int armourPercent = 0;
        float speedPercent = 0;

        if (getArmour() != null && getArmour().hasItemMeta() && getArmour().getItemMeta().hasLore()) //have to use getArmour() now, ItemStack armour returns normal.
        {
            List<String> armourLore = getArmour().getItemMeta().getLore();

            for (String finder : armourLore)
            {
                if (finder.contains("HP"))
                {
                    health = Integer.parseInt(finder.substring(finder.indexOf("HP:") + 6));
                }
                if (finder.contains("Armour"))
                {
                    armourPercent = Integer.parseInt(finder.substring(finder.indexOf("Armour:") + 10).replaceAll("%", ""));
                }
                if (finder.contains("Speed"))
                {
                    speedPercent = (Float.parseFloat(finder.substring(finder.indexOf("Speed:") + 9).replaceAll("%", "")));
                }
            }
        }
        if (speedPercent != 0 && player.getEquipment().getBoots() == getArmour())
        {
            player.setWalkSpeed(getSpeedPercent());
        }
    }

    public List<String> generateLore()
    {
        List<String> lore = new ArrayList<>();
        int chance = new Random().nextInt(100) + 1;
        int HP = 0;
        int armourPercent = 0;
        double speedPercent = 0;
        String rarity;

        /*
        Yes I know, I could've done a lot better structurally
        Could've gone
        armourTier first then type, but I was too tired at the time I made this
         */

        switch (armour.getType())
        {
            case LEATHER_BOOTS:

                switch (armourTier)
                {
                    case TIER1:
                        if (chance > 97)
                        {
                            HP = getRandomInt(53, 72);
                            armourPercent = getRandomInt(2, 5);
                            speedPercent = getRandomDouble(0.27, 0.4);
                        }
                        else if (chance > 94)
                        {
                            HP = getRandomInt(43, 52);
                            armourPercent = getRandomInt(2, 5);
                            speedPercent = getRandomDouble(0.27, 0.4);
                        }
                        else if (chance > 75)
                        {
                            HP = getRandomInt(26, 42);
                            armourPercent = getRandomInt(2, 5);
                            speedPercent = getRandomDouble(0.27, 0.4);
                        }
                        else
                        {
                            HP = getRandomInt(3, 25);
                            armourPercent = getRandomInt(2, 5);
                            speedPercent = getRandomDouble(0.27, 0.4);
                        }
                        break;

                    case TIER2:
                        if (chance > 97)
                        {
                            HP = getRandomInt(247, 289);
                            armourPercent = getRandomInt(5, 8);
                            speedPercent = getRandomDouble(0.27, 0.4);
                        }
                        else if (chance > 94)
                        {
                            HP = getRandomInt(190, 246);
                            armourPercent = getRandomInt(5, 8);
                            speedPercent = getRandomDouble(0.27, 0.4);
                        }
                        else if (chance > 75)
                        {
                            HP = getRandomInt(132, 189);
                            armourPercent = getRandomInt(5, 8);
                            speedPercent = getRandomDouble(0.27, 0.4);
                        }
                        else
                        {
                            HP = getRandomInt(82, 131);
                            armourPercent = getRandomInt(5, 8);
                            speedPercent = getRandomDouble(0.27, 0.4);
                        }

                        break;

                    case TIER3:
                        if (chance > 97)
                        {
                            HP = getRandomInt(391, 432);
                            armourPercent = getRandomInt(7, 10);
                            speedPercent = getRandomDouble(0.27, 0.4);
                        }
                        else if (chance > 94)
                        {
                            HP = getRandomInt(305, 390);
                            armourPercent = getRandomInt(7, 10);
                            speedPercent = getRandomDouble(0.27, 0.4);
                        }
                        else if (chance > 75)
                        {
                            HP = getRandomInt(254, 304);
                            armourPercent = getRandomInt(7, 10);
                            speedPercent = getRandomDouble(0.27, 0.4);
                        }
                        else
                        {
                            HP = getRandomInt(178, 253);
                            armourPercent = getRandomInt(7, 10);
                            speedPercent = getRandomDouble(0.27, 0.4);
                        }

                        break;

                    case TIER4:
                        if (chance > 97)
                        {
                            HP = getRandomInt(759, 827);
                            armourPercent = getRandomInt(9, 13);
                            speedPercent = getRandomDouble(0.27, 0.4);
                        }
                        else if (chance > 89)
                        {
                            HP = getRandomInt(679, 758);
                            armourPercent = getRandomInt(9, 13);
                            speedPercent = getRandomDouble(0.27, 0.4);
                        }
                        else if (chance > 75)
                        {
                            HP = getRandomInt(500, 678);
                            armourPercent = getRandomInt(9, 13);
                            speedPercent = getRandomDouble(0.27, 0.4);
                        }
                        else
                        {
                            HP = getRandomInt(403, 499);
                            armourPercent = getRandomInt(9, 13);
                            speedPercent = getRandomDouble(0.27, 0.4);
                        }
                        break;

                    case TIER5:
                        if (chance > 97)
                        {
                            HP = getRandomInt(1073, 1163);
                            armourPercent = getRandomInt(8, 14);
                            speedPercent = getRandomDouble(0.27, 0.4);
                        }
                        else if (chance > 94)
                        {
                            HP = getRandomInt(986, 1072);
                            armourPercent = getRandomInt(8, 14);
                            speedPercent = getRandomDouble(0.27, 0.4);
                        }
                        else if (chance > 75)
                        {
                            HP = getRandomInt(843, 985);
                            armourPercent = getRandomInt(8, 14);
                            speedPercent = getRandomDouble(0.27, 0.4);
                        }
                        else
                        {
                            HP = getRandomInt(608, 842);
                            armourPercent = getRandomInt(8, 14);
                            speedPercent = getRandomDouble(0.27, 0.4);
                        }
                        Vowed.LOG.info(String.valueOf(speedPercent));
                        break;


                }

                break;

            case LEATHER_LEGGINGS:

                switch (armourTier)
                {
                    case TIER1:
                        if (chance > 97)
                        {
                            HP = getRandomInt(87, 107);
                            armourPercent = getRandomInt(3, 7);
                        }
                        else if (chance > 89)
                        {
                            HP = getRandomInt(70, 86);
                            armourPercent = getRandomInt(3, 7);
                        }
                        else if (chance > 75)
                        {
                            HP = getRandomInt(43, 69);
                            armourPercent = getRandomInt(3, 7);
                        }
                        else
                        {
                            HP = getRandomInt(17, 42);
                            armourPercent = getRandomInt(3, 7);
                        }

                        break;

                    case TIER2:
                        if (chance > 97)
                        {
                            HP = getRandomInt(396, 432);
                            armourPercent = getRandomInt(5, 8);
                        }
                        else if (chance > 89)
                        {
                            HP = getRandomInt(283, 395);
                            armourPercent = getRandomInt(5, 8);
                        }
                        else if (chance > 75)
                        {
                            HP = getRandomInt(190, 282);
                            armourPercent = getRandomInt(5, 8);
                        }
                        else
                        {
                            HP = getRandomInt(100, 189);
                            armourPercent = getRandomInt(5, 8);
                        }

                        break;

                    case TIER3:
                        if (chance > 97)
                        {
                            HP = getRandomInt(704, 778);
                            armourPercent = getRandomInt(7, 10);
                        }
                        else if (chance > 89)
                        {
                            HP = getRandomInt(603, 703);
                            armourPercent = getRandomInt(7, 10);
                        }
                        else if (chance > 75)
                        {
                            HP = getRandomInt(521, 602);
                            armourPercent = getRandomInt(7, 10);
                        }
                        else
                        {
                            HP = getRandomInt(321, 520);
                            armourPercent = getRandomInt(7, 10);
                        }

                        break;

                    case TIER4:
                        if (chance > 97)
                        {
                            HP = getRandomInt(1800, 1900);
                            armourPercent = getRandomInt(9, 12);
                        }
                        else if (chance > 89)
                        {
                            HP = getRandomInt(1643, 1799);
                            armourPercent = getRandomInt(9, 12);
                        }
                        else if (chance > 75)
                        {
                            HP = getRandomInt(1329, 1642);
                            armourPercent = getRandomInt(9, 12);
                        }
                        else
                        {
                            HP = getRandomInt(700, 1328);
                            armourPercent = getRandomInt(9, 12);
                        }

                        break;

                    case TIER5:
                        if (chance > 97)
                        {
                            HP = getRandomInt(2300, 2500);
                            armourPercent = getRandomInt(10, 14);
                        }
                        else if (chance > 89)
                        {
                            HP = getRandomInt(1900, 2299);
                            armourPercent = getRandomInt(10, 14);
                        }
                        else if (chance > 75)
                        {
                            HP = getRandomInt(1732, 1899);
                            armourPercent = getRandomInt(10, 14);
                        }
                        else
                        {
                            HP = getRandomInt(1200, 1731);
                            armourPercent = getRandomInt(10, 14);
                        }

                        break;

                }


                break;

            case LEATHER_CHESTPLATE:

                switch (armourTier)
                {
                    case TIER1:
                        if (chance > 97)
                        {
                            HP = getRandomInt(87, 107);
                            armourPercent = getRandomInt(3, 7);
                        }
                        else if (chance > 89)
                        {
                            HP = getRandomInt(70, 86);
                            armourPercent = getRandomInt(3, 7);
                        }
                        else if (chance > 75)
                        {
                            HP = getRandomInt(43, 69);
                            armourPercent = getRandomInt(3, 7);
                        }
                        else
                        {
                            HP = getRandomInt(17, 42);
                            armourPercent = getRandomInt(3, 7);
                        }

                        break;

                    case TIER2:
                        if (chance > 97)
                        {
                            HP = getRandomInt(396, 432);
                            armourPercent = getRandomInt(5, 8);
                        }
                        else if (chance > 89)
                        {
                            HP = getRandomInt(283, 395);
                            armourPercent = getRandomInt(5, 8);
                        }
                        else if (chance > 75)
                        {
                            HP = getRandomInt(190, 282);
                            armourPercent = getRandomInt(5, 8);
                        }
                        else
                        {
                            HP = getRandomInt(100, 189);
                            armourPercent = getRandomInt(5, 8);
                        }

                        break;

                    case TIER3:
                        if (chance > 97)
                        {
                            HP = getRandomInt(704, 778);
                            armourPercent = getRandomInt(7, 10);
                        }
                        else if (chance > 89)
                        {
                            HP = getRandomInt(603, 703);
                            armourPercent = getRandomInt(7, 10);
                        }
                        else if (chance > 75)
                        {
                            HP = getRandomInt(521, 602);
                            armourPercent = getRandomInt(7, 10);
                        }
                        else
                        {
                            HP = getRandomInt(321, 520);
                            armourPercent = getRandomInt(7, 10);
                        }

                        break;

                    case TIER4:
                        if (chance > 97)
                        {
                            HP = getRandomInt(1800, 1900);
                            armourPercent = getRandomInt(9, 12);
                        }
                        else if (chance > 89)
                        {
                            HP = getRandomInt(1643, 1799);
                            armourPercent = getRandomInt(9, 12);
                        }
                        else if (chance > 75)
                        {
                            HP = getRandomInt(1329, 1642);
                            armourPercent = getRandomInt(9, 12);
                        }
                        else
                        {
                            HP = getRandomInt(700, 1328);
                            armourPercent = getRandomInt(9, 12);
                        }

                        break;

                    case TIER5:
                        if (chance > 97)
                        {
                            HP = getRandomInt(2300, 2500);
                            armourPercent = getRandomInt(10, 14);
                        }
                        else if (chance > 89)
                        {
                            HP = getRandomInt(1900, 2299);
                            armourPercent = getRandomInt(10, 14);
                        }
                        else if (chance > 75)
                        {
                            HP = getRandomInt(1732, 1899);
                            armourPercent = getRandomInt(10, 14);
                        }
                        else
                        {
                            HP = getRandomInt(1200, 1731);
                            armourPercent = getRandomInt(10, 14);
                        }

                        break;

                }


                break;

            case LEATHER_HELMET:

                switch (armourTier)
                {
                    case TIER1:
                        if (chance > 97)
                        {
                            HP = getRandomInt(53, 72);
                            armourPercent = getRandomInt(2, 5);
                        }
                        else if (chance > 89)
                        {
                            HP = getRandomInt(43, 52);
                            armourPercent = getRandomInt(2, 5);
                        }
                        else if (chance > 75)
                        {
                            HP = getRandomInt(26, 42);
                            armourPercent = getRandomInt(2, 5);
                        }
                        else
                        {
                            HP = getRandomInt(3, 25);
                            armourPercent = getRandomInt(2, 5);
                        }

                        break;

                    case TIER2:
                        if (chance > 97)
                        {
                            HP = getRandomInt(247, 289);
                            armourPercent = getRandomInt(5, 8);
                        }
                        else if (chance > 89)
                        {
                            HP = getRandomInt(190, 246);
                            armourPercent = getRandomInt(5, 8);
                        }
                        else if (chance > 75)
                        {
                            HP = getRandomInt(132, 189);
                            armourPercent = getRandomInt(5, 8);
                        }
                        else
                        {
                            HP = getRandomInt(82, 131);
                            armourPercent = getRandomInt(5, 8);
                        }

                        break;

                    case TIER3:
                        if (chance > 97)
                        {
                            HP = getRandomInt(391, 432);
                            armourPercent = getRandomInt(7, 10);
                        }
                        else if (chance > 89)
                        {
                            HP = getRandomInt(305, 390);
                            armourPercent = getRandomInt(7, 10);
                        }
                        else if (chance > 75)
                        {
                            HP = getRandomInt(254, 304);
                            armourPercent = getRandomInt(7, 10);
                        }
                        else
                        {
                            HP = getRandomInt(178, 253);
                            armourPercent = getRandomInt(7, 10);
                        }

                        break;

                    case TIER4:
                        if (chance > 97)
                        {
                            HP = getRandomInt(759, 827);
                            armourPercent = getRandomInt(9, 13);
                        }
                        else if (chance > 89)
                        {
                            HP = getRandomInt(679, 758);
                            armourPercent = getRandomInt(9, 13);
                        }
                        else if (chance > 75)
                        {
                            HP = getRandomInt(500, 678);
                            armourPercent = getRandomInt(9, 13);
                        }
                        else
                        {
                            HP = getRandomInt(403, 499);
                            armourPercent = getRandomInt(9, 13);
                        }

                        break;

                    case TIER5:
                        if (chance > 97)
                        {
                            HP = getRandomInt(1073, 1163);
                            armourPercent = getRandomInt(8, 14);
                        }
                        else if (chance > 89)
                        {
                            HP = getRandomInt(986, 1072);
                            armourPercent = getRandomInt(8, 14);
                        }
                        else if (chance > 75)
                        {
                            HP = getRandomInt(843, 985);
                            armourPercent = getRandomInt(8, 14);
                        }
                        else
                        {
                            HP = getRandomInt(608, 842);
                            armourPercent = getRandomInt(8, 14);
                        }

                        break;

                }

                break;
        }

        lore.add(ChatColor.RESET + ChatColor.GREEN.toString() + "HP: " + ChatColor.RESET + HP);
        lore.add(ChatColor.RESET + ChatColor.GREEN.toString() + "Armour: " + ChatColor.RESET + armourPercent + "%");


        if (chance > 97)
        {
            rarity = ChatColor.RESET + ChatColor.RED.toString() + "Rarity: " + ChatColor.RESET + "Unique";
        }
        else if (chance > 89)
        {
            rarity = ChatColor.RESET + ChatColor.RED.toString() + "Rarity: " + ChatColor.RESET + "Rare";
        }
        else if (chance > 75)
        {
            rarity = ChatColor.RESET + ChatColor.RED.toString() + "Rarity: " + ChatColor.RESET + "Uncommon";
        }
        else
        {
            rarity = ChatColor.RESET + ChatColor.RED.toString() + "Rarity: " + ChatColor.RESET + "Common";
        }

        if (chance > 60)
        {
            if (speedPercent != 0)
            {
                lore.add(ChatColor.RESET + ChatColor.GREEN.toString() + "Speed: " + ChatColor.RESET + round(speedPercent, 2)  + "%");
            }
        }

        lore.add(rarity);
        return lore;
    }


    @Override
    public ItemStack getArmour()
    {
        return armour;
    }

    @Override
    public UUID getUUID()
    {
        return this.uuid;
    }

    @Override
    public ArmourTier getTier()
    {
        return this.armourTier;
    }

    @Override
    public List<String> getLore()
    {
        return this.lore;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public Color getColour()
    {
        return this.colour;
    }

    @Override
    public int getHP()
    {
        int health = 0;

        if (armour != null && armour.hasItemMeta() && armour.getItemMeta().hasLore())
        {
            List<String> armourLore = armour.getItemMeta().getLore();

            for (String finder : armourLore)
            {
                if (finder.contains("HP:"))
                {
                    health = Integer.parseInt(finder.substring(finder.indexOf("HP:") + 6));
                }
            }
        }

        return health;
    }

    @Override
    public int getArmourPercent()
    {
        int armourPercent = 0;

        if (armour != null && armour.hasItemMeta() && armour.getItemMeta().hasLore())
        {
            List<String> armourLore = armour.getItemMeta().getLore();

            for (String finder : armourLore)
            {
                if (finder.contains("Armour:"))
                {
                    armourPercent = Integer.parseInt(finder.substring(finder.indexOf("Armour:") + 10).replaceAll("%", ""));
                }
            }
        }

        return armourPercent;
    }

    @Override
    public float getSpeedPercent()
    {
        float speedPercent = 0;

        if (armour != null && armour.hasItemMeta() && armour.getItemMeta().hasLore())
        {
            List<String> armourLore = armour.getItemMeta().getLore();

            for (String finder : armourLore)
            {
                if (finder.contains("Speed:"))
                {
                    speedPercent = (Float.parseFloat(finder.substring(finder.indexOf("Speed:") + 9).replaceAll("%", "")));
                }
            }
        }

        return speedPercent;
    }

    @Override
    public void setArmour(ItemStack armour)
    {
        this.armour = armour;
    }

    @Override
    public void setUUID(UUID uuid)
    {
        this.uuid = uuid;
    }

    @Override
    public void setItemLore(List<String> lore)
    {
        ItemMeta itemMeta = armour.getItemMeta();
        itemMeta.setLore(lore);
        armour.setItemMeta(itemMeta);
        this.lore = lore;
    }

    @Override
    public void addToLore(String string)
    {
        this.lore.add(string);
        setItemLore(this.lore);
    }

    @Override
    public void setItemName(String name)
    {
        ItemMeta itemMeta = armour.getItemMeta();
        itemMeta.setDisplayName(name);
        armour.setItemMeta(itemMeta);
        this.name = name;
    }

    @Override
    public void setTier(ArmourTier armourTier)
    {
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) armour.getItemMeta();

        switch (armourTier)
        {
            case TIER1:
                leatherArmorMeta.setColor(Color.LIME);
                break;
            case TIER2:
                leatherArmorMeta.setColor(Color.BLACK);
                break;
            case TIER3:
                leatherArmorMeta.setColor(Color.RED);
                break;
            case TIER4:
                leatherArmorMeta.setColor(Color.AQUA);
                break;
            case TIER5:
                leatherArmorMeta.setColor(Color.FUCHSIA);
                break;
        }

        armour.setItemMeta(leatherArmorMeta);
        this.colour = leatherArmorMeta.getColor();
    }

    @Override
    public void setInChest(Chest chest)
    {
        chest.getInventory().setItem(0, getArmour());
    }

    @Override
    public void update(ItemStack item)
    {
        ItemMeta itemMeta = armour.getItemMeta();
        itemMeta.setDisplayName(getName());
        itemMeta.setLore(getLore());
        item.setItemMeta(itemMeta);
    }

    @Override
    public ItemStack generateBoots()
    {
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        this.armour = boots;
        this.setTier(this.armourTier);
        this.setItemLore(generateLore());
        this.setItemName(this.name);

        return boots;
    }

    @Override
    public ItemStack generateLeggings(ArmourTier armourTier, List<String> lore, String name)
    {
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        this.setArmour(leggings);
        this.setTier(armourTier);
        this.setItemLore(lore);
        this.setItemName(name);

        return leggings;
    }

    @Override
    public ItemStack generateChestPlate(ArmourTier armourTier, List<String> lore, String name)
    {
        ItemStack chestPlate = new ItemStack(Material.LEATHER_CHESTPLATE);
        this.setArmour(chestPlate);
        this.setTier(armourTier);
        this.setItemLore(lore);
        this.setItemName(name);

        return chestPlate;
    }

    @Override
    public ItemStack generateHelmet(ArmourTier armourTier, List<String> lore, String name)
    {
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        this.setArmour(helmet);
        this.setTier(armourTier);
        this.setItemLore(lore);
        this.setItemName(name);

        return helmet;
    }

    public int getRandomInt(int min, int max){
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public double getRandomDouble(double min, double max)
    {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
