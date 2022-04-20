package de.digitaldevs.lobby.utils;

import de.digitaldevs.core.builder.ItemBuilder;
import de.digitaldevs.lobby.Main;
import de.digitaldevs.lobby.Var;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author MerryChrismas
 * @author <a href='https://digitaldevs.de'>DigitalDevs.de</a>
 * @version 1.0.0
 */
public class ShieldManager {

    public static void activate(Player player) {
        Var.RUNNING_SHIELD.put(player, new BukkitRunnable() {
            @Override
            public void run() {
                player.playEffect(player.getLocation(), Effect.ENDER_SIGNAL, 3);
            }
        });
        Var.RUNNING_SHIELD.get(player).runTaskTimer(Main.getInstance(), 20L, 20L);
        player.getInventory().setItem(5, new ItemBuilder(Material.EYE_OF_ENDER).name("§7≫ §5Schutzschild §cdeaktivieren §7≪").build());
        player.sendMessage(Var.PREFIX + "§7Du hast das §5Schutzschild §aaktiviert§7.");
        player.playSound(player.getLocation(), Sound.PORTAL_TRIGGER, 8.0F, 1.0F);
    }

    public static void deactivate(Player player) {
        Var.RUNNING_SHIELD.get(player).cancel();
        Var.RUNNING_SHIELD.remove(player);
        player.getInventory().setItem(5, new ItemBuilder(Material.EYE_OF_ENDER).name("§7≫ §5Schutzschild §aaktivieren §7≪").build());
        player.sendMessage(Var.PREFIX + "§7Du hast das §5Schutzschild §cdeaktiviert§7.");
        player.playSound(player.getLocation(), Sound.CREEPER_HISS, 15.0F, 1.0F);
    }

}
