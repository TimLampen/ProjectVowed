package me.vowed.api.pets.ai.navigation;

/**
 * Created by JPaul on 11/23/2015.
 */
import me.vowed.api.pets.petTypes.EntityWolfPet;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.Navigation;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

public class VanillaNavigation extends AbstractNavigation {
    Navigation nav;

    public VanillaNavigation(EntityWolfPet entityPet, double speed) {
        super(entityPet, speed);
        nav = (Navigation) entityPet.getNavigation();
    }

    public VanillaNavigation(EntityWolfPet entityPet, NavigationParameters parameters) {
        super(entityPet, parameters);
        nav = (Navigation) entityPet.getNavigation();
    }

    @Override
    public void stop() {
        nav.n();
    }

    @Override
    public boolean navigateTo(double x, double y, double z) {
        applyNavigationParameters();
        if (this.nav.a(x, y, z, 1.D)) {
            applyNavigationParameters();
            return true;
        }
        return false;
    }

    @Override
    public boolean navigateTo(LivingEntity entity) {
        return navigateTo(((CraftLivingEntity) entity).getHandle());
    }

    @Override
    public boolean navigateTo(EntityLiving entity) {
        if (this.nav.a(entity, 1.D)) {
            applyNavigationParameters();
            return true;
        }
        return false;
    }

    @Override
    public void tick() {
        nav.k();
    }

    public void applyNavigationParameters() {
        this.nav.a(parameters.avoidWater());
        this.entityPet.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(parameters.speed() + parameters.speedModifier());
    }
}
