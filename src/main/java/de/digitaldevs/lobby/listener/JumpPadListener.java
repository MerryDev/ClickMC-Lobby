package de.digitaldevs.lobby.listener;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class JumpPadListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.getLocation().getBlock().getType() == Material.IRON_PLATE) {
            if (player.getLocation().subtract(0, 1, 0).getBlock().getType() == Material.IRON_BLOCK) {

                Vector vector = player.getLocation().getDirection().multiply(3.75).setY(1.75);
                player.setVelocity(vector);

                player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 2.5F, 1.0F);
            }
        }
    }

}
