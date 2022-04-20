package de.digitaldevs.lobby.utils;

import de.digitaldevs.lobby.Var;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author MerryChrismas
 * @author <a href='https://digitaldevs.de'>DigitalDevs.de</a>
 * @version 1.0.0
 */
@AllArgsConstructor
public class HideManager {

    private final Player player;

    public void showAll() {
        Bukkit.getOnlinePlayers().forEach(this.player::showPlayer);
    }

    public void showVIPsAndTeam() {
        Bukkit.getOnlinePlayers().forEach(target -> {
            if (!(target.hasPermission(Var.PERMISSION_VIP) || target.hasPermission(Var.PERMISSION_STAFF) || target.hasPermission(Var.SUPER_PERMISSION)))
                this.player.hidePlayer(target);
        });
    }

    public void hideAll() {
        Bukkit.getOnlinePlayers().forEach(this.player::hidePlayer);
    }

}
