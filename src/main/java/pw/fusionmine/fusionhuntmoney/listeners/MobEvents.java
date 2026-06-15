package pw.fusionmine.fusionhuntmoney.listeners;

import java.util.Random;

import pw.fusionmine.fusionhuntmoney.FusionHuntMoney;
import pw.fusionmine.fusionhuntmoney.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MobEvents implements Listener {

    private FusionHuntMoney plugin;

    private FileConfiguration config;

    private Random RANDOM = new Random();

    public MobEvents(FusionHuntMoney plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        LivingEntity livingEntity = e.getEntity();
        if (!(livingEntity instanceof Player) && livingEntity.getKiller() != null) {
            Player player = livingEntity.getKiller();
            if (!livingEntity.isSilent()) {
                int minDrop = config.getInt(livingEntity.fromMobSpawner() ? "mob-drop.spawner.min" : "mob-drop.no-spawner.min");
                int maxDrop = config.getInt(livingEntity.fromMobSpawner() ? "mob-drop.spawner.max" : "mob-drop.no-spawner.max");
                int difference = maxDrop - minDrop + 1;
                double money = (this.RANDOM.nextInt(difference) + minDrop);
                String group = plugin.getPermission().getPrimaryGroup(player);
                double booster = config.getDouble("boosters." + group, 1.0D);
                money *= booster;
                plugin.getEconomy().depositPlayer(player, money);
                String typeName = livingEntity.getType().name();
                String mobName = config.getString("mob-names." + typeName, typeName.toUpperCase());
                player.sendActionBar(Utils.getMsg("kill.mob.actionbar")
                        .replace("{money}", String.valueOf(money))
                        .replace("{mob}", mobName)
                        .replace("{booster}", String.valueOf(booster))
                );
            }
        }
    }

}
