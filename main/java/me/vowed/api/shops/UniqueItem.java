package me.vowed.api.shops;

import me.vowed.api.plugin.Vowed;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.*;

/**
 * Created by JPaul on 11/14/2015.
 */
public class UniqueItem
{
    private String UUID;
    private ItemStack is;

    public UniqueItem(ItemStack is, String UUID) {
        this.is = is;
        this.UUID = UUID;
    }

    public String getUUID() {
        return this.UUID;
    }

    public ItemStack getItemStack() {
        return this.is;
    }
}
