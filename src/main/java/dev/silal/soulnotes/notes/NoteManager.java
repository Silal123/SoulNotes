package dev.silal.soulnotes.notes;

import dev.silal.soulnotes.SoulNotes;
import dev.silal.soulnotes.config.database.util.DatabaseEntry;
import dev.silal.soulnotes.config.database.util.DatabaseUtil;
import org.bukkit.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class NoteManager {

    private final SoulNotes plugin;

    public NoteManager(SoulNotes plugin) {
        this.plugin = plugin;
    }

    public boolean noteExists(long id) {
        try {
            return DatabaseUtil.entryExists("notes", "id = " + id, plugin.getDatabase().getStatement());
        } catch (Exception e) {
            return false;
        }
    }

    public long getPlayerNoteCount(UUID uuid) {
        try {
            return DatabaseUtil.countEntries("notes", "uuid = " + uuid.toString(), plugin.getDatabase().getStatement());
        } catch (Exception e) {
            return 0;
        }
    }

    public Note getNote(long id) {
        if (!noteExists(id)) return null;
        return new Note(id, plugin);
    }

    public void deleteNote(long id) {
        if (!noteExists(id)) return;
        DatabaseUtil.delete("notes", "id = " + id, plugin.getDatabase().getStatement());
        plugin.getNoteSpawner().deleteNote(id);
    }

    public Note createNote(UUID creator, String message, Location location) {
        Date now = Timestamp.valueOf(LocalDateTime.now());
        try {
            PreparedStatement stmt = plugin.getDatabase().getConnection().prepareStatement(
                    "INSERT INTO notes (`creator`, `message`, `created_at`, `x`, `y`, `z`, `world`) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            stmt.setString(1, creator.toString());
            stmt.setString(2, message);
            stmt.setString(3, DatabaseUtil.getDateString(now));
            stmt.setDouble(4, location.getX());
            stmt.setDouble(5, location.getY());
            stmt.setDouble(6, location.getZ());
            stmt.setString(7, location.getWorld().getName());

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                long id = generatedKeys.getLong(1);
                Note note = getNote(id);
                if (note != null && plugin.getNoteSpawner() != null) {
                    plugin.getNoteSpawner().spawnNote(note);
                }
                return note;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Note> getAllNotes() {
        List<Note> finalList = new ArrayList<>();
        try {
            for (DatabaseEntry entry : DatabaseUtil.getAllEntries("notes", null, plugin.getDatabase().getStatement())) {
                int id = entry.getInt("id");
                if (!noteExists(id)) continue;
                finalList.add(getNote(id));
            }
        } catch (Exception e) {}
        return finalList;
    }

    public List<Note> getAllNotes(UUID uuid) {
        List<Note> finalList = new ArrayList<>();
        try {
            for (DatabaseEntry entry : DatabaseUtil.getAllEntries("notes", "creator = '" + uuid.toString() + "'", plugin.getDatabase().getStatement())) {
                int id = entry.getInt("id");
                if (!noteExists(id)) continue;
                finalList.add(getNote(id));
            }
        } catch (Exception e) {}
        return finalList;
    }

    public List<Note> getNotesInRadius(Location center, double radius) {
        List<Note> result = new ArrayList<>();

        String worldName = center.getWorld().getName();
        double radiusSquared = radius * radius;

        try {
            String query = "SELECT id, x, y, z, world FROM notes WHERE world = '" + worldName.replace("'", "''") + "'";
            ResultSet rs = plugin.getDatabase().getStatement().executeQuery(query);

            while (rs.next()) {
                double x = rs.getDouble("x");
                double y = rs.getDouble("y");
                double z = rs.getDouble("z");
                String world = rs.getString("world");

                double dx = x - center.getX();
                double dy = y - center.getY();
                double dz = z - center.getZ();
                double distanceSquared = dx * dx + dy * dy + dz * dz;

                if (distanceSquared <= radiusSquared) {
                    int id = rs.getInt("id");
                    result.add(getNote(id));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
