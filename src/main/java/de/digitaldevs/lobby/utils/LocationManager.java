package de.digitaldevs.lobby.utils;

import de.digitaldevs.lobby.Main;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * @author MerryChrismas
 * @author <a href='https://digitaldevs.de'>DigitalDevs.de</a>
 * @version 1.0.0
 */

@RequiredArgsConstructor
public class LocationManager {

    private final File directory = new File(Main.getInstance().getDataFolder().getPath());
    private final File file = new File(this.directory, "locations.yml");
    private final FileConfiguration configuration = YamlConfiguration.loadConfiguration(this.file);

    private final String name;
    private final Location location;

    public LocationManager(String name) {
        this(name, null);
    }

    public void saveLocation() {
        this.configuration.set(this.name + ".X", this.location.getX());
        this.configuration.set(this.name + ".Y", this.location.getY());
        this.configuration.set(this.name + ".Z", this.location.getZ());
        this.configuration.set(this.name + ".Yaw", this.location.getYaw());
        this.configuration.set(this.name + ".Pitch", this.location.getPitch());
        this.configuration.set(this.name + ".World", this.location.getWorld().getName());
        this.save();
    }

    public Location getLocation() {
        if (this.configuration.getConfigurationSection(this.name) == null) return null;
        double x = this.configuration.getDouble(this.name + ".X"), y = this.configuration.getDouble(this.name + ".Y"), z = this.configuration.getDouble(this.name + ".Z");
        float yaw = (float) this.configuration.getDouble(this.name + ".Yaw"), pitch = (float) this.configuration.getDouble(this.name + ".Pitch");
        World world = Bukkit.getWorld(this.configuration.getString(this.name + ".World"));
        if (world == null) return null;
        return new Location(world, x, y, z, yaw, pitch);
    }

    @SneakyThrows
    private void save() {
        this.configuration.save(this.file);
    }
}
