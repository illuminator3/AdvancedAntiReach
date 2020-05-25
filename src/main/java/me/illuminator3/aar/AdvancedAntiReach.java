package me.illuminator3.aar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Level;

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
        registerCommands();
        registerEvents();
        message(true);
    }

    @Override
    public void onDisable()
    {
        message(false);
    }

    protected void message(boolean b)
    {
        ConsoleCommandSender c = Bukkit.getConsoleSender();

        c.sendMessage("§7§m-----------------------------");
        c.sendMessage("§6AAR §7developed by §eilluminator3");
        c.sendMessage("§7Discord§8: §eilluminator3#0001");
        c.sendMessage("§7§m-----------------------------");
        c.sendMessage("§eStatus§8: " + (b ? "§aenabled" : "§cdisabled"));
        c.sendMessage("§7§m-----------------------------");
    }

    protected void registerCommands()
    {
        String[] messages = {
            "§7§m-----------------------------",
            "§6AAR §7developed by §eilluminator3",
            "§7Discord§8: §eilluminator3#0001",
            "§7§m-----------------------------"
        };

        getCommand("aar").setExecutor(Utils.createMessageExecutor(messages));
        getCommand("aarlist").setExecutor((a0, a1, a2, a3) -> {
            if (a3.length >= 2)
            {
                a0.sendMessage(PREFIX + "Usage: /aarlist [<Page>]");

                return true;
            }

            int page = 1;

            if (a3.length == 1)
            {
                try
                {
                    page = Integer.parseInt(a3[0]);
                } catch (NumberFormatException ignored)
                {
                    a0.sendMessage(PREFIX + "Invalid page §e" + a3[0]);

                    return true;
                }
            }

            List<UUID> list = new ArrayList<>(LEVELS.keySet());

            if (list.isEmpty())
            {
                a0.sendMessage(PREFIX + "No alerts");

                return true;
            }

            int maxPage = ((list.size() + 1) / 10 + 1);

            if (page > maxPage)
            {
                a0.sendMessage(PREFIX + "Invalid page §e" + page);

                return true;
            }

            a0.sendMessage("§7§m-------------------------");
            a0.sendMessage("§7You are viewing page §e" + page + "§8/§e" + maxPage);
            a0.sendMessage("§7§m-------------------------");

            try
            {
                for (int i = 10 * (page - 1); i < 10 + 10 * (page - 1); i++)
                {
                    UUID uuid = list.get(i);
                    int l = LEVELS.get(uuid);

                    a0.sendMessage("§e" + Bukkit.getOfflinePlayer(uuid).getName() + "§8: §a" + l);
                }
            } catch (final Exception ignored) {}

            a0.sendMessage("§7§m-------------------------");

            return true;
        });
        getCommand("aarlist").setPermissionMessage(ChatColor.translateAlternateColorCodes('&', CONFIG.getString("messages.noperms")).replace("%prefix%", PREFIX).replace("%permission%", CONFIG.getString("permissions.list")));
        getCommand("aarlist").setPermission(CONFIG.getString("permissions.list"));
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