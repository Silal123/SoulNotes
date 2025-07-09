package dev.silal.soulnotes.notes.spawner;

import dev.silal.soulnotes.SoulNotes;
import dev.silal.soulnotes.notes.Note;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class NoteSpawner {

    private final SoulNotes plugin;

    public NoteSpawner(SoulNotes plugin) {
        this.plugin = plugin;
    }

    public void deleteNote(long id) {
        Bukkit.getServer().getWorlds().forEach(world -> {
            world.getEntities().stream().filter(entity -> entity.getScoreboardTags().contains("note_id_" + id) && entity.getScoreboardTags().contains("note_display")).forEach(entity -> entity.remove());
        });
    }

    public void spawnNote(Note note) {
        long id = note.getId();
        Location location = note.getLocation();
        if (location == null || location.getWorld() == null) return;

        UUID creator = note.getCreator();
        OfflinePlayer player = null;
        if (creator != null) player = plugin.getServer().getOfflinePlayer(creator);

        if (plugin.getConfiguration().getHologramTextEnabled()) {
            TextDisplay title = location.getWorld().spawn(location.clone().add(0, 1.5, 0), TextDisplay.class);
            title.setText(player == null || player.getName() == null ? "§8✎§7 Read message" : "§8✎§7 Read message of §e" + player.getName());
            title.setAlignment(TextDisplay.TextAlignment.CENTER);
            title.setDefaultBackground(false);
            title.setPersistent(true);
            title.setBillboard(Display.Billboard.VERTICAL);

            title.addScoreboardTag("note_display");
            title.addScoreboardTag("note_title");
            title.addScoreboardTag("note_id_" + id);

            if (plugin.getConfiguration().getHologramCreatedEnabled()) {
                Date created = note.getCreatedAt();
                TextDisplay createdAt = location.getWorld().spawn(location.clone().add(0, 1.2, 0), TextDisplay.class);
                createdAt.setText(created == null ? "§eUnknown" : "§a" + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(created));
                createdAt.setAlignment(TextDisplay.TextAlignment.CENTER);
                createdAt.setDefaultBackground(false);
                createdAt.setPersistent(true);
                createdAt.setBillboard(Display.Billboard.VERTICAL);

                createdAt.addScoreboardTag("note_display");
                createdAt.addScoreboardTag("note_created");
                createdAt.addScoreboardTag("note_id_" + id);
            }
        }

        ItemDisplay book = location.getWorld().spawn(location.clone().add(0, 0.05, 0), ItemDisplay.class);
        book.setItemStack(new ItemStack(Material.WRITTEN_BOOK));
        book.setRotation(0, 90);
        book.setGravity(false);
        book.setPersistent(true);
        book.setInvulnerable(true);

        book.setTransformation(new Transformation(new Vector3f(0, 0, 0), new AxisAngle4f(), new Vector3f(0.5f, 0.5f, 0.5f), new AxisAngle4f()));

        book.addScoreboardTag("note_display");
        book.addScoreboardTag("note_book");
        book.addScoreboardTag("note_id_" + id);

        Interaction interaction = location.getWorld().spawn(location, Interaction.class);
        interaction.setInteractionHeight(0.5f);
        interaction.setInteractionWidth(0.5f);

        interaction.addScoreboardTag("note_display");
        interaction.addScoreboardTag("note_interaction");
        interaction.addScoreboardTag("note_id_" + id);
    }

    public void removeAllNotes() {
        plugin.getServer().getWorlds().forEach(world -> {
            world.getEntities().forEach(entity -> {
                if (entity.getScoreboardTags().contains("note_display")) {
                    entity.remove();
                }
            });
        });
    }

    public void spawnAllNotes() {
        for (Note note : plugin.getNoteManager().getAllNotes()) {
            this.spawnNote(note);
        }
    }

}
