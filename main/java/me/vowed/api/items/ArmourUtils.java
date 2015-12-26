package me.vowed.api.items;

import java.util.UUID;

/**
 * Created by JPaul on 11/11/2015.
 */
public class ArmourUtils
{
    public static IArmour getArmourByUUID(UUID uuid)
    {
        return Armour.UUIDLocator.get(uuid);
    }
}
