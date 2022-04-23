package de.digitaldevs.lobby.listener;

import de.digitaldevs.lobby.Var;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author MerryChrismas
 * @author <a href='https://digitaldevs.de'>DigitalDevs.de</a>
 * @version 1.0.0
 */
public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        event.setQuitMessage(null);

        Var.BUILD_PLAYERS.remove(player.getUniqueId());

        if (Var.RUNNING_SHIELD.containsKey(player)) {
            Var.RUNNING_SHIELD.get(player).cancel();
            Var.RUNNING_SHIELD.remove(player);
        }
    }

}
