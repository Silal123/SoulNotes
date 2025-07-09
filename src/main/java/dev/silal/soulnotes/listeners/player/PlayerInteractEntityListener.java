package dev.silal.soulnotes.listeners.player;

import dev.silal.soulnotes.SoulNotes;
import dev.silal.soulnotes.notes.Note;
import dev.silal.soulnotes.utils.Permissions;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.awt.*;
import java.text.SimpleDateFormat;

public class PlayerInteractEntityListener implements Listener {

    public static void addPagedContent(BookMeta meta, String content) {
        final int MAX_CHARS_PER_PAGE = 256;

        for (int i = 0; i < content.length(); i += MAX_CHARS_PER_PAGE) {
            int end = Math.min(i + MAX_CHARS_PER_PAGE, content.length());
            String page = content.substring(i, end);
            meta.addPage(page);
        }
    }

    @EventHandler
    public void onPlayerInteractEntityListener(PlayerInteractAtEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (!entity.getScoreboardTags().contains("note_display")) return;

        long id = -1;
        for (String tag : entity.getScoreboardTags()) {
            if (!tag.startsWith("note_id_")) continue;
            String noteId = tag.split("_")[2];
            try {
                id = Integer.valueOf(noteId);
            } catch (Exception e) { return; }
        }

        if (id == -1) return;

        try {
            Note note = SoulNotes.getInstance().getNoteManager().getNote(id);

            OfflinePlayer player = null;
            if (note.getCreator() != null) {
                player = SoulNotes.getInstance().getServer().getOfflinePlayer(note.getCreator());
            }

            ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
            BookMeta meta = (BookMeta) book.getItemMeta();

            String noteText = "Author: §2" + (player != null && player.getName() != null ? player.getName() : "Unknown") + "\n§rCreated at: §6" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(note.getCreatedAt()) + "\n\n§r§lNote:§r\n" + note.getContent();

            addPagedContent(meta, noteText);

            if ((note.getCreator() != null && note.getCreator().equals(event.getPlayer().getUniqueId())) || event.getPlayer().hasPermission(Permissions.MANAGE_NOTES.perm())) {
                TextComponent component = new TextComponent("§c[Delete]");
                component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/deletesoulnote " + id));
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§7Click to delete this note")));

                TextComponent creator = new TextComponent("\nCreator: §2" + (player != null && player.getName() != null ? player.getName() : "Unknown") + " (" + note.getCreator() + ")");
                TextComponent createdAt = new TextComponent("\nCreated at: §6" + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(note.getCreatedAt()));

                meta.spigot().addPage(new BaseComponent[]{ component, creator, createdAt });
            }

            meta.setAuthor(player == null && player.getName() == null ? "Unknown" : player.getName());
            book.setItemMeta(meta);

            event.getPlayer().openBook(book);
        } catch (Exception e) {

        }
    }

}
