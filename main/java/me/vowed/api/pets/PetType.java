package me.vowed.api.pets;

import me.vowed.api.pets.petTypes.EntityWolfPet;
import me.vowed.api.pets.petTypes.PetWolf;
import me.vowed.api.pets.player.PetPlayer;
import me.vowed.api.plugin.Vowed;
import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.entity.EntityType;

import java.lang.reflect.Constructor;

/**
 * Created by JPaul on 11/22/2015.
 */
public enum PetType {
    Wolf(EntityType.WOLF, "Wolf", EntityWolfPet.class, PetWolf.class),
    Zombie(EntityType.ZOMBIE, "Zombie", EntityWolfPet.class, PetWolf.class);

    private EntityType bukkitType;
    private String name;
    private Class<? extends EntityPet> entityClass;
    private Class<? extends Pet> myPetClass;

    PetType(EntityType bukkitType, String typeName, Class<? extends EntityPet> entityClass, Class<? extends Pet> myPetClass) {
        this.bukkitType = bukkitType;
        this.name = typeName;
        this.entityClass = entityClass;
        this.myPetClass = myPetClass;
    }

    public Class<? extends EntityPet> getEntityClass() {
        return entityClass;
    }

    public EntityType getEntityType() {
        return bukkitType;
    }

    public Class<? extends Pet> getMyPetClass() {
        return myPetClass;
    }

    public static PetType getMyPetTypeByEntityClass(Class<? extends EntityCreature> entityClass) {
        for (PetType myPetType : PetType.values()) {
            if (myPetType.entityClass == entityClass) {
                return myPetType;
            }
        }
        return null;
    }

    public static PetType getMyPetTypeByEntityType(EntityType type) {
        for (PetType myPetType : PetType.values()) {
            if (myPetType.bukkitType == type) {
                return myPetType;
            }
        }
        return null;
    }

    public static PetType getMyPetTypeByName(String name) {
        for (PetType myPetType : PetType.values()) {
            if (myPetType.name.equalsIgnoreCase(name)) {
                return myPetType;
            }
        }
        return null;
    }

    //copied from echopet/mypet
    public EntityPet getNewEntityInstance(World world, Pet myPet) {
        EntityPet petEntity = null;

        try {
            Constructor<?> ctor = entityClass.getConstructor(World.class, Pet.class);
            Object obj = ctor.newInstance(world, myPet);
            if (obj instanceof EntityPet) {
                petEntity = (EntityPet) obj;
            }
        } catch (Exception e) {
            Vowed.LOG.warning(entityClass.getName() + " is not a valid pet");
            e.printStackTrace();
        }
        return petEntity;
    }

    public Pet getNewMyPetInstance(PetPlayer owner) {
        Pet pet = null;

        try {
            Constructor<?> ctor = myPetClass.getConstructor(PetPlayer.class);
            Object obj = ctor.newInstance(owner);
            if (obj instanceof Pet) {
                pet = (Pet) obj;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Vowed.LOG.warning(myPetClass.getName() + " is no valid MyPet!");
        }
        return pet;
    }

    public String getTypeName() {
        return name;
    }

    public static boolean isLeashableEntityType(EntityType type) {
        for (PetType myPetType : PetType.values()) {
            if (myPetType.bukkitType == type) {
                return true;
            }
        }
        return false;
    }
}
