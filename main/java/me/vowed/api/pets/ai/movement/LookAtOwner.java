package me.vowed.api.pets.ai.movement;

import me.vowed.api.pets.petTypes.EntityWolfPet;
import me.vowed.api.plugin.Vowed;
import net.minecraft.server.v1_8_R3.PathfinderGoal;
import org.bukkit.entity.Player;

import java.util.Random;

/**
 * Created by JPaul on 11/22/2015.
 */
public class LookAtOwner extends PathfinderGoal
{

    private EntityWolfPet pet;
    private float range;
    private int ticksLeft;
    private float chance;
    private Player player;
    private Random random = new Random();

    public LookAtOwner(EntityWolfPet pet, Player player) {
        this.pet = pet;
        this.range = 8.0F;
        this.chance = 0.02F;
        this.player = player;
    }

    public LookAtOwner(EntityWolfPet pet, Player player, float f, float f1) {
        this.pet = pet;
        this.range = f;
        this.chance = f1;
        this.player = player;
    }

    @Override
    public boolean a() {
        return true;
    }

    @Override
    public void d() {
        this.player = null;
    }

    @Override
    public void e() {
        Vowed.LOG.warning(String.valueOf(pet.yaw));
        Vowed.LOG.info(String.valueOf(pet.pitch));
        this.pet.getControllerLook().a(this.player.getLocation().getX(), this.player.getLocation().getY() + this.player.getEyeHeight(), this.player.getLocation().getZ(), 10.0F, (float) this.pet.bQ());
    }
}
