package de.digitaldevs.lobby.utils;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;

/**
 * @author MerryChrismas
 * @author <a href='https://digitaldevs.de'>DigitalDevs.de</a>
 * @version 1.0.0
 */
public class WorldPreparer {

    public static void prepareWorlds() {
        Bukkit.getWorlds().forEach(world -> {
            world.setTime(0L);
            world.setThundering(false);
            world.setDifficulty(Difficulty.PEACEFUL);
            world.setGameRuleValue("doMobSpawning", "false");
            world.setGameRuleValue("doMobLoot", "false");
            world.setGameRuleValue("doDaylightCycle", "false");
        });
    }

}
