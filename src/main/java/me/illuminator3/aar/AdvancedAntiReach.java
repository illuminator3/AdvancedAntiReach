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
        setDefaults();
        registerCommand();
        registerEvents();
    }

    protected void registerCommand()
    {
        String[] messages = {
            "§7§m---------------------------",
            "§6AAR §7developed by §eilluminator3",
            "§7Discord§8: §eilluminator3#0001",
            "§7§m---------------------------"
        };

        getCommand("aar").setExecutor(Utils.createMessageExecutor(messages));
    }

    protected void registerEvents()
    {
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
    }

    protected void setDefaults()
    {
        CONFIG = getConfig();
        PREFIX = ChatColor.translateAlternateColorCodes('&', CONFIG.getString("messages.prefix"));
        MAX = CONFIG.getInt("max");
    }

    public static void notify(Player s, Player p, double r)
    {
        int l = LEVELS.get(p.getUniqueId());

        s.sendMessage(ChatColor.translateAlternateColorCodes('&', CONFIG.getString("messages.notify")).replace("%prefix%", PREFIX).replace("%player%", p.getName()).replace("%reach%", String.valueOf(r)).replace("%level%", String.valueOf(l)).replace("%max%", String.valueOf(MAX)));
    }
}