package me.vowed.api.plugin;

import com.comphenix.protocol.ProtocolManager;
import de.robingrether.idisguise.api.DisguiseAPI;
import me.vowed.api.companies.CompanyManager;
import me.vowed.api.damage.IDamage;
import me.vowed.api.database.Database;
import me.vowed.api.health.IHealthManager;
import me.vowed.api.money.IMoneyManager;
import me.vowed.api.race.IRaceManager;
import me.vowed.api.utils.ShopData;
import me.vowed.api.shops.IShopManager;
import org.bukkit.plugin.Plugin;

/**
 * Created by JPaul on 9/30/2015.
 */
public interface IVowedPlugin extends Plugin
{
    String getPrefix();

    Database getDataPool();

    DisguiseAPI getDisguiseAPI();

    ProtocolManager getProtocolManager();

    IDamage getDamageManager();

    IHealthManager getHealthManager();

    IMoneyManager getMoneyManager();

    IShopManager getShopManager();

    IRaceManager getRaceManager();

    CompanyManager getCompanyManager();

    ShopData getTransactionTable();

    String getCommandString();

    String getAdminCommandString();

    boolean isUsingNetty();

    boolean isUpdateAvailable();

    String getUpdateName();

    long getUpdateSize();
}
