package de.digitaldevs.lobby.listener;

import de.digitaldevs.core.builder.ItemBuilder;
import de.digitaldevs.lobby.Main;
import de.digitaldevs.lobby.Var;
import de.digitaldevs.lobby.storage.PlayerStorage;
import de.digitaldevs.lobby.utils.ShieldManager;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.dytanic.cloudnet.ext.bridge.player.executor.ServerSelectorType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author MerryChrismas
 * @author <a href='https://digitaldevs.de'>DigitalDevs.de</a>
 * @version 1.0.0
 */
public class InteractListener implements Listener {

    private final List<Material> allowedItems = new ArrayList<>(Arrays.asList(Material.ENDER_PEARL, Material.FISHING_ROD));
    private final PlayerStorage playerStorage = Main.getInstance().getPlayerStorage();

    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        try {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                event.setCancelled(this.checkCancellation(player, event.getMaterial()));

                ItemMeta currentItem = player.getItemInHand().getItemMeta();

                // Navigator
                if (currentItem.getDisplayName().equals("§7≫ §cNavigator §7≪")) {
                    Inventory inventory = Bukkit.createInventory(null, 54, "§cNavigator");

                    this.setBorderItems(inventory);
                    inventory.setItem(20, new ItemBuilder(Material.BED).name("§b§lBedWars").build());
                    inventory.setItem(22, new ItemBuilder(Material.DIAMOND).name("§a§lSpawn").build());
                    inventory.setItem(24, new ItemBuilder(Material.WOOD_AXE).name("§f§fGunGame").build());
                    inventory.setItem(39, new ItemBuilder(Material.BLAZE_ROD).name("§6§lMLGRush").build());
                    inventory.setItem(41, new ItemBuilder(Material.BOW).name("§6§lBowBash").build());

                    player.openInventory(inventory);

                    // Player-Hider
                } else if (currentItem.getDisplayName().equals("§7≫ §6Spieler verstecken §7≪")) {
                    Inventory inventory = Bukkit.createInventory(null, 27, "§6Spieler verstecken");
                    this.setBorderItems(inventory);

                    int visibleState = playerStorage.getStoredInt(player.getUniqueId(), "Visibility");
                    if (visibleState == 0) {
                        inventory.setItem(11, new ItemBuilder(Material.INK_SACK, 1, 10).name("§aAlle Spieler anzeigen").lore("§7Aktuell §aausgewählt").build());
                        inventory.setItem(13, new ItemBuilder(Material.INK_SACK, 1, 5).name("§5Nur VIPs und Teammitglieder anzeigen").build());
                        inventory.setItem(15, new ItemBuilder(Material.INK_SACK, 1, 1).name("§cAlle Spieler verstecken").build());

                    } else if (visibleState == 1) {
                        inventory.setItem(11, new ItemBuilder(Material.INK_SACK, 1, 10).name("§aAlle Spieler anzeigen").build());
                        inventory.setItem(13, new ItemBuilder(Material.INK_SACK, 1, 5).name("§5Nur VIPs und Teammitglieder anzeigen").lore("§7Aktuell §aausgewählt").build());
                        inventory.setItem(15, new ItemBuilder(Material.INK_SACK, 1, 1).name("§cAlle Spieler verstecken").build());

                    } else if (visibleState == 2) {
                        inventory.setItem(11, new ItemBuilder(Material.INK_SACK, 1, 10).name("§aAlle Spieler anzeigen").build());
                        inventory.setItem(13, new ItemBuilder(Material.INK_SACK, 1, 5).name("§5Nur VIPs und Teammitglieder anzeigen").build());
                        inventory.setItem(15, new ItemBuilder(Material.INK_SACK, 1, 1).name("§cAlle Spieler verstecken").lore("§7Aktuell §aausgewählt").build());

                    } else {
                        System.out.println(-1);
                    }

                    player.openInventory(inventory);

                    // Shield on
                } else if (currentItem.getDisplayName().equals("§7≫ §5Schutzschild §aaktivieren §7≪")) {
                    if (!Var.RUNNING_SHIELD.containsKey(player)) ShieldManager.activate(player);

                    // Shield off
                } else if (currentItem.getDisplayName().equals("§7≫ §5Schutzschild §cdeaktivieren §7≪")) {
                    if (Var.RUNNING_SHIELD.containsKey(player)) ShieldManager.deactivate(player);

                    // Gadgets
                } else if (currentItem.getDisplayName().equals("§7≫ §3Gadgets §7≪")) {
                    Inventory inventory = Bukkit.createInventory(null, 27, "§3Gadgets");

                    this.setBorderItems(inventory);
                    inventory.setItem(12, new ItemBuilder(Material.ENDER_PEARL).name("§5Enderperle").build());
                    inventory.setItem(14, new ItemBuilder(Material.FISHING_ROD).name("§bEnterhaken").build());
                    inventory.setItem(inventory.getSize() - 1, new ItemBuilder(Material.BARRIER).name("§cAktuelles Gadget entfernen").build());

                    player.openInventory(inventory);

                } else if (currentItem.getDisplayName().equals("§7≫ §cSilentLobby verlassen §7≪")) {
                    ICloudPlayer cloudPlayer = this.playerManager.getOnlinePlayer(player.getUniqueId());
                    if (cloudPlayer == null) return;
                    cloudPlayer.getPlayerExecutor().connectToGroup("Lobby", ServerSelectorType.HIGHEST_PLAYERS); // HIGHEST_PLAYERS and LOWEST_PLAYERS is reversed for some reason
                }
            }
        } catch (Exception ignored) {
        }
    }

    private boolean checkCancellation(Player player, Material material) {
        boolean canceled = true;
        if (Var.BUILD_PLAYERS.contains(player.getUniqueId())) canceled = false;
        else if (this.allowedItems.contains(material)) canceled = false;
        return canceled;
    }

    private void setBorderItems(Inventory inventory) {
        final ItemStack borderItem = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 8).name(" ").build();
        this.fillRect(inventory, (inventory.getSize() / 9) - 1, borderItem);
    }

    private void fillRect(Inventory inventory, int toRow, ItemStack item) {
        for (int row = 0; row <= toRow; row++) {
            for (int column = 0; column <= 8; column++) {
                if (row != 0 && row != toRow && column != 0 && column != 8) continue;
                this.set(inventory, row, column, item);
            }
        }
    }

    private void set(Inventory inventory, int row, int column, ItemStack item) {
        inventory.setItem(9 * row + column, item);
    }

}
