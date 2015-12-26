package me.vowed.api.race;

import me.vowed.api.plugin.Vowed;
import me.vowed.api.race.races.Dwarf;
import me.vowed.api.race.races.Elf;
import me.vowed.api.race.races.Gender;
import me.vowed.api.race.races.Human;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by JPaul on 11/14/2015.
 */
public class RaceManager implements IRaceManager
{
    private HashMap<UUID, Race> races = new HashMap<>();

    @Override
    public Race getRace(Player player)
    {
        Vowed.LOG.info(races.toString());
        return races.get(player.getUniqueId());
    }

    @Override
    public Race getRace(String string, Gender gender)
    {
        switch (string)
        {
            case "ELF":
                return new Elf(gender);
            case "DWARF":
                return new Dwarf(gender);
            case "HUMAN":
                return new Human(gender);
        }

        return null;
    }

    @Override
    public void setRace(Player player, Race race)
    {
        if (!this.races.containsKey(player.getUniqueId()))
        {
            this.races.put(player.getUniqueId(), race);
        }
    }

    @Override
    public void changeRace(Player player, Race race)
    {
        this.races.put(player.getUniqueId(), race);
    }

    @Override
    public Race getRandomRace(Gender gender)
    {
        switch (ThreadLocalRandom.current().nextInt(races.size()))
        {
            case 0:
                return new Dwarf(gender);

            case 1:
                return new Elf(gender);
        }
        return null;
    }
}
