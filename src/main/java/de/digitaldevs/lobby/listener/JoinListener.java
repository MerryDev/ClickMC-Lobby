package de.digitaldevs.lobby.listener;

import de.digitaldevs.core.builder.ItemBuilder;
import de.digitaldevs.lobby.Main;
import de.digitaldevs.lobby.Var;
import de.digitaldevs.lobby.storage.PlayerStorage;
import de.digitaldevs.lobby.utils.HideManager;
import de.digitaldevs.lobby.utils.LocationManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author MerryChrismas
 * @author <a href='https://digitaldevs.de'>DigitalDevs.de</a>
 * @version 1.0.0
 */
public class JoinListener implements Listener {

    private static final PlayerStorage PLAYER_STORAGE = Main.getInstance().getPlayerStorage();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(null);
        teleportToSpawn(player);
        initPlayer(player);
    }

    public static void getItems(Player player) {
        player.getInventory().setItem(0, new ItemBuilder(Material.COMPASS).name("§7≫ §cNavigator §7≪").build());
        player.getInventory().setItem(1, new ItemBuilder(Material.BLAZE_ROD).name("§7≫ §6Spieler verstecken §7≪").build());

        if (player.hasPermission(Var.PERMISSION_STAFF) || player.hasPermission(Var.SUPER_PERMISSION)) {
            player.getInventory().setItem(3, new ItemBuilder(Material.TNT).name("§7≫ §cSilentLobby verlassen §7≪").build());
            player.getInventory().setItem(5, new ItemBuilder(Material.EYE_OF_ENDER).name("§7≫ §5Schutzschild §aaktivieren §7≪").build());
            player.getInventory().setItem(7, new ItemBuilder(Material.BARRIER).name("§7≫ §cKein Gadget ausgewählt §7≪").build());
            player.getInventory().setItem(8, new ItemBuilder(Material.CHEST).name("§7≫ §3Gadgets §7≪").build());

        } else if (player.hasPermission(Var.PERMISSION_VIP)) {
            player.getInventory().setItem(4, new ItemBuilder(Material.EYE_OF_ENDER).name("§7≫ §5Schutzschild §aaktivieren §7≪").build());
            player.getInventory().setItem(7, new ItemBuilder(Material.BARRIER).name("§7≫ §cKein Gadget ausgewählt §7≪").build());
            player.getInventory().setItem(8, new ItemBuilder(Material.CHEST).name("§7≫ §3Gadgets §7≪").build());

        } else {
            player.getInventory().setItem(4, new ItemBuilder(Material.CHEST).name("§7≫ §3Gadgets §7≪").build());
            player.getInventory().setItem(8, new ItemBuilder(Material.BARRIER).name("§7≫ §cKein Gadget ausgewählt §7≪").build());
        }
    }

    public static void initPlayer(Player player) {
        initBasic(player);
        getItems(player);

        if (!PLAYER_STORAGE.isStored(player.getUniqueId())) PLAYER_STORAGE.addEntry(player.getUniqueId());

        // Hide or show players by the stored player data
        HideManager hideManager = new HideManager(player);
        int visibleState = PLAYER_STORAGE.getStoredInt(player.getUniqueId(), "Visibility");
        Bukkit.getOnlinePlayers().forEach(target -> {
            if (visibleState == 0) hideManager.showAll();
            else if (visibleState == 2) hideManager.hideAll();
            else hideManager.showVIPsAndTeam();
        });

    }

    public static void initBasic(Player player) {
        player.getInventory().clear();
        player.setFoodLevel(20);
        player.setHealth(20.0D);
        player.setLevel(0);
        player.setExp(0.0F);
        player.setGameMode(GameMode.SURVIVAL);
    }

    public static void teleportToSpawn(Player player) {
        Location location = new LocationManager("spawn").getLocation();
        if (location == null) {
            if (player.hasPermission(Var.SUPER_PERMISSION)) {
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10.0F, 1.0F);
                player.sendMessage(Var.PREFIX + "§cDer §eLobbyspawn §cwurde noch nicht gesetzt!");
            }
        } else {
            player.teleport(location);
        }
    }

}