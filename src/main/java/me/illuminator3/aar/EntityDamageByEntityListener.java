package me.illuminator3.aar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageByEntityListener
    implements Listener
{
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e)
    {
        if (!isValid(e)) return;

        Player p = (Player) e.getDamager();
        double distance = calcDistance(p, e.getEntity());

        if (!isValid(distance, p)) return;

        int update = update(p);

        notifyAll(p, distance);

        if (update == AdvancedAntiReach.MAX)
            AdvancedAntiReach.LEVELS.remove(p.getUniqueId());

        cancel(e);
    }

    protected boolean isValid(EntityDamageByEntityEvent e)
    {
        return e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK && e.getDamager() instanceof Player && ((Player) e.getDamager()).getGameMode() != GameMode.CREATIVE;
    }

    protected boolean isValid(double distance, Player p)
    {
        return distance >= 4.8 + getLatency(p);
    }

    protected void cancel(Cancellable c)
    {
        c.setCancelled(true);
    }

    protected void notifyAll(Player p, double distance)
    {
        Bukkit.getOnlinePlayers().stream().filter(s -> s.hasPermission(AdvancedAntiReach.CONFIG.getString("permissions.notify"))).forEach(f -> AdvancedAntiReach.notify(f, p, distance));
    }

    protected int update(Player p)
    {
        int update = updateLevel(p);

        if (update == AdvancedAntiReach.MAX)
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), ChatColor.translateAlternateColorCodes('&', AdvancedAntiReach.CONFIG.getString("command")).replace("%prefix%", AdvancedAntiReach.PREFIX).replace("%player%", p.getName()));

        return update;
    }

    protected int updateLevel(Player p)
    {
        int current = 0;

        if (AdvancedAntiReach.LEVELS.containsKey(p.getUniqueId()))
            current = AdvancedAntiReach.LEVELS.get(p.getUniqueId());

        AdvancedAntiReach.LEVELS.put(p.getUniqueId(), ++current);

        return AdvancedAntiReach.LEVELS.get(p.getUniqueId());
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    protected double calcDistance(Entity ent1, Entity ent2)
    {
        Location ent1Loc = ent1.getLocation(),
                 ent2Loc = ent2.getLocation();

        double distance = ent1Loc.distance(ent2Loc);

        if (ent1Loc.getY() < ent2Loc.getY())
            distance -= (ent2Loc.getY() - ent1Loc.getY()) / (2 + 10 / 3);

        return Utils.cut(distance, 1);
    }

    protected double getLatency(Player p)
    {
        double latency = 0.;
        int ping = ((CraftPlayer) p).getHandle().ping;

        if (ping < 300 && ping > 100)
            latency = ((double) (ping)) / 100 / 10 * 1.1;
        /* else: ping spoof */

        return latency;
    }
}