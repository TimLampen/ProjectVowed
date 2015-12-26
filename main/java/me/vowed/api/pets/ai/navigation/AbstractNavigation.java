package me.vowed.api.pets.ai.navigation;

import me.vowed.api.pets.petTypes.EntityWolfPet;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

/**
 * Created by JPaul on 11/23/2015.
 */
public abstract class AbstractNavigation {
    protected EntityWolfPet entityPet;
    NavigationParameters parameters;

    public abstract void stop();

    public abstract boolean navigateTo(double x, double y, double z);

    public abstract void applyNavigationParameters();

    public AbstractNavigation(EntityWolfPet entityPet, double speed) {
        this.entityPet = entityPet;
        parameters = new NavigationParameters(speed);
    }

    public AbstractNavigation(EntityWolfPet entityMyPet, NavigationParameters parameters) {
        this.entityPet = entityMyPet;
        this.parameters = parameters;
    }

    public boolean navigateTo(Location loc) {
        return navigateTo(loc.getX(), loc.getY(), loc.getZ());
    }

    public boolean navigateTo(LivingEntity entity) {
        return navigateTo(entity.getLocation());
    }

    public boolean navigateTo(EntityLiving entity) {
        return navigateTo((LivingEntity) entity.getBukkitEntity());
    }

    public NavigationParameters getParameters() {
        return parameters;
    }

    public abstract void tick();
}
