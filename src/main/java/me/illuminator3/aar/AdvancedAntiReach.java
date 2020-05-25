package me.illuminator3.aar;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AdvancedAntiReach
    extends JavaPlugin
{
    public static final Map<UUID, Integer> LEVELS = new HashMap<>();

    public static FileConfiguration CONFIG;
    public static String PREFIX;
    public static int MAX;

    @Override
    public void onEnable()
    {
        saveDefaultConfig();

        CONFIG = getConfig();
        PREFIX = ChatColor.translateAlternateColorCodes('&', CONFIG.getString("messages.prefix"));
        MAX = CONFIG.getInt("max");

        getCommand("aar").setExecutor(new AARCommand());
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
    }

    public static void notify(Player s, Player p, double r)
    {
        int l = LEVELS.get(p.getUniqueId());

        s.sendMessage(ChatColor.translateAlternateColorCodes('&', CONFIG.getString("messages.notify")).replace("%prefix%", PREFIX).replace("%player%", p.getName()).replace("%reach%", String.valueOf(r)).replace("%level%", String.valueOf(l)).replace("%max%", String.valueOf(MAX)));
    }
}