package me.vowed.api.damage;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import java.util.UUID;

/**
 * Created by JPaul on 10/2/2015.
 */
public interface IDamage
{
    Entity getAttacker();

    Entity getTarget();

    double getDamage();

    DamageCause getDamageCause();

    boolean isImmune(Entity entity);

    Vector getKnockback();

    void setAttacker(Entity entity);

    void setTarget(Entity entity);

    void setDamage(double damage);

    void setDamageCause(DamageCause damageCause);

    void setImmune(Entity immune, Boolean b);

    void setKnockback(Vector knockback);

    IDamage createDamage(double damage, DamageCause damageCause, Vector knockback, LivingEntity attacker, LivingEntity target);

    IDamage createDamage(double damage, DamageCause damageCause, Vector knockback, LivingEntity target);
}
