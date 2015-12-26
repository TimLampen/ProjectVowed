package me.vowed.api.race.races;

import me.vowed.api.money.currency.CurrencyType;
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
public class Elf extends Race
{
    String name;
    Gender gender;

    List<UUID> elfSkins = Arrays.asList(UUID.fromString("62c4bc6f-7afb-45cc-97ba-d3dc7852a59c"), UUID.fromString("62c4bc6f-7afb-45cc-97ba-d3dc7852a59c"));

    public Elf(Gender gender)
    {
        super(RaceType.ELF, gender);
        this.name = "Elf";
        this.gender = gender;
    }

    public List<UUID> getElfSkins()
    {
        return elfSkins;
    }

    public UUID getRandomSkin()
    {
        int index = ThreadLocalRandom.current().nextInt(elfSkins.size());
        return elfSkins.get(index);
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    public UUID getSkin()
    {
        if (this.gender == Gender.MALE)
        {
            return elfSkins.get(0);
        }
        else if (this.gender == Gender.FEMALE)
        {
            return elfSkins.get(1);
        }

        return null;
    }
}
