package dev.silal.soulnotes.commands.notes;

import dev.silal.soulnotes.SoulNotes;
import dev.silal.soulnotes.notes.Note;
import dev.silal.soulnotes.utils.Permissions;
import dev.silal.soulnotes.utils.Prefix;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.PufferFish;

public class DeleteNoteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (args.length < 1) {
            sender.sendMessage(Prefix.X.key() + "Please specify your §enote id§7!");
            return true;
        }
        String id = args[0];

        if (id.equalsIgnoreCase("all")) {
            if (!sender.hasPermission(Permissions.MANAGE_NOTES.perm())) {
                sender.sendMessage(Prefix.X.key() + "You are §enot permitted§7 to delete all notes!");
                return true;
            }

            if (args.length > 1) {
                String playerName = args[1];
                OfflinePlayer player = SoulNotes.getInstance().getServer().getOfflinePlayer(playerName);

                if (player == null || player.getName() == null || player.getUniqueId() == null) {
                    sender.sendMessage(Prefix.SYSTEM.key() + "Player §e" + playerName + "§7 not found!");
                    return true;
                }

                SoulNotes.getInstance().getNoteManager().getAllNotes(player.getUniqueId()).forEach(note -> {
                    SoulNotes.getInstance().getNoteSpawner().deleteNote(note.getId());
                    SoulNotes.getInstance().getNoteManager().deleteNote(note.getId());
                });

                sender.sendMessage(Prefix.SYSTEM.key() + "Deleted all notes of §e" + playerName);
                return true;
            }

            SoulNotes.getInstance().getNoteSpawner().removeAllNotes();
            SoulNotes.getInstance().getNoteManager().getAllNotes().forEach(note -> {
                SoulNotes.getInstance().getNoteManager().deleteNote(note.getId());
            });

            sender.sendMessage(Prefix.SYSTEM.key() + "Deleted §eall§7 created notes");
            return true;
        }

        long intId;
        try {
            intId = Integer.valueOf(id);
        } catch (Exception e) {
            sender.sendMessage(Prefix.SYSTEM.key() + "Please use §enumber or all§7");
            return true;
        }

        if (!SoulNotes.getInstance().getNoteManager().noteExists(intId)) {
            sender.sendMessage(Prefix.SYSTEM.key() + "No note with the id §e" + id + "§7 found!");
            return true;
        }

        Note note = SoulNotes.getInstance().getNoteManager().getNote(intId);

        if (note == null) {
            sender.sendMessage(Prefix.SYSTEM.key() + "No note with the id §e" + id + "§7 found!");
            return true;
        }

        boolean isOwner = false;
        if (sender instanceof Player p) {
            if (p.getUniqueId().equals(note.getCreator())) {
                isOwner = true;
            }
        }

        if (!sender.hasPermission(Permissions.MANAGE_NOTES.perm()) && !isOwner) {
            sender.sendMessage(Prefix.X.key() + "You are §enot permitted§7 to delete this note!");
            return true;
        }

        SoulNotes.getInstance().getNoteManager().deleteNote(intId);
        sender.sendMessage(Prefix.SYSTEM.key() + "Deleted note with id §e" + id);
        return true;
    }
}
