package pw.fusionmine.fusionhuntmoney.utils;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

@UtilityClass
public class Utils {

    @Getter
    private static final JavaPlugin inst = JavaPlugin.getProvidingPlugin(Utils.class);

    public String color(String path) {
        return ChatColor.translateAlternateColorCodes('&', path);
    }

    public String getMsg(String path) {
        return color(inst.getConfig().getString(path));
    }

}
