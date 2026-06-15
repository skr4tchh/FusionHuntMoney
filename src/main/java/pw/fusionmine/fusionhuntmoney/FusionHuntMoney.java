package pw.fusionmine.fusionhuntmoney;

import pw.fusionmine.fusionhuntmoney.listeners.MobEvents;
import pw.fusionmine.fusionhuntmoney.listeners.PlayerEvents;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class FusionHuntMoney extends JavaPlugin {

    private Economy economy;

    private Permission permission;

    @Override
    public void onEnable() {
        if (!setupEconomy() || !setupPermissions()) {
            getLogger().warning("Не удалось подключиться к Vault. Плагин будет отключен.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();
        registerListeners();
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new MobEvents(this), this);
        pluginManager.registerEvents(new PlayerEvents(this), this);
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
        economy = rsp.getProvider();
        return economy != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = this.getServer().getServicesManager().getRegistration(Permission.class);
        permission = rsp.getProvider();
        return permission != null;
    }

}
