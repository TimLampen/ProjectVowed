package me.vowed.api.pets.ai.movement;

import me.vowed.api.pets.EntityPet;
import me.vowed.api.pets.ai.pathfinding.astarmain.PathingResult;
import me.vowed.api.pets.ai.pathfinding.astarmain.AStar;
import me.vowed.api.pets.ai.pathfinding.astarmain.Tile;
import me.vowed.api.pets.petTypes.EntityWolfPet;
import me.vowed.api.pets.petTypes.Wolf;
import me.vowed.api.plugin.Vowed;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nitsua on 8/4/2015.
 */
public class FollowOwner extends PathfinderGoal
{
    private double speed;
    private double stopDistance;
    private double startDistance;
    private double teleportDistance;
    private int setPathTimer = 0;
    private Wolf petEntity;
    private Player owner;
    private NavigationAbstract navigation;
    AStar aStarPath;
    boolean isDone;

    public static Location ownerLocation;
    public static int xINT;
    public static int y;
    public static int z;

    HashMap<Wolf, AStar> hashMap = new HashMap<>();
    HashMap<EntityWolfPet, Location> locationComparer = new HashMap<>();
    ArrayList<Tile> route;
    Location location;
    int counter;


    public FollowOwner(Wolf petEntity, double stopDistance, double startDistance, double teleportDistance)
    {
        this.petEntity = petEntity;
        this.stopDistance = stopDistance * stopDistance;
        this.startDistance = startDistance * startDistance;
        this.teleportDistance = teleportDistance * teleportDistance;
        this.navigation = petEntity.getNavigation();
        this.owner = petEntity.getPlayerOwner();
    }

    @Override
    public boolean a()
    {
        if (!this.petEntity.canMove())
        {
            return false;
        } else if (this.petEntity.getGoalTarget() != null && this.petEntity.getGoalTarget().isAlive())
        {
            return false;
        } else if (this.petEntity.getPlayerOwner() == null)
        {
            return false;
        } else if (this.petEntity.h(((CraftPlayer) owner.getPlayer()).getHandle()) <= this.startDistance)
        {
            return false;
        }

        return true;
    }

    @Override
    public boolean b()
    {
        if (this.petEntity.getPlayerOwner() == null)
        {
            return false;
        } else if (this.petEntity.h(((CraftPlayer) owner.getPlayer()).getHandle()) <= this.stopDistance)
        {
            return false;
        } else if (!this.petEntity.canMove())
        {
            return false;
        } else if (this.petEntity.getGoalTarget() != null && this.petEntity.getGoalTarget().isAlive())
        {
            return false;
        }

        return true;
    }

    @Override
    public void c()
    {
        this.setPathTimer = 0;
    }

    @Override
    public void d()
    {
        Vowed.LOG.severe("STOPPED");
        this.navigation.n();
        this.hashMap.remove(petEntity);
    }

    @Override
    public void e()
    {
        try
        {
            Field JUMP_FIELD = getField(EntityLiving.class, "aY");
            JUMP_FIELD.setAccessible(true);

            if (JUMP_FIELD.getBoolean(this.petEntity))
            {
                JUMP_FIELD.set(this.petEntity, false);
            }
        } catch (NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

        if (location != null)
        {
            this.petEntity.getControllerLook().a(((CraftPlayer) owner).getHandle(), 10.0F, this.petEntity.bQ());
        }

        if (!hashMap.containsKey(this.petEntity))
        {
            runPathing(petEntity.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation(), owner.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation(), 100);
        } else if (this.hashMap.get(this.petEntity) == null)
        {
            runPathing(petEntity.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation(), owner.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation(), 100);
        }
    }

    public void runPathing(final Location start, final Location end, final int range)
    {
        //create our pathfinder
        try
        {
            aStarPath = new AStar(start, end, range);

            route = aStarPath.iterate();

            PathingResult pathingResult = aStarPath.getPathingResult();

            switch (pathingResult)
            {
                case SUCCESS:

                    hashMap.put(this.petEntity, aStarPath);
                    changePathBlocksToDiamond(start, route);
                    break;

                case NO_PATH:
                    //No path found, throw error.
                    System.out.println("No path found!");
                    break;
            }

            Vowed.LOG.info("-----------------------");
            Vowed.LOG.info(String.valueOf(xINT + " " + y + " " + z));
            Vowed.LOG.info(end.toString());
            Vowed.LOG.info("-----------------------");
        } catch (AStar.InvalidPathException e)
        {

            if (e.isStartNotSolid())
            {
                System.out.println("End block is not walkable");
            }
            if (e.isStartNotSolid())
            {
                System.out.println("Start block is not walkable");
            }
        }


        //get the list of nodes to walk to as a Tile object
        //get the result of the path trace

    }

    private void changePathBlocksToDiamond(Location start, ArrayList<Tile> route)
    {

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (counter >= route.size() || !petEntity.isAlive() || route.isEmpty())
                {
                    hashMap.remove(petEntity);

                    counter = 0;
                    route.clear();
                }

                if (!route.isEmpty() && counter <= 2)
                {
                    location = route.get(counter).getLocation(start);
                    Vowed.LOG.debug(navigation.toString());
                    petEntity.getNavigation().a(route.get(counter).getLocation(start).getBlockX(), route.get(counter).getLocation(start).getBlockX(), route.get(counter).getLocation(start).getBlockX());
                    if (petEntity.getNavigation() != null)
                    {
                        Vowed.LOG.debug("ok");
                    }
                    counter++;

                    for (Tile tile : route)
                    {
                        Location location = tile.getLocation(start);
                        location.getBlock().setType(Material.MOSSY_COBBLESTONE);
                    }
                }

                if (!route.isEmpty())
                {
                    if (counter > 2)
                    {
                        if (counter >= 0 && counter < route.size())
                        {
                            location = route.get(counter).getLocation(start);

                            if (Math.abs(petEntity.getLocation().getX() - route.get(counter).getLocation(start).getX()) <= 2 &&
                                    Math.abs(petEntity.getLocation().getY() - route.get(counter).getLocation(start).getY()) <= 3 &&
                                    Math.abs(petEntity.getLocation().getZ() - route.get(counter).getLocation(start).getZ()) <= 2)
                            {
                                petEntity.getControllerMove().a(route.get(counter).getLocation(start).getBlockX() + 0.5, route.get(counter).getLocation(start).getBlockY(), route.get(counter).getLocation(start).getBlockZ() + 0.5, 2);

                                Vowed.LOG.warning(String.valueOf(petEntity.getLocation().getBlockX() + " " + petEntity.getLocation().getBlockY() + " " + petEntity.getLocation().getBlockZ() + " ------ " +
                                        route.get(counter).getLocation(start).getX() + " " + route.get(counter).getLocation(start).getBlockY() + " " +
                                        route.get(counter).getLocation(start).getZ()));
                                counter++;

                            } else
                            {
                                Vowed.LOG.info(String.valueOf(petEntity.getLocation().getBlockX() + " " + petEntity.getLocation().getBlockY() + " " +
                                        petEntity.getLocation().getBlockZ() + " ------ " +
                                        route.get(counter).getLocation(start).getX() + " " + route.get(counter).getLocation(start).getBlockY() + " " +
                                        route.get(counter).getLocation(start).getZ()));

                                int x = (int) Math.abs(petEntity.getLocation().getX() - route.get(counter).getLocation(start).getX());
                                int z = (int) Math.abs(petEntity.getLocation().getZ() - route.get(counter).getLocation(start).getZ());
                                int y = (int) Math.abs(petEntity.getLocation().getY() - route.get(counter).getLocation(start).getY());
                                Vowed.LOG.severe(x + " " + y + " " + z);

                                if (y > 3)
                                {
                                    navigation.a(route.get(counter).getLocation(start).getBlockX() + 0.5, route.get(counter).getLocation(start).getBlockY(), route.get(counter).getLocation(start).getBlockZ() + 0.5);
                                } else
                                {
                                    navigation.a(route.get(counter).getLocation(start).getBlockX() + 0.5, route.get(counter).getLocation(start).getBlockY(), route.get(counter).getLocation(start).getBlockZ() + 0.5);
                                }
                            }
                        }
                    }
                }
                Vowed.LOG.severe(counter + "   " + route.size());
            }
        }.runTaskTimer(Vowed.getPlugin(), 0, 6);
    }

    private static Field getField(Class clazz, String fieldName)
            throws NoSuchFieldException
    {
        try
        {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e)
        {
            Class superClass = clazz.getSuperclass();
            if (superClass == null)
            {
                throw e;
            } else
            {
                return getField(superClass, fieldName);
            }
        }
    }
}


