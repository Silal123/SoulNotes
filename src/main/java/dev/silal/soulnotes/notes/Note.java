package dev.silal.soulnotes.notes;

import dev.silal.soulnotes.SoulNotes;
import dev.silal.soulnotes.config.database.util.DatabaseUtil;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Date;
import java.util.UUID;

public class Note {

    private final long id;
    private final SoulNotes plugin;

    public Note(long id, SoulNotes plugin) {
        this.id = id;
        this.plugin = plugin;
    }

    public long getId() {
        return id;
    }

    public UUID getCreator() {
        try {
            String uuid = DatabaseUtil.getText("notes", "creator", "id = " + id, plugin.getDatabase().getStatement()).orElse(null);
            if (uuid == null) return null;
            return UUID.fromString(uuid);
        } catch (Exception e) {
            return null;
        }
    }

    public Date getCreatedAt() {
        try {
            String date = DatabaseUtil.getText("notes", "created_at", "id = " + id, plugin.getDatabase().getStatement()).orElse(null);
            if (date == null) return null;
            return DatabaseUtil.getDate(date);
        } catch (Exception e) {
            return null;
        }
    }

    public String getContent() {
        try {
            return DatabaseUtil.getText("notes", "message", "id = " + id, plugin.getDatabase().getStatement()).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public Location getLocation() {
        try {
            double x = DatabaseUtil.getDouble("notes", "x", "id = " + id, plugin.getDatabase().getStatement()).orElse(0D);
            double y = DatabaseUtil.getDouble("notes", "y", "id = " + id, plugin.getDatabase().getStatement()).orElse(0D);
            double z = DatabaseUtil.getDouble("notes", "z", "id = " + id, plugin.getDatabase().getStatement()).orElse(0D);
            String w = DatabaseUtil.getText("notes", "world", "id = " + id, plugin.getDatabase().getStatement()).orElse("world");

            World world = plugin.getServer().getWorld(w);
            if (world == null) return null;

            return new Location(world, x, y, z);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
