package me.vowed.api.items;

import me.vowed.api.tier.ArmourTier;
import org.bukkit.Color;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

/**
 * Created by JPaul on 10/4/2015.
 */
public interface IArmour
{
    ItemStack getArmour();

    UUID getUUID();

    ArmourTier getTier();

    List<String> getLore();

    String getName();

    Color getColour();

    int getHP();

    int getArmourPercent();

    float getSpeedPercent();

    void setArmour(ItemStack armour);

    void setUUID(UUID uuid);

    void setItemLore(List<String> lore);

    void addToLore(String string);

    void setItemName(String name);

    void setTier(ArmourTier armourTier);

    void setInChest(Chest chest);

    void update(ItemStack armour);

    ItemStack generateBoots();

    ItemStack generateLeggings(ArmourTier armourTier, List<String> lore, String name);

    ItemStack generateChestPlate(ArmourTier armourTier, List<String> lore, String name);

    ItemStack generateHelmet(ArmourTier armourTier, List<String> lore, String name);

}
