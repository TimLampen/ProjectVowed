package me.vowed.api.damage;

import me.vowed.api.plugin.Vowed;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by JPaul on 10/2/2015.
 */
public class Damage implements IDamage
{
    private static DamageCause damageCause;
    private static Entity attacker;
    private static Entity target;
    private static double damage;
    private static Vector knockback;
    private static HashMap<UUID, Boolean> isImmune = new HashMap<>();

    public Damage(double damageINT, DamageCause damageCause, Vector knockback, Entity attacker, Entity target)
    {
        damage = damageINT;
        Damage.damageCause = damageCause;
        this.setKnockback(knockback);
        this.setAttacker(attacker);
        this.setTarget(target);
    }

    public Damage(double damageINT, DamageCause damageCause, Vector knockback, Entity target)
    {
        this.setDamage(damageINT);
        this.setDamageCause(damageCause);
        this.setKnockback(knockback);
        this.setTarget(target);
    }

    public Damage(double damageINT, DamageCause damageCause, Entity attacker, Entity target)
    {
        this.setDamage(damageINT);
        this.setDamageCause(damageCause);
        this.setAttacker(attacker);
        this.setTarget(target);
    }

    public Damage()
    {
    }

    public void start()
    {

        if (!isImmune(getTarget()))
        {
            Vowed.getHealthManager().getHealth((Player) getTarget()).convertHealth();
        }
    }


    @Override
    public Entity getAttacker()
    {
        return attacker;
    }

    @Override
    public Entity getTarget()
    {
        return target;
    }

    @Override
    public double getDamage()
    {
        return damage;
    }

    @Override
    public DamageCause getDamageCause()
    {
        return damageCause;
    }

    @Override
    public Vector getKnockback()
    {
        return knockback;
    }

    @Override
    public void setKnockback(Vector knockback)
    {
        Damage.knockback = knockback;
    }

    @Override
    public void setAttacker(Entity entity)
    {
        attacker = entity;
    }

    @Override
    public void setTarget(Entity entity)
    {
        target = entity;
    }

    @Override
    public void setDamage(double damageINT)
    {
        damage = damageINT;
    }

    @Override
    public void setDamageCause(DamageCause damageCause)
    {
        Damage.damageCause = damageCause;
    }

    @Override
    public void setImmune(Entity immune, Boolean b)
    {
        isImmune.put(immune.getUniqueId(), b);
    }

    @Override
    public boolean isImmune(Entity entity)
    {
        return isImmune.get(entity.getUniqueId());
    }

    @Override
    public IDamage createDamage(double damageINT, DamageCause damageCause, Vector knockback, LivingEntity attacker, LivingEntity target)
    {
        return new Damage(damageINT, damageCause, knockback, attacker, target);
    }

    @Override
    public IDamage createDamage(double damageINT, DamageCause damageCause, Vector knockback, LivingEntity target)
    {
        return new Damage(damageINT, damageCause, knockback, target);
    }
}
