package de.digitaldevs.lobby.listener;

import de.digitaldevs.lobby.Main;
import de.digitaldevs.lobby.Var;
import de.digitaldevs.lobby.storage.PlayerStorage;
import de.digitaldevs.lobby.utils.HideManager;
import de.digitaldevs.lobby.utils.LocationManager;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.dytanic.cloudnet.ext.bridge.player.executor.ServerSelectorType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author MerryChrismas
 * @author waddlespam
 * @version 1.0.0
 */
public class ClickListener implements Listener {

    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);
    private final PlayerStorage playerStorage = Main.getInstance().getPlayerStorage();

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!Var.BUILD_PLAYERS.contains(player.getUniqueId())) event.setCancelled(true);

        try {
            final Inventory inventory = event.getClickedInventory();
            final ItemStack item = event.getCurrentItem();

            if (item == null) return;

            final ItemMeta currentItem = item.getItemMeta();

            if (inventory.equals(player.getInventory())) {
                final ICloudPlayer cloudPlayer = this.playerManager.getOnlinePlayer(player.getUniqueId());
                if (cloudPlayer == null) return;

                if (currentItem.getDisplayName().equalsIgnoreCase("§7≫ §bDevserver betreten §7≪")) {
                    cloudPlayer.getPlayerExecutor().connect("devserver-1");
                    player.sendMessage(Var.PREFIX + "§7Du wirst nun zum §eDeveloper-Netzwerk §7verbunden...");
                } else if (currentItem.getDisplayName().equalsIgnoreCase("§7≫ §aBauServer betreten §7≪")) {
                    cloudPlayer.getPlayerExecutor().connect("BauServer-1");
                    player.sendMessage(Var.PREFIX + "§7Du wirst nun zum §eBauserver §7verbunden...");
                }





            } else {
                if (item.getType() == Material.STAINED_GLASS_PANE) return;

                if (inventory.getName().equals("§cNavigator")) {
                    boolean success = false;

                    if (currentItem.getDisplayName().equals("§a§lSpawn")) {
                        JoinListener.teleportToSpawn(player);
                        success = true;

                    } else if (currentItem.getDisplayName().equals("§c§lBedWars")) {
                        if (this.teleport(player, "bedwars")) success = true;

                    } else if (currentItem.getDisplayName().equals("§f§lGunGame")) {
                        if (this.teleport(player, "gungame")) success = true;

                    } else if (currentItem.getDisplayName().equals("§6§lMLGRush")) {
                        if (this.teleport(player, "mlgrush")) success = true;

                    } else if (currentItem.getDisplayName().equals("§6§lBowBash")) {
                        if (this.teleport(player, "bowbash")) success = true;

                    } else if (currentItem.getDisplayName().equals("§b§lFlashBlock")) {
                        if (this.teleport(player, "flashblock")) success = true;

                    }

                    if (success) player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 10.0F, 1.0F);

                } else if (inventory.getName().equals("§6Spieler verstecken")) {
                    int visibleState = this.playerStorage.getStoredInt(player.getUniqueId(), "Visibility");
                    final HideManager hideManager = new HideManager(player);

                    if (currentItem.getDisplayName().equals("§aAlle Spieler anzeigen")) {
                        if (visibleState != 0) {
                            hideManager.showAll();
                            playerStorage.storeInt(player.getUniqueId(), "Visibility", 0);
                            player.sendMessage(Var.PREFIX + "§7Du siehst nun §aalle Spieler§7.");
                            player.playSound(player.getLocation(), Sound.NOTE_PLING, 10.0F, 3.0F);

                        } else {
                            player.sendMessage(Var.PREFIX + "§cDu siehst bereits alle Spieler!");
                            player.playSound(player.getLocation(), Sound.NOTE_BASS, 10.0F, 1.5F);
                        }

                    } else if (currentItem.getDisplayName().equalsIgnoreCase("§5Nur VIPs und Teammitglieder anzeigen")) {
                        if (visibleState != 1) {
                            hideManager.showVIPsAndTeam();
                            playerStorage.storeInt(player.getUniqueId(), "Visibility", 1);
                            player.sendMessage(Var.PREFIX + "§7Du siehst nun nur doch §5VIPs und Teammitglieder§7.");
                            player.playSound(player.getLocation(), Sound.NOTE_PLING, 10.0F, 3.0F);

                        } else {
                            player.sendMessage(Var.PREFIX + "§cDu siehst bereits nur VIPs und Teammitglieder!");
                            player.playSound(player.getLocation(), Sound.NOTE_BASS, 10.0F, 1.5F);
                        }

                    } else if (currentItem.getDisplayName().equalsIgnoreCase("§cAlle Spieler verstecken")) {
                        if (visibleState != 2) {
                            hideManager.hideAll();
                            playerStorage.storeInt(player.getUniqueId(), "Visibility", 2);
                            player.sendMessage(Var.PREFIX + "§7Du siehst nun §ckeine Spieler§7.");
                            player.playSound(player.getLocation(), Sound.NOTE_PLING, 10.0F, 3.0F);

                        } else {
                            player.sendMessage(Var.PREFIX + "§cDu siehst keine Spieler!");
                            player.playSound(player.getLocation(), Sound.NOTE_BASS, 10.0F, 1.5F);
                        }

                    }

                    player.closeInventory();

                } else if (inventory.getName().equals("§3Gadgets")) {
                    if (currentItem.getDisplayName().equals("§cAktuelles Gadget entfernen")) {
                        player.getInventory().clear();
                        JoinListener.getItems(player);
                        player.playSound(player.getLocation(), Sound.ITEM_BREAK, 7.0F, 1.0F);
                        player.sendMessage(Var.PREFIX + "§7Du hast dein aktuelles Gadget §centfernt§7.");

                    } else {
                        if (player.hasPermission(Var.PERMISSION_STAFF) || player.hasPermission(Var.PERMISSION_VIP))
                            player.getInventory().setItem(7, event.getCurrentItem());
                        else player.getInventory().setItem(8, event.getCurrentItem());
                        player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 7.0F, 1.0F);
                        player.sendMessage(Var.PREFIX + "§7Du hast das Gadget §r" + currentItem.getDisplayName() + " §7ausgewählt.");
                    }

                }
            }

        } catch (Exception ignored) {
        }
    }

    private boolean teleport(Player player, String name) {
        Location location = new LocationManager(name).getLocation();
        if (location != null) {
            player.teleport(location);
            return true;

        } else {
            if (player.hasPermission(Var.SUPER_PERMISSION))
                player.sendMessage(Var.PREFIX + "§cDieser Spielmodi wurde noch nicht gesetzt!");
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10.0F, 1.0F);
            return false;
        }
    }

}
