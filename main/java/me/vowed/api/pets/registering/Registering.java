package me.vowed.api.pets.registering;

/**
 * Created by Nitsua on 8/3/2015.
 */

import me.vowed.api.pets.petTypes.EntityWolfPet;
import me.vowed.api.pets.petTypes.Wolf;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.entity.EntityType;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public enum Registering
{

    WOLFPET1("Wolf", 95, EntityType.WOLF, net.minecraft.server.v1_8_R3.EntityWolf.class, Wolf.class);

    private String name;
    private int id;
    private EntityType entityType;
    private Class<? extends EntityInsentient> nmsClass;
    private Class<? extends EntityInsentient> customClass;

    private Registering(String name, int id, EntityType entityType, Class<? extends EntityInsentient> nmsClass,
                        Class<? extends EntityInsentient> customClass)
    {
        this.name = name;
        this.id = id;
        this.entityType = entityType;
        this.nmsClass = nmsClass;
        this.customClass = customClass;
    }

    public String getName()
    {
        return name;
    }

    public int getID()
    {
        return id;
    }

    public EntityType getEntityType()
    {
        return entityType;
    }

    public Class<? extends EntityInsentient> getNMSClass()
    {
        return nmsClass;
    }

    public Class<? extends EntityInsentient> getCustomClass()
    {
        return customClass;
    }

    public static void registerEntities()
    {
        for (Registering entity : values())
            a(entity.getCustomClass(), entity.getName(), entity.getID());

        BiomeBase[] biomes;
        try
        {
            biomes = (BiomeBase[]) getPrivateStatic(BiomeBase.class, "biomes");
        } catch (Exception exc)
        {
            return;
        }
        for (BiomeBase biomeBase : biomes)
        {
            if (biomeBase == null)
                break;

            for (String field : new String[]{"at", "au", "av", "aw"})
                try
                {
                    Field list = BiomeBase.class.getDeclaredField(field);
                    list.setAccessible(true);
                    @SuppressWarnings("unchecked")
                    List<BiomeBase.BiomeMeta> mobList = (List<BiomeBase.BiomeMeta>) list.get(biomeBase);

                    for (BiomeBase.BiomeMeta meta : mobList)
                        for (Registering entity : values())
                            if (entity.getNMSClass().equals(meta.b))
                                meta.b = entity.getCustomClass();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
        }
    }

    @SuppressWarnings("rawtypes")
    private static Object getPrivateStatic(Class clazz, String f) throws Exception
    {
        Field field = clazz.getDeclaredField(f);
        field.setAccessible(true);
        return field.get(null);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void a(Class paramClass, String paramString, int paramInt)
    {
        try
        {
            ((Map) getPrivateStatic(EntityTypes.class, "c")).put(paramString, paramClass);
            ((Map) getPrivateStatic(EntityTypes.class, "d")).put(paramClass, paramString);
            ((Map) getPrivateStatic(EntityTypes.class, "f")).put(paramClass, Integer.valueOf(paramInt));
            ((Map) getPrivateStatic(EntityTypes.class, "g")).put(paramString, Integer.valueOf(paramInt));
        } catch (Exception exc)
        {
        }
    }
}
