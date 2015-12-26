package me.vowed.api.pets.util;

import me.vowed.api.pets.EntityPet;
import me.vowed.api.plugin.Vowed;
import net.minecraft.server.v1_8_R3.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by JPaul on 11/29/2015.
 */
public class BukkitUtil
{
    @SuppressWarnings("unchecked")
    //mypets way of registering (thought it would work, but the entities are just invisible)
    public static boolean registerPetEntity(Class<? extends EntityPet> myPetEntityClass, String entityTypeName, int entityTypeId)
    {
        try
        {
            Field EntityTypes_d = EntityTypes.class.getDeclaredField("d");
            Field EntityTypes_f = EntityTypes.class.getDeclaredField("f");
            EntityTypes_d.setAccessible(true);
            EntityTypes_f.setAccessible(true);

            Map<Class, String> d = (Map) EntityTypes_d.get(EntityTypes_d);
            Map<Class, Integer> f = (Map) EntityTypes_f.get(EntityTypes_f);

            Iterator cIterator = d.keySet().iterator();
            while (cIterator.hasNext())
            {
                Class clazz = (Class) cIterator.next();
                if (clazz.getCanonicalName().equals(myPetEntityClass.getCanonicalName()))
                {
                    cIterator.remove();
                }
            }

            Iterator eIterator = f.keySet().iterator();
            while (eIterator.hasNext())
            {
                Class clazz = (Class) eIterator.next();
                if (clazz.getCanonicalName().equals(myPetEntityClass.getCanonicalName()))
                {
                    eIterator.remove();
                }
            }

            d.put(myPetEntityClass, entityTypeName);
            f.put(myPetEntityClass, entityTypeId);

            Vowed.LOG.debug(d.get(myPetEntityClass));
            Vowed.LOG.debug(f.get(myPetEntityClass));

            return true;
        } catch (Exception e)
        {
            Vowed.LOG.severe("error while registering " + myPetEntityClass.getCanonicalName());
            Vowed.LOG.severe(e.getMessage());
            return false;
        }
    }
    public static void registerEntity1(String name, int id, Class<?> test) {
        try {
            Class<?> class1 = (Class<?>) EntityTypes.class.getDeclaredMethod("a", int.class).invoke(null, id);
            List<Map<?, ?>> dataMaps = new ArrayList<Map<?, ?>>();
            for (Field f : EntityTypes.class.getDeclaredFields()) {
                if (f.getType().getSimpleName().equals(Map.class.getSimpleName())) {
                    f.setAccessible(true);
                    dataMaps.add((Map<?, ?>) f.get(null));
                }
            }
            if (dataMaps.get(2).containsKey(id)) {
                dataMaps.get(0).remove(name);
                dataMaps.get(2).remove(id);
            }
            Method method = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
            method.setAccessible(true);
            method.invoke(null, test, name, id);
            Class<?> biomebase = BiomeBase.class;
            Class<?> biomemeta;
            try{
                biomemeta = BiomeBase.BiomeMeta.class;
            }catch(Exception e){
                biomemeta = BiomeBase.BiomeMeta.class;
            }
            for (Field f : biomebase.getDeclaredFields()) {
                if (f.getType().getSimpleName().equals(biomebase.getSimpleName())) {
                    if (f.get(null) != null) {
                        for (Field list : biomebase.getDeclaredFields()) {
                            if (list.getType().getSimpleName().equals(List.class.getSimpleName())) {
                                list.setAccessible(true);
                                List<?> metaList = (List<?>) list.get(f.get(null));
                                for (Object meta : metaList) {
                                    Field clazz = biomemeta.cast(meta).getClass().getDeclaredFields()[0];
                                    if (clazz.get(meta).equals(class1)) {
                                        clazz.set(meta, test);
                                    }
                                }
                            }
                        }

                    }
                }
            }
            Vowed.LOG.debug("Overridden entity " + name + " with " + test.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
