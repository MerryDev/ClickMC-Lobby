package de.digitaldevs.lobby.commands;

import de.digitaldevs.lobby.Var;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GmCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Var.NO_PLAYER);
            return true;
        }

        Player player = (Player) sender;

        if(player.hasPermission(Var.PERMISSION_STAFF)) {
            if(args.length == 1) {
            if(args[0].equals("0")) {
                player.setGameMode(GameMode.SURVIVAL);
                player.sendMessage(Var.PREFIX + "§7Du hast deinen Spielmodus auf §eÜberlebensmodus§7 gewechselt");
            } else if(args[0].equals("1")) {
                player.setGameMode(GameMode.CREATIVE);
                player.sendMessage(Var.PREFIX + "§7Du hast deinen Spielmodus auf §eKreativmodus§7 gewechselt");
            } else if (args[0].equals("2")) {
                player.setGameMode(GameMode.ADVENTURE);
                player.sendMessage(Var.PREFIX + "§7Du hast deinen Spielmodus auf §eAbenteuermodus§7 gewechselt");
            } else if (args[0].equals("3")) {
                player.setGameMode(GameMode.SPECTATOR);
                player.sendMessage(Var.PREFIX + "§7Du hast deinen Spielmodus auf §eZuschauermodus§7 gewechselt");
            } else if(args.length >= 1) {
                player.sendMessage(Var.PREFIX + "§7Bitte benutze §e/gm §c<1, 2, 3, 0>");
                }
            } else {
                    player.sendMessage(Var.PREFIX + "§7Bitte benutze §e/gm §c<1, 2, 3, 0>");
            }
        } else {
            player.sendMessage(Var.PREFIX + Var.NO_PERMISSION);
            return true;
        }

        return true;

    }
}
