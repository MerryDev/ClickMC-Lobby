package de.digitaldevs.lobby.listener;

import de.digitaldevs.lobby.Var;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

public class DoubleJumpListener implements Listener {

    //TODO: Add cosmetics

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        player.setAllowFlight(true);
        player.setFlying(false);
    }

    @EventHandler
    public void onFly(PlayerToggleFlightEvent event) {
        final Player player = event.getPlayer();

        if (Var.FLY_PLAYERS.contains(player)) return;

        if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
            event.setCancelled(true);
            player.setAllowFlight(false);
            player.setFlying(false);
            player.setVelocity(player.getLocation().getDirection().multiply(2).add(new Vector(0, 1.5, 0)));
            player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 2.5F, 1.0F);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        if (Var.FLY_PLAYERS.contains(player)) return;

        if (player.getLocation().add(0, -1, 0).getBlock().getType() != Material.AIR) {
            player.setAllowFlight(true);
            player.setFlying(false);
        }
    }
}
