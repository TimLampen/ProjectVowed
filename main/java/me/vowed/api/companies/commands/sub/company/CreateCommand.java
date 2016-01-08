package me.vowed.api.companies.commands.sub.company;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import me.vowed.api.companies.Company;
import me.vowed.api.companies.CompanyListener;
import me.vowed.api.companies.commands.sub.SubCommand;
import me.vowed.api.companies.types.CompanyType;
import me.vowed.api.plugin.Vowed;
import me.vowed.api.utils.bukkit.BukkitUtil;
import me.vowed.api.utils.worldedit.WorldEditUtil;
import me.vowed.api.utils.worldguard.WorldGuardUtil;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by JPaul on 1/2/2016.
 */
public class CreateCommand extends SubCommand
{
    public CreateCommand(String label)
    {
        super(label);
    }

    @Override
    public int getMinimumArguments()
    {
        return 2;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args)
    {
        WorldEditPlugin worldEdit = WorldEditUtil.getWorldEdit();
        WorldGuardPlugin worldGuard = WorldGuardUtil.getWorldGuard();
        assert worldGuard != null;
        assert worldEdit != null;

        Selection selection = worldEdit.getSelection((Player) commandSender);

        Location minimumPoint = selection.getMinimumPoint();
        Location maximumPoint = selection.getMaximumPoint();

        BlockVector minimumBlockVector = new BlockVector(minimumPoint.getBlockX(), minimumPoint.getBlockY(), minimumPoint.getBlockZ());
        BlockVector maximumBlockVector = new BlockVector(maximumPoint.getBlockX(), maximumPoint.getBlockY(), maximumPoint.getBlockZ());

        final ProtectedCuboidRegion cuboidRegion = new ProtectedCuboidRegion("company_" + args[2], minimumBlockVector, maximumBlockVector);
        worldGuard.getRegionManager(((Player) commandSender).getWorld()).addRegion(cuboidRegion);
        cuboidRegion.getOwners().addPlayer(((Player) commandSender).getUniqueId());

        final int[] centerXZ = WorldGuardUtil.getCenter(cuboidRegion);
        Location center = new Location(((Player) commandSender).getWorld(), centerXZ[0], cuboidRegion.getMinimumPoint().getBlockY(), centerXZ[1]);

        Vowed.LOG.warning(center.toString());

        final Company company = Vowed.getCompanyManager().getInstance(args[2], CompanyType.valueOf(args[3].toUpperCase()), (Player) commandSender, center, cuboidRegion);

        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, company.getOwner().getName() + "'s Builder");

        npc.spawn(center);

        BukkitUtil.sendCommand("npc select " + npc.getId());
        BukkitUtil.sendCommand("trait builder");
        BukkitUtil.sendCommand("builder timeout 0");

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                switch (company.getType())
                {
                    case ARMOURY:
                        BukkitUtil.sendCommand("builder load Armoury");
                        break;

                    case FOOD:
                        BukkitUtil.sendCommand("builder load Food");
                }
                BukkitUtil.sendCommand("builder origin " + centerXZ[0] + "," + cuboidRegion.getMinimumPoint().getBlockY() + "," + centerXZ[1]);
                BukkitUtil.sendCommand("builder build excavate");
            }
        }.runTaskLater(Vowed.getPlugin(), 60);

        return true;
    }
}
