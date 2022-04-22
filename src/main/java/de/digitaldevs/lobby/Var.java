package de.digitaldevs.lobby;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author MerryChrismas
 * @author <a href='https://digitaldevs.de'>DigitalDevs.de</a>
 * @version 1.0.0
 */
public class Var {

    public static final String PREFIX = "§7[§6Lobby§7] §r",
            NO_PLAYER = "§cDazu musst du ein Spieler sein!",
            NO_PERMISSION = "§cDazu hast du keine Rechte!",
            PERMISSION_IGNORE_SHIELD = "lobby.ignore.shield",
            PERMISSION_VIP = "lobby.vip",
            PERMISSION_VIP_PLUS = "lobby.vip+",
            PERMISSION_STAFF = "lobby.staff",
            PERMISSION_DEV = "lobby.staff.dev",
            SUPER_PERMISSION = "lobby.*";


    public static final double RADIUS = 3.5D;

    public static final List<UUID> BUILD_PLAYERS = new ArrayList<>();

    public static final List<Player> FLY_PLAYERS = new ArrayList<>();

    public static final HashMap<Player, BukkitRunnable> RUNNING_SHIELD = new HashMap<>();

}
