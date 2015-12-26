package me.vowed.api.pets.petTypes;

import me.vowed.api.pets.ai.movement.FollowOwner;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by JPaul on 12/1/2015.
 */
public class Wolf extends EntityWolf
{
    Player owner;

    public Wolf(World world, Player player)
    {
        super(world);
        this.owner = player;

        List goalB = (List) getPrivateField("b", PathfinderGoalSelector.class, goalSelector);
        goalB.clear();
        List goalC = (List) getPrivateField("c", PathfinderGoalSelector.class, goalSelector);
        goalC.clear();
        List targetB = (List) getPrivateField("b", PathfinderGoalSelector.class, targetSelector);
        targetB.clear();
        List targetC = (List) getPrivateField("c", PathfinderGoalSelector.class, targetSelector);
        targetC.clear();

        this.goalSelector.a(1, new FollowOwner(this, 2, 3, 100));


    }

    public static Object getPrivateField(String fieldName, Class clazz, Object object)
    {
        Field field;
        Object o = null;

        try
        {
            field = clazz.getDeclaredField(fieldName);

            field.setAccessible(true);

            o = field.get(object);
        } catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }

        return o;
    }

    public Player getPlayerOwner()
    {
        return this.owner;
    }

    public boolean canMove()
    {
        return true;
    }

    public Location getLocation()
    {
        return getBukkitEntity().getLocation();
    }

    @Override
    public void g(float sideMot, float forMot)
    {

        this.S = 1.5F;
        super.g(sideMot, forMot);
    }

    @Override
    protected void initAttributes()
    {
        super.initAttributes();
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(64.0D); // Original 3.0D
    }
}

