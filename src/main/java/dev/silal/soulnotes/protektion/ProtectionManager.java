package dev.silal.soulnotes.protektion;

import dev.silal.soulnotes.SoulNotes;
import org.bukkit.Location;
import org.bukkit.World;

public class ProtectionManager {

    private final SoulNotes plugin;

    private WorldGuardManager worldGuardManager;

    public ProtectionManager(SoulNotes plugin) {
        this.plugin = plugin;
        if (plugin.getServer().getPluginManager().isPluginEnabled("WorldGuard")) {
            worldGuardManager = new WorldGuardManager(plugin);
        }
    }

    public boolean isInSpawnProtection(Location location) {
        int protectionRadius = plugin.getConfiguration().getSpawnProtectionRadius();
        World world = location.getWorld();
        Location spawn = world.getSpawnLocation();

        if (!location.getWorld().equals(spawn.getWorld())) return false;

        int dx = Math.abs(location.getBlockX() - spawn.getBlockX());
        int dz = Math.abs(location.getBlockZ() - spawn.getBlockZ());

        return dx <= protectionRadius && dz <= protectionRadius;
    }

    public WorldGuardManager getWorldGuardManager() {
        return worldGuardManager;
    }
}
