package me.vowed.api.race;

import me.vowed.api.race.races.Gender;
import me.vowed.api.race.races.RaceType;

import java.util.UUID;

/**
 * Created by JPaul on 11/14/2015.
 */
public abstract class Race
{
    Gender gender;
    RaceType type;

    public Race(RaceType raceType, Gender gender)
    {
        this.type = raceType;
        this.gender = gender;
    }

    public abstract String getName();

    public abstract UUID getSkin();

    public RaceType getType()
    {
        return type;
    }

    public Gender getGender()
    {
        return this.gender;
    }
}
