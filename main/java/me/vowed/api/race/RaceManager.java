package me.vowed.api.race;

import me.vowed.api.plugin.Vowed;
import me.vowed.api.race.races.Dwarf;
import me.vowed.api.race.races.Elf;
import me.vowed.api.race.races.Gender;
import me.vowed.api.race.races.Human;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;
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
        switch (string.toUpperCase())
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

            try
            {
                PreparedStatement setRace = Vowed.getDatabase().prepareStatement("INSERT INTO player_info (UUID, race, gender) " +
                    "VALUES ('" + player.getUniqueId().toString() + "', '" + race.getName().toLowerCase() + "', '" + race.getGender().toString().toUpperCase() + "') " +
                    "ON DUPLICATE KEY UPDATE race = '" + race.getName() + "', gender = '" + race.getGender() + "'");
                Vowed.LOG.debug(setRace.toString());
                setRace.executeUpdate();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
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
