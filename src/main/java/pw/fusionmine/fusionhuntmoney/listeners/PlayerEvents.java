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
import java.text.DecimalFormat;

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
        Player killer = player.getKiller();
        if (killer != null) {
            if (player.equals(killer)) return;
            double balance = plugin.getEconomy().getBalance(player);
            if (balance > 0) {
                String group = plugin.getPermission().getPrimaryGroup(player);
                int percent = config.getInt("death-percent." + group, 10);
                BigDecimal equals = new BigDecimal(balance * percent / 100.0)
                        .setScale(0, RoundingMode.HALF_UP);
                if (equals.compareTo(BigDecimal.ZERO) > 0) {
                    plugin.getEconomy().withdrawPlayer(player, equals.doubleValue());
                    plugin.getEconomy().depositPlayer(killer, equals.doubleValue());
                    DecimalFormat formatter = new DecimalFormat("#,###");
                    String moneyStr = formatter.format(equals);
                    player.sendMessage(Utils.getMsg("death.message")
                            .replace("{money}", moneyStr)
                            .replace("{percent}", String.valueOf(percent))
                            .replace("{player}", killer.getName())
                    );
                    killer.sendMessage(Utils.getMsg("kill.player.message")
                            .replace("{money}", moneyStr)
                            .replace("{player}", player.getName())
                    );
                }
            }
        }
    }

}
