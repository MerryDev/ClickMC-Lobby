package de.digitaldevs.lobby.listener;

import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;

/**
 * @author MerryChrismas
 * @author <a href='https://digitaldevs.de'>DigitalDevs.de</a>
 * @version 1.0.0
 */
public class FishListener implements Listener {

    @SneakyThrows
    @EventHandler
    public void onFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        FishHook hook = event.getHook();
        if (hook.getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.WATER) return;
        if (hook.getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() != Material.AIR) {
            Location playerLocation = player.getLocation();
            Location hookLocation = hook.getLocation();

            Vector vector = player.getVelocity();
            double distance = playerLocation.distance(hookLocation);

            vector.setX((1.08D * distance) * (hookLocation.getX() - playerLocation.getX()) / distance);
            vector.setY(distance * (hookLocation.getY() - playerLocation.getY()) / distance - (-0.5D * distance));
            vector.setZ(((1.08D * distance) * (hookLocation.getZ() - playerLocation.getZ()) / distance));

            player.setVelocity(vector);
            player.getInventory().getItemInHand().setDurability((short) 0);
            player.updateInventory();
        }
    }

}
