package de.digitaldevs.lobby.listener;

import de.digitaldevs.lobby.Var;
import de.digitaldevs.lobby.utils.Direction;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ShieldListener implements Listener {

    private static final double RADIUS = 3.5D;
    private final Map<Player, BukkitRunnable> shieldUsers;

    public ShieldListener() {
        this.shieldUsers = Var.RUNNING_SHIELD;
    }

    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();

        for (Entity entity : player.getNearbyEntities(RADIUS, RADIUS, RADIUS)) {
            if (!(entity instanceof Player)) return;
            final Player target = (Player) entity;

            if (player == target) return;

            // Check for players while the player with the activated shield is moving
            if (this.shieldUsers.containsKey(player)) {
                final Vector vector = this.calculateVelocity(player.getLocation(), target.getLocation(), Direction.TARGET);
                target.setVelocity(vector);

            } else {  // Check for players with an activated shield while a player without the bypass permission is moving
                final Vector vector = this.calculateVelocity(player.getLocation(), target.getLocation(), Direction.PLAYER);
                player.setVelocity(vector);
            }
        }
    }

    private Vector calculateVelocity(@NotNull final Location first, @NotNull final Location second, @NotNull final Direction direction) {
        final double ax = first.getX();
        final double ay = first.getY();
        final double az = first.getZ();

        final double bx = second.getX();
        final double by = second.getY();
        final double bz = second.getZ();

        double x, y, z;

        if (direction == Direction.TARGET) { // The Player with the activated shield is moving -> Other players should be thrown away
            x = ax - bx;
            y = ay - by;
            z = az - bz;

        } else { // The Player without the permission in moving -> he should be thrown away
            x = bx - ax;
            y = by - ay;
            z = bz - az;
        }

        return new Vector(x, y, z).normalize().setY(0.1D).multiply(1);
    }

}
