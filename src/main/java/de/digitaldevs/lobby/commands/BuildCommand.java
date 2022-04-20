package de.digitaldevs.lobby.commands;

import de.digitaldevs.lobby.Var;
import de.digitaldevs.lobby.listener.JoinListener;
import de.digitaldevs.lobby.utils.ShieldManager;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author MerryChrismas
 * @author <a href='https://digitaldevs.de'>DigitalDevs.de</a>
 * @version 1.0.0
 */
public class BuildCommand implements CommandExecutor {

    private final HashMap<UUID, HashMap<Integer, ItemStack>> stored_inventories = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Var.NO_PLAYER);
            return true;
        }

        Player player = (Player) sender;

        if (player.hasPermission(Var.SUPER_PERMISSION)) {
            if (args.length == 0) {

                if (!Var.BUILD_PLAYERS.contains(player.getUniqueId())) {
                    Var.BUILD_PLAYERS.add(player.getUniqueId());
                    if (Var.RUNNING_SHIELD.containsKey(player)) ShieldManager.deactivate(player);
                    this.stored_inventories.put(player.getUniqueId(), this.serializeInventoryContent(player.getInventory()));
                    player.getInventory().clear();
                    player.setGameMode(GameMode.CREATIVE);
                    player.sendMessage(Var.PREFIX + "§7Du bist nun im §bBaumodus§7.");

                } else {
                    Var.BUILD_PLAYERS.remove(player.getUniqueId());
                    JoinListener.initBasic(player);
                    this.readItems(player, this.stored_inventories.get(player.getUniqueId()));
                    player.sendMessage(Var.PREFIX + "§7Du hast den Baumodus §cverlassen§7.");
                }
            } else {
                player.sendMessage(Var.PREFIX + "§cBitte benutze §6/build§c!");
            }
        } else {
            player.sendMessage(Var.NO_PERMISSION);
        }

        return true;
    }

    private HashMap<Integer, ItemStack> serializeInventoryContent(Inventory inventory) {
        HashMap<Integer, ItemStack> items = new HashMap<>();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack == null) continue;
            items.put(i, itemStack);
        }
        return items;
    }

    private void readItems(Player player, HashMap<Integer, ItemStack> items) {
        items.forEach((i, itemStack) -> player.getInventory().setItem(i, itemStack));
    }

}
