package me.vowed.api.plugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.zaxxer.hikari.HikariConfig;
import de.robingrether.idisguise.api.DisguiseAPI;
import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;
import me.vowed.api.damage.Damage;
import me.vowed.api.damage.DamagePlayer;
import me.vowed.api.damage.IDamage;
import me.vowed.api.database.Database;
import me.vowed.api.database.MySQLDatabase;
import me.vowed.api.enchants.HideEnchantsListener;
import me.vowed.api.health.HealthManager;
import me.vowed.api.health.IHealthManager;
import me.vowed.api.money.IMoneyManager;
import me.vowed.api.money.MoneyListener;
import me.vowed.api.money.MoneyManager;
import me.vowed.api.pets.PetListener;
import me.vowed.api.player.PlayerWrapperListener;
import me.vowed.api.race.IRaceManager;
import me.vowed.api.race.RaceListener;
import me.vowed.api.race.RaceManager;
import me.vowed.api.test.ShopData;
import me.vowed.api.shops.*;
import me.vowed.api.skins.SkinHandler;
import me.vowed.api.skins.SkinUtil;
import me.vowed.api.stocks.Test;
import me.vowed.api.utils.Log;
import me.vowed.api.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.swing.*;
import java.sql.PreparedStatement;

/**
 * Created by JPaul on 9/28/2015.
 */
public class VowedPlugin extends JavaPlugin implements IVowedPlugin, Listener
{
    private static VowedPlugin instance;
    private ProtocolManager protocolManager;

    private static EffectManager effectManager;
    private static DisguiseAPI DISGUISE_MANAGER;
    private static Damage DAMAGE_MANAGER;
    private static IHealthManager HEALTH_MANAGER;
    private static IMoneyManager MONEY_MANAGER;
    private static IShopManager SHOP_MANAGER;
    private static IRaceManager RACE_MANAGER;
    private static ShopData TRANSACTIONS;
    private JFrame frame;

    private static Database DATA_POOL;

    String prefix = ChatColor.WHITE + "[ProjectVowed]";

    public void onEnable()
    {

        instance = this;
        Vowed.setPlugin(this);
        Logger.initiate(prefix);
        Logger.log(Log.LogLevel.NORMAL, Vowed.getPrefix(), true);

        prepareDatabase();

        getInstance().getServer().getPluginManager().registerEvents(new PlayerWrapperListener(), getInstance());
        getInstance().getServer().getPluginManager().registerEvents(new Test(), getInstance());
        getInstance().getServer().getPluginManager().registerEvents(new RaceListener(), getInstance());
        getInstance().getServer().getPluginManager().registerEvents(new SkinHandler(), getInstance());
        getInstance().getServer().getPluginManager().registerEvents(new MoneyListener(), getInstance());
        getInstance().getServer().getPluginManager().registerEvents(new PetListener(), getInstance());
        getInstance().getServer().getPluginManager().registerEvents(new ShopListener(), getInstance());
        getInstance().getServer().getPluginManager().registerEvents(new DamagePlayer(), getInstance());
        getInstance().getServer().getPluginManager().registerEvents(this, getInstance());

        DAMAGE_MANAGER = new Damage();
        HEALTH_MANAGER = new HealthManager();
        SHOP_MANAGER = new ShopManager();
        MONEY_MANAGER = new MoneyManager();
        RACE_MANAGER = new RaceManager();
        DISGUISE_MANAGER = getServer().getServicesManager().getRegistration(DisguiseAPI.class).getProvider();
        TRANSACTIONS = null;

        protocolManager = ProtocolLibrary.getProtocolManager();
        EffectLib effectLib = EffectLib.instance();
        HideEnchantsListener enchantsListener = new HideEnchantsListener(getServer(), getLogger());
        enchantsListener.addListener(protocolManager, getInstance());

        SkinUtil.createFile();

    }

    public void onDisable()
    {
        DATA_POOL.disconnect();
        instance = null;
        TRANSACTIONS.dispose();

        SkinUtil.saveSkins();
    }

    private void prepareDatabase()
    {
        String host = "localhost";
        int port = 3306;
        String databaseName = "mysql";
        String username = "root";
        String password = "";
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + databaseName);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);

        DATA_POOL = new MySQLDatabase(host, port, databaseName, username, password, getInstance());

        DATA_POOL.connect();

        PreparedStatement preparedStatement = DATA_POOL.prepareStatement("CREATE TABLE IF NOT EXISTS " + "player_info" + "(UUID VARCHAR(32) PRIMARY KEY, first_login BOOL, race VARCHAR(32));");

        DATA_POOL.execute(preparedStatement);
    }

    public static VowedPlugin getInstance()
    {
        return instance;
    }

    public static EffectManager getEffectManager()
    {
        return effectManager;
    }

    @Override
    public ProtocolManager getProtocolManager()
    {
        return protocolManager;
    }

    @Override
    public String getPrefix()
    {
        return prefix;
    }

    @Override
    public Database getDataPool()
    {
        return DATA_POOL;
    }

    @Override
    public DisguiseAPI getDisguiseAPI()
    {
        return DISGUISE_MANAGER;
    }

    @Override
    public IDamage getDamageManager()
    {
        return DAMAGE_MANAGER;
    }

    @Override
    public IHealthManager getHealthManager()
    {
        return HEALTH_MANAGER;
    }

    @Override
    public IMoneyManager getMoneyManager()
    {
        return MONEY_MANAGER;
    }

    @Override
    public IShopManager getShopManager()
    {
        return SHOP_MANAGER;
    }

    @Override
    public IRaceManager getRaceManager()
    {
        return RACE_MANAGER;
    }

    @Override
    public ShopData getTransactionTable()
    {
        return TRANSACTIONS;
    }

    @Override
    public String getCommandString()
    {
        return null;
    }

    @Override
    public String getAdminCommandString()
    {
        return null;
    }

    @Override
    public boolean isUsingNetty()
    {
        return false;
    }

    @Override
    public boolean isUpdateAvailable()
    {
        return false;
    }

    @Override
    public String getUpdateName()
    {
        return null;
    }

    @Override
    public long getUpdateSize()
    {
        return 0;
    }

    Location[] path;
    Material[] materials;
    Byte[] data;

    @EventHandler
    public void test(PlayerBedEnterEvent bedEnterEvent)
    {
        bedEnterEvent.setCancelled(true);
        Vowed.LOG.warning("1");

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run()
            {
                resetTest();
            }
        }, 80L);
    }

    public void resetTest()
    {
        for(int i = 0; i < path.length; i++)
        {
            path[i].getBlock().setType(materials[i]);
            path[i].getBlock().setData(data[i]);
        }
    }

}
