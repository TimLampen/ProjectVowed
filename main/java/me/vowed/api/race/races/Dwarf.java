package me.vowed.api.race.races;

import me.vowed.api.money.currency.CurrencyType;
import me.vowed.api.plugin.Vowed;
import me.vowed.api.race.Race;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by JPaul on 11/14/2015.
 */
public class Dwarf extends Race
{
    String name;
    Gender gender;

    List<UUID> dwarfSkins = Arrays.asList(UUID.fromString("df5d1a6f-b8c7-4f95-bba8-b0dad83ed796"), UUID.fromString("3f78e36b-3dab-43d9-8daf-c416318fbe85"));

    public Dwarf(Gender gender)
    {
        super(RaceType.DWARF, gender);
        this.name = "Dwarf";
        this.gender = gender;
    }

    public List<UUID> getDwarfSkins()
    {
        return dwarfSkins;
    }

    public UUID getRandomSkin()
    {
        int index = ThreadLocalRandom.current().nextInt(dwarfSkins.size());
        Vowed.LOG.info(String.valueOf(index));
        return dwarfSkins.get(index);
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public UUID getSkin()
    {
        if (this.gender == Gender.MALE)
        {
            return dwarfSkins.get(0);
        }
        else if (this.gender == Gender.FEMALE)
        {
            return dwarfSkins.get(1);
        }

        return null;
    }
}
