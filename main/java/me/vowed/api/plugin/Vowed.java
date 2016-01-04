package me.vowed.api.plugin;

import com.comphenix.protocol.ProtocolManager;
import de.robingrether.idisguise.api.DisguiseAPI;
import me.vowed.api.companies.CompanyManager;
import me.vowed.api.damage.IDamage;
import me.vowed.api.database.Database;
import me.vowed.api.health.IHealthManager;
import me.vowed.api.money.IMoneyManager;
import me.vowed.api.race.IRaceManager;
import me.vowed.api.test.ShopData;
import me.vowed.api.shops.IShopManager;
import me.vowed.api.utils.Log;

/**
 * Created by JPaul on 9/30/2015.
 */
public class Vowed
{
    private static IVowedPlugin PLUGIN;
    private static IDamage DAMAGE;
    public static final Log LOG = new Log("[Vowed]");

    public static void setPlugin(IVowedPlugin plugin)
    {
        if (PLUGIN != null)
        {
            return;
        }
        PLUGIN = plugin;
    }

    public static IVowedPlugin getPlugin()
    {
        return PLUGIN;
    }

    public static Database getDatabase()
    {
        return PLUGIN.getDataPool();
    }

    public static DisguiseAPI getDisguiseAPI() { return PLUGIN.getDisguiseAPI(); }

    public static IDamage getDamageManager()
    {
        return PLUGIN.getDamageManager();
    }

    public static IHealthManager getHealthManager()
    {
        return PLUGIN.getHealthManager();
    }

    public static IMoneyManager getMoneyManager()
    {
        return PLUGIN.getMoneyManager();
    }

    public static IRaceManager getRaceManager()
    {
        return PLUGIN.getRaceManager();
    }

    public static IShopManager getShopManager()
    {
        return PLUGIN.getShopManager();
    }

    public static CompanyManager getCompanyManager()
    {
        return PLUGIN.getCompanyManager();
    }

    public static ShopData getTransactionTable()
    {
        return PLUGIN.getTransactionTable();
    }

    public static ProtocolManager getProtocolManager()
    {
        return PLUGIN.getProtocolManager();
    }

    public static String getPrefix()
    {
        return PLUGIN.getPrefix();
    }
}
