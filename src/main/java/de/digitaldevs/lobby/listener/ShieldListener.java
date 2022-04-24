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
        final Player player = event.getPlayer(); // The player wich is moving

        for (Player target : this.shieldUsers.keySet()) {
            if (player == target) break;
            if (player.getLocation().distance(target.getLocation()) > RADIUS) break;
            if (player.hasPermission(Var.PERMISSION_IGNORE_SHIELD)) break;

            final Vector vector = this.calculateVelocity(player.getLocation(), target.getLocation(), Direction.PLAYER);
            player.setVelocity(vector);
        }

        // A player without an active shield is moving
        for (Entity entity : player.getNearbyEntities(RADIUS, RADIUS, RADIUS)) {  // Loop through all entities in radius (5|5|5)
            if (!(entity instanceof Player)) break; // If the entity is not a player the shield should not work
            Player target = (Player) entity; // Then we know the entity is a player and not an e.g. cow

            if (player == target) continue; // When the moving player is the player in the 5-block radius, we don't care

            if (!this.shieldUsers.containsKey(target))
                break; // When the entity in the 5-block radius is not a player with an activated shield we don't care
            if (player.hasPermission(Var.PERMISSION_IGNORE_SHIELD)) break; //

            final Vector vector = this.calculateVelocity(player.getLocation(), target.getLocation(), Direction.TARGET);
            player.setVelocity(vector);
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

}
