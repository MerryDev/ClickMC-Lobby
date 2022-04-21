package de.digitaldevs.lobby.utils;

import de.digitaldevs.core.scoreboard.PersonalScoreboard;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionManagement;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.dytanic.cloudnet.driver.permission.PermissionUserGroupInfo;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

public class ScoreboardManager {

    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);

    public void setScoreboard(@NotNull final Player player) {
        final ICloudPlayer cloudPlayer = this.playerManager.getOnlinePlayer(player.getUniqueId());
        if (cloudPlayer == null) return;

        PersonalScoreboard scoreboard = new PersonalScoreboard(
                (target) -> Colorizer.bold(Colorizer.colorizeAlternately("ClickMC", ChatColor.DARK_AQUA, ChatColor.AQUA)),
                (target) -> Arrays.asList(
                        "§7§m------------",
                        "",
                        "§fRang",
                        "§7➥ §r" + this.getPlayerPermissionGroupFormattedWithColorCode(cloudPlayer),
                        " ",
                        "§fCoins",
                        "§7➥ §e0",
                        "  ",
                        "§7§m------------",
                        "§6" + cloudPlayer.getConnectedService().getServerName()

                ));

        scoreboard.addPlayer(player);
        scoreboard.updateScoreboard();
    }

    private String getPlayerPermissionGroupFormattedWithColorCode(@NotNull final ICloudPlayer cloudPlayer) {
        final IPermissionManagement permissionManagement = CloudNetDriver.getInstance().getPermissionManagement();
        final IPermissionUser user = permissionManagement.getUser(cloudPlayer.getUniqueId());

        if (user == null) return "Unknown";

        final AtomicReference<String> groupName = new AtomicReference<>("§7Spieler");
        user.getGroups().forEach(group -> {
            switch (group.getGroup()) {
                case "Admin": groupName.set("§4Admin"); break;
                case "SrDeveloper": groupName.set("§bSrDev"); break;
                case "Developer": groupName.set("§bDev"); break;
                case "Content": groupName.set("§5Content"); break;
                case "SrModerator": groupName.set("§cSrMod"); break;
                case "Moderator": groupName.set("§cMod"); break;
                case "Supporter": groupName.set("§9Sup"); break;
                case "Builder": groupName.set("§eBuilder"); break;
                case "YouTuber": groupName.set("§5YouTube"); break;
                case "VIP+": groupName.set("§aVIP§6+"); break;
                case "VIP": groupName.set("§aVIP"); break;
                default: groupName.set("§7Spieler"); break;
            }
        });
        return groupName.get();
    }

}
