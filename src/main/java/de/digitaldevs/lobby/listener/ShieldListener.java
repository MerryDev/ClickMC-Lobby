package de.digitaldevs.lobby.listener;

import de.digitaldevs.lobby.Var;
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
        final Player player = event.getPlayer(); // The player which is moving

        // The player without an active shield is moving
        for (Player target : this.shieldUsers.keySet()) {
            if (player == target) break;
            if (player.getLocation().distance(target.getLocation()) > RADIUS) break;

            if (player.hasPermission(Var.PERMISSION_IGNORE_SHIELD)) break;

            final Vector vector = this.calculateVelocity(player.getLocation(), target.getLocation(), Direction.PLAYER);
            player.setVelocity(vector);
        }

        // Check if the moving player has an active shield
        if (!this.shieldUsers.containsKey(player)) return;

        // A player with an active shield is moving
        for (Entity entity : player.getNearbyEntities(RADIUS, RADIUS, RADIUS)) {
            if (!(entity instanceof Player)) break;
            Player target = (Player) entity;

            if (player == target) continue;
            if (target.hasPermission(Var.PERMISSION_IGNORE_SHIELD)) break;

            final Vector vector = this.calculateVelocity(player.getLocation(), target.getLocation(), Direction.TARGET);
            target.setVelocity(vector);
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
        if (direction == Direction.PLAYER) {
            x = ax - bx;
            y = ay - by;
            z = az - bz;

        } else {
            x = bx - ax;
            y = by - ay;
            z = bz - az;
        }

        return new Vector(x, y, z).normalize().multiply(2.0D).setY(0.3D);
    }

    private enum Direction {
        TARGET, PLAYER
    }

}
