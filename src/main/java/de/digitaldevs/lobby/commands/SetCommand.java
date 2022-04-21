package de.digitaldevs.lobby.commands;

import de.digitaldevs.lobby.Var;
import de.digitaldevs.lobby.utils.LocationManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author MerryChrismas
 * @author <a href='https://digitaldevs.de'>DigitalDevs.de</a>
 * @version 1.0.0
 */
public class SetCommand implements CommandExecutor, TabCompleter {

    private final String[] allCompletions = new String[]{"BedWars", "GunGame", "MLGRush", "BowBash", "FlashBlock","Spawn"};

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Var.NO_PLAYER);
            return true;
        }

        Player player = (Player) sender;

        if (player.hasPermission(Var.SUPER_PERMISSION)) {
            if (args.length == 1) {
                List<String> lowerAllowed = new ArrayList<>();
                for (String s : this.allCompletions) lowerAllowed.add(s.toLowerCase());

                String loweredArg = args[0].toLowerCase();
                if (lowerAllowed.contains(loweredArg)) {
                    new LocationManager(loweredArg, player.getLocation()).saveLocation();
                    if (!loweredArg.equals("spawn")) {
                        String modi = Arrays.stream(this.allCompletions).filter(s -> s.equalsIgnoreCase(loweredArg)).collect(Collectors.joining());
                        player.sendMessage(Var.PREFIX + "§7Du hast §e" + modi + " §7erfolgreich §agesetzt§7.");
                    } else player.sendMessage(Var.PREFIX + "§7Du hast den §eSpawn §7erfolgreich §agesetzt§7.");

                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String s : this.allCompletions) {
                        if (!s.equals(this.allCompletions[this.allCompletions.length - 1]))
                            stringBuilder.append(s).append(", ");
                        else stringBuilder.append(s);
                    }
                    player.sendMessage(Var.PREFIX + "§cEs stehen folgende Modi zur Auswahl: " + stringBuilder);
                }

            } else {
                player.sendMessage(Var.PREFIX + "§cBitte benutze §6/set <Name>§c!");
            }
        } else {
            player.sendMessage(Var.NO_PERMISSION);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            if (args[0] == null || args[0].equalsIgnoreCase("")) completions.addAll(Arrays.asList(this.allCompletions));
            else {
                for (String possible : this.allCompletions) {
                    if (args[0].toLowerCase().charAt(0) == possible.toLowerCase().charAt(0)) completions.add(possible);
                }
            }
        }

        return completions;
    }
}
