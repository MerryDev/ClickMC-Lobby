package de.digitaldevs.lobby.listener;

import de.digitaldevs.lobby.Var;
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
        event.setQuitMessage(null);
        if (Var.RUNNING_SHIELD.containsKey(event.getPlayer())) {
            Var.RUNNING_SHIELD.get(event.getPlayer()).cancel();
            Var.RUNNING_SHIELD.remove(event.getPlayer());
        }
    }

}
