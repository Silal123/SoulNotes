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

        ArmorStand title = location.getWorld().spawn(location.clone(), ArmorStand.class);
        title.setVisible(false);
        title.setSmall(false);
        title.setMarker(false);
        title.setGravity(false);
        title.setCustomNameVisible(true);
        title.setCustomName(player == null || player.getName() == null ? "§8✎§7 Read message" : "§8✎§7 Read message of §e" + player.getName());
        title.setInvulnerable(true);

        title.addScoreboardTag("note_display");
        title.addScoreboardTag("note_title");
        title.addScoreboardTag("note_id_" + id);

        Date created = note.getCreatedAt();
        ArmorStand createdAt = location.getWorld().spawn(location.clone().add(0, -0.4, 0), ArmorStand.class);
        createdAt.setVisible(false);
        createdAt.setSmall(false);
        createdAt.setMarker(false);
        createdAt.setGravity(false);
        createdAt.setInvisible(true);
        createdAt.setCustomNameVisible(true);
        createdAt.setInvulnerable(true);
        createdAt.setCustomName((created == null ? "§eUnknown" : "§a" + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(created)));

        createdAt.addScoreboardTag("note_display");
        createdAt.addScoreboardTag("note_created");
        createdAt.addScoreboardTag("note_id_" + id);


        ArmorStand book = location.getWorld().spawn(location.clone().add(0, -1.6, -0.7), ArmorStand.class);
        book.setVisible(false);
        book.setMarker(true);
        book.setGravity(false);
        book.setInvulnerable(true);

        book.getEquipment().setHelmet(new ItemStack(Material.WRITTEN_BOOK));

        book.setHeadPose(new EulerAngle(Math.toRadians(90), 0, 0));

        book.addScoreboardTag("note_display");
        book.addScoreboardTag("note_book");
        book.addScoreboardTag("note_id_" + id);
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
