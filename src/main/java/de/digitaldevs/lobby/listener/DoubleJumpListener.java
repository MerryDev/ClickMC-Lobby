package de.digitaldevs.lobby.listener;

import de.digitaldevs.lobby.Var;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import java.util.HashMap;

public class DoubleJumpListener implements Listener {

    HashMap<Player, Boolean> cooldown = new HashMap<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        player.setAllowFlight(true);
        cooldown.put(player, false);

    }

    @EventHandler
    public void onFly(PlayerToggleFlightEvent event) {
        final Player player = event.getPlayer();

        if(Var.FLY_PLAYERS.contains(player)) return;

        if(player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
            event.setCancelled(true);
            if(cooldown.get(player)) return;
            player.setVelocity(player.getLocation().getDirection().setY(1));
            cooldown.put(player, true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        if(Var.FLY_PLAYERS.contains(player)) return;

        if(player.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.AIR) {
            cooldown.put(player, false);
        }
    }

    @EventHandler
    public void onGamemodeChange(PlayerGameModeChangeEvent event) {
        final Player player = event.getPlayer();
        if(Var.FLY_PLAYERS.contains(player)) return;
        if(event.getNewGameMode() == GameMode.SURVIVAL || event.getNewGameMode() == GameMode.ADVENTURE) {
            player.setAllowFlight(true);
        }
    }
}
