package de.digitaldevs.lobby.commands;

import de.digitaldevs.lobby.Var;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //Checks if the commandsender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(Var.NO_PLAYER);
            return true;
        }

        Player player = (Player) sender;

        //Checks if player has the VIP+ Permission
        if(player.hasPermission(Var.PERMISSION_VIP_PLUS)) {
            if(args.length == 0) {
                if(!player.getAllowFlight()) {
                    player.setAllowFlight(true);
                    player.setFlying(true);
                    player.sendMessage(Var.PREFIX + "§7Du hast den Flugmodus §aaktiviert!");
                } else {
                    player.setAllowFlight(false);
                    player.setFlying(false);
                    player.sendMessage(Var.PREFIX + "§7Du hast den Flugmodus §cdeaktiviert!");
                }
            } else {
                player.sendMessage(Var.PREFIX + "§7Bitte benutze §e/fly");
            }
        } else {
            player.sendMessage(Var.PREFIX + Var.NO_PERMISSION);
            return true;
        }
        return true;
    }
}
