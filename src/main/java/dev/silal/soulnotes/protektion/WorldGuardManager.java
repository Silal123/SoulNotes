package dev.silal.soulnotes.protektion;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import dev.silal.soulnotes.SoulNotes;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WorldGuardManager {

    private final SoulNotes plugin;

    public WorldGuardManager(SoulNotes plugin) {
        this.plugin = plugin;
    }

    public boolean canBuild(Player player, Location location) {
        if (!plugin.getServer().getPluginManager().isPluginEnabled("WorldGuard")) return true;
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        com.sk89q.worldedit.world.World wgWorld = BukkitAdapter.adapt(location.getWorld());
        RegionManager regionManager = container.get(wgWorld);

        if (regionManager == null) return true;

        BlockVector3 vec = BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        ApplicableRegionSet regions = regionManager.getApplicableRegions(vec);

        return regions.testState(WorldGuardPlugin.inst().wrapPlayer(player), Flags.BUILD);
    }

}
