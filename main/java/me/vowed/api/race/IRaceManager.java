package me.vowed.api.race;

import me.vowed.api.race.races.Gender;
import org.bukkit.entity.Player;

/**
 * Created by JPaul on 11/14/2015.
 */
public interface IRaceManager
{
    Race getRace(Player player);

    Race getRace(String string, Gender gender);

    void setRace(Player player, Race race);

    void changeRace(Player player, Race race);

    Race getRandomRace(Gender gender);
}
