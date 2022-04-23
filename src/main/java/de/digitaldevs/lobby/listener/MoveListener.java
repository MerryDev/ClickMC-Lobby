package de.digitaldevs.lobby.listener;

import de.digitaldevs.lobby.Var;
import de.digitaldevs.lobby.utils.Direction;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

/**
 * @author MerryChrismas
 * @author <a href='https://digitaldevs.de'>DigitalDevs.de</a>
 * @version 1.0.0
 */
public class MoveListener implements Listener {

    private static final double RADIUS = Var.RADIUS;

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        for (Player target1 : Var.RUNNING_SHIELD.keySet()) {
            if (player != target1) {
                if (player.getLocation().distance(target1.getLocation()) <= RADIUS) {
                    if (!target1.hasPermission(Var.PERMISSION_IGNORE_SHIELD)) {
                        Vector calculateVelocity = this.calculateVelocity(player.getLocation(), target1.getLocation(), Direction.PLAYER);
                        player.setVelocity(calculateVelocity);
                    }
                }
            }
        }

        if (!Var.RUNNING_SHIELD.containsKey(player)) {
            for (Entity entity : player.getNearbyEntities(RADIUS, RADIUS, RADIUS)) {
                if (entity instanceof Player) {
                    Player target = (Player) entity;
                    if (player != target) {
                        if (!target.hasPermission(Var.PERMISSION_IGNORE_SHIELD)) {
                            Vector vector = this.calculateVelocity(player.getLocation(), target.getLocation(), Direction.TARGET);
                            target.setVelocity(vector);
                        }
                    }
                }
            }
        }


    }

    private Vector calculateVelocity(Location first, Location second, Direction direction) {
        double ax = first.getX(), ay = first.getY(), az = first.getZ();
        double bx = second.getX(), by = second.getY(), bz = second.getZ();
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

        return new Vector(x, y, z).normalize().multiply(2).setY(0.3D);
    }

}
