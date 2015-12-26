package me.vowed.api.pets.player;

import java.util.UUID;

/**
 * Created by JPaul on 11/30/2015.
 */
public class OnlinePetPlayer extends PetPlayer
{

    public OnlinePetPlayer(UUID playerUUID)
    {
        super(playerUUID);
    }

    @Override
    public String toString()
    {
        return "ONLINE" + super.toString();
    }
}
