package me.vowed.api.pets;

import me.vowed.api.pets.player.PetPlayer;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by JPaul on 11/29/2015.
 */
public class PetList
{
    public static HashMap<PetPlayer, Pet> pets = new HashMap<>();

    public static Collection<Pet> getPets()
    {
        return pets.values();
    }

    public static Pet getPet(PetPlayer owner)
    {
        return pets.get(owner);
    }

    public static void addPet(PetPlayer owner, Pet pet)
    {
        pets.put(owner, pet);
    }

    public static void setPets(HashMap<PetPlayer, Pet> pets)
    {
        PetList.pets = pets;
    }
}
