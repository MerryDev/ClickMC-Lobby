package de.digitaldevs.lobby;

import de.digitaldevs.lobby.commands.BuildCommand;
import de.digitaldevs.lobby.commands.SetCommand;
import de.digitaldevs.lobby.listener.*;
import de.digitaldevs.lobby.storage.PlayerStorage;
import de.digitaldevs.lobby.utils.WorldPreparer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author MerryChrismas
 * @author mmmaxxx45
 * @author <a href='https://digitaldevs.de'>DigitalDevs.de</a>
 * @version 1.0.0
 */
public class Main extends JavaPlugin {

    @Getter static Main instance;
    @Getter PlayerStorage playerStorage;

    @Override
    public void onEnable() {
        instance = this;

        WorldPreparer.prepareWorlds();
        this.initPlayerStorage();
        this.registerCommands();
        this.registerListener(Bukkit.getPluginManager());
        Bukkit.getConsoleSender().sendMessage(Var.PREFIX + "Das Plugin wurde §aaktiviert§r!");
    }

    @Override
    public void onDisable() {
        instance = null;
        Bukkit.getConsoleSender().sendMessage(Var.PREFIX + "Das Plugin wurde §cdeaktiviert§r!");
    }

    private void initPlayerStorage() {
        this.playerStorage = new PlayerStorage();
        this.playerStorage.create();
    }

    private void registerCommands() {
        this.getCommand("set").setExecutor(new SetCommand());
        this.getCommand("build").setExecutor(new BuildCommand());
    }

    private void registerListener(PluginManager pluginManager) {
        pluginManager.registerEvents(new JoinListener(), this);
        pluginManager.registerEvents(new ProtectionListener(), this);
        pluginManager.registerEvents(new InteractListener(), this);
        pluginManager.registerEvents(new ClickListener(), this);
        pluginManager.registerEvents(new MoveListener(), this);
        pluginManager.registerEvents(new QuitListener(), this);
        pluginManager.registerEvents(new FishListener(), this);
        pluginManager.registerEvents(new TeleportListener(), this);
    }
}