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

public class ScoreboardManager {

    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);

    public void setScoreboard(@NotNull final Player player) {
        final ICloudPlayer cloudPlayer = this.playerManager.getOnlinePlayer(player.getUniqueId());
        if (cloudPlayer == null) return;

        PersonalScoreboard scoreboard = new PersonalScoreboard(
                (target) -> Colorizer.colorizeAlternately("ClickMC", ChatColor.DARK_AQUA, ChatColor.AQUA),
                (target) -> Arrays.asList(
                        "§7§m------------",
                        "",
                        "§fRang",
                        "§7➥ §r",
                        " ",
                        "§fCoins",
                        "§7➥ §e0",
                        "  ",
                        "§7§m------------",
                        "§6" + cloudPlayer.getConnectedService().getServerName()

                ));

        printData(cloudPlayer);

        scoreboard.addPlayer(player);
        scoreboard.updateScoreboard();
    }

    private void printData(ICloudPlayer cloudPlayer) {
        IPermissionManagement management = CloudNetDriver.getInstance().getPermissionManagement();
        IPermissionUser user = management.getUser(cloudPlayer.getUniqueId());
        if (user == null) return;
        Collection<PermissionUserGroupInfo> groups = user.getGroups();
        System.out.println(groups);
    }

}
