package me.vowed.api.tier;


import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by JPaul on 10/6/2015.
 */
public class WeaponTier implements IWeaponTier
{

    @Override
    public int getWeaponTier(ItemStack weapon)
    {
        switch (weapon.getType())
        {
            case WOOD_SWORD:
            case WOOD_HOE:
            case WOOD_AXE:
                return 1;

            case STONE_SWORD:
            case STONE_HOE:
            case STONE_AXE:
                return 2;

            case IRON_SWORD:
            case IRON_HOE:
            case IRON_AXE:
                return 3;

            case DIAMOND_SWORD:
            case DIAMOND_HOE:
            case DIAMOND_AXE:
                return 4;

            case GOLD_SWORD:
            case GOLD_HOE:
            case GOLD_AXE:
                return 5;
        }
        return 0;
    }
}
