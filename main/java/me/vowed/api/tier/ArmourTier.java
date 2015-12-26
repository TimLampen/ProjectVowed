package me.vowed.api.tier;


import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.HashMap;

/**
 * Created by JPaul on 10/4/2015.
 */
public enum ArmourTier
{
    TIER1(Color.LIME),
    TIER2(Color.BLACK),
    TIER3(Color.RED),
    TIER4(Color.AQUA),
    TIER5(Color.FUCHSIA);

    private Color colour;

    ArmourTier(Color colour)
    {
        this.colour = colour;
    }

    public Color getColour()
    {
        return this.colour;
    }
}
