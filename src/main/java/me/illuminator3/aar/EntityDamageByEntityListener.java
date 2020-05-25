package me.illuminator3.aar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageByEntityListener
    implements Listener
{
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e)
    {
        if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK || !(e.getDamager() instanceof Player))
            return;

        Player p = (Player) e.getDamager();

        if (p.getGameMode() == GameMode.CREATIVE)
            return;

        Location damagerLoc = p.getLocation(),
                 entityLoc = e.getEntity().getLocation();

        double distance;

        distance = damagerLoc.distance(entityLoc);

        if (damagerLoc.getY() < entityLoc.getY())
            distance -= (entityLoc.getY() - damagerLoc.getY()) / (2 + 10 / 3);

        final double d = Utils.cut(distance, 1);

        double latency = 0.;
        int ping = ((CraftPlayer) p).getHandle().ping;

        if (ping < 300 && ping > 100)
            latency = ((double) (ping)) / 100 / 10 * 1.1;
        /* else: ping spoof */

        if (d >= (4.8 + latency))
        {
            int current = 0;

            if (AdvancedAntiReach.LEVELS.containsKey(p.getUniqueId()))
                current = AdvancedAntiReach.LEVELS.get(p.getUniqueId());

            AdvancedAntiReach.LEVELS.put(p.getUniqueId(), ++current);

            Bukkit.getOnlinePlayers().stream().filter(s -> s.hasPermission(AdvancedAntiReach.CONFIG.getString("permissions.notify"))).forEach(f -> AdvancedAntiReach.notify(f, p, d));

            if (current == AdvancedAntiReach.MAX)
            {
                AdvancedAntiReach.LEVELS.remove(p.getUniqueId());

                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), ChatColor.translateAlternateColorCodes('&', AdvancedAntiReach.CONFIG.getString("command")).replace("%prefix%", AdvancedAntiReach.PREFIX).replace("%player%", p.getName()));
            }

            e.setCancelled(true);
        }
    }
}