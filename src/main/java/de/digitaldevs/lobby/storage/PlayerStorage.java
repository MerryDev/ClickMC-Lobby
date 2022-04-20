package de.digitaldevs.lobby.storage;

import de.digitaldevs.lobby.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author MerryChrismas
 * @author <a href='https://digitaldevs.de'>DigitalDevs.de</a>
 * @version 1.0.0
 */
public class PlayerStorage {

    private final File directory = new File(Main.getInstance().getDataFolder().getPath());
    private final File file = new File(directory, "storage.yml");
    private final FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);

    public boolean isStored(UUID uuid) {
        return this.configuration.contains(uuid.toString());
    }

    public void addEntry(UUID uuid) {
        this.configuration.set(uuid.toString() + ".Visibility", 0);
        this.save();
    }

    public void storeInt(UUID uuid, String data, int i) {
        this.configuration.set(uuid.toString() + "." + data, i);
        this.save();
    }

    public int getStoredInt(UUID uuid, String data) {
        return this.configuration.getInt(uuid.toString() + "." + data);
    }

    public void create() {
        try {
            if (!this.directory.exists()) this.directory.mkdirs();
            if (this.file.exists()) this.file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void save() {
        try {
            this.configuration.save(this.file);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
