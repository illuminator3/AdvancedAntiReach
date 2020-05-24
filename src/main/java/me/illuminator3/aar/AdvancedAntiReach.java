package me.illuminator3.aar;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AdvancedAntiReach
    extends JavaPlugin
{
    public static final Map<UUID, Integer> LEVELS = new HashMap<>();
    public static final int MAX = 5;
    public static final String PREFIX = "§8[§6AAR§8] §7";

    @Override
    public void onEnable()
    {
        getCommand("aar").setExecutor(new AARCommand());
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
    }

    public static void notify(Player s, Player p, double r)
    {
        int l = LEVELS.get(p.getUniqueId());

        s.sendMessage(PREFIX + "§e" + p.getName() + " §7may be using reach (§a" + r + "§7) [§e" + l + "§8/§e" + MAX + "§7]");
    }
}