package pw.fusionmine.fusionhuntmoney.listeners;

import pw.fusionmine.fusionhuntmoney.FusionHuntMoney;
import pw.fusionmine.fusionhuntmoney.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PlayerEvents implements Listener {

    private FusionHuntMoney plugin;

    private FileConfiguration config;

    public PlayerEvents(FusionHuntMoney plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        if (config.getBoolean("death.sendTitle.enabled")) {
            String title = Utils.getMsg("death.sendTitle.title");
            String subtitle = Utils.getMsg("death.sendTitle.subtitle");
            player.sendTitle(title, subtitle);
        }
        if (e.getEntity().getKiller() != null) {
            Player killer = player.getKiller();
            if (player.equals(killer)) return;
            double balance = plugin.getEconomy().getBalance(player);
            if (balance > 0) {
                String group = plugin.getPermission().getPrimaryGroup(player);
                int percent = config.getInt("death-percent." + group, 10);
                double equals = new BigDecimal(balance * percent / 100.0)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();
                if (equals > 0) {
                    plugin.getEconomy().withdrawPlayer(player, equals);
                    plugin.getEconomy().depositPlayer(killer, equals);
                    player.sendMessage(Utils.getMsg("death.message")
                            .replace("{money}", String.valueOf(equals))
                            .replace("{percent}", String.valueOf(percent))
                            .replace("{player}", killer.getName())
                    );
                    killer.sendMessage(Utils.getMsg("kill.player.message")
                            .replace("{money}", String.valueOf(equals))
                            .replace("{player}", player.getName())
                    );
                }
            }
        }
    }

}
