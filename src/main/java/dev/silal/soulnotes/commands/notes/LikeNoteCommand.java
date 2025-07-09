package dev.silal.soulnotes.commands.notes;

import dev.silal.soulnotes.SoulNotes;
import dev.silal.soulnotes.notes.Note;
import dev.silal.soulnotes.utils.Prefix;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class LikeNoteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player p)) return false;

        if (args.length < 1) {
            p.sendMessage(Prefix.SYSTEM.key() + "Please specify the §enote id§7!");
            return true;
        }
        String noteId = args[0];

        int id;
        try {
            id = Integer.valueOf(noteId);
        } catch (Exception e) {
            p.sendMessage(Prefix.SYSTEM.key() + "§e" + noteId + "§7 is not valid! Please specify a number!");
            return true;
        }

        if (!SoulNotes.getInstance().getNoteManager().noteExists(id)) {
            p.sendMessage(Prefix.SYSTEM.key() + "A note with the id §e" + id + "§7 doesn`t exist!");
            return true;
        }

        Note note = SoulNotes.getInstance().getNoteManager().getNote(id);

        List<UUID> likes = note.getLikes();
        if (likes.contains(p.getUniqueId())) {
            p.sendMessage(Prefix.V.key() + "You removed your like from the §anote§7!");
            likes.remove(p.getUniqueId());
        } else {
            p.sendMessage(Prefix.LIKE.key() + "You liked the §anote§7!");
            likes.add(p.getUniqueId());
        }
        note.setLikes(likes);
        return true;
    }
}
