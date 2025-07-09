package dev.silal.soulnotes.commands.notes;

import dev.silal.soulnotes.SoulNotes;
import dev.silal.soulnotes.notes.Note;
import dev.silal.soulnotes.utils.Permissions;
import dev.silal.soulnotes.utils.Prefix;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NoteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player p)) return false;

        if (SoulNotes.getInstance().getConfiguration().getNeedsPermissionToCreate() && !p.hasPermission(Permissions.CREATE_NOTES.perm())) {
            p.sendMessage(Prefix.X.key() + "You don`t have §epermission§7 to create notes!");
            return true;
        }

        if (SoulNotes.getInstance().getConfiguration().getBlacklistedWorlds().contains(p.getLocation().getWorld().getName())) {
            p.sendMessage(Prefix.X.key() + "The world §e" + p.getLocation().getWorld().getName() + "§7 is on the notes world blacklist!");
            return true;
        }

        if (SoulNotes.getInstance().getConfiguration().getSpawnProtectionEnabled() && SoulNotes.getInstance().getProtectionManager().isInSpawnProtection(p.getLocation())) {
            p.sendMessage(Prefix.X.key() + "You can`t place notes in §espawn protection§7!");
            return true;
        }

        if (SoulNotes.getInstance().getConfiguration().getWorldGuardProtectionEnabled() && SoulNotes.getInstance().getProtectionManager().getWorldGuardManager() != null && !SoulNotes.getInstance().getProtectionManager().getWorldGuardManager().canBuild(p, p.getLocation())) {
            p.sendMessage(Prefix.X.key() + "§eWorldGuard§7 restricts you to note here!");
            return true;
        }

        if (args.length > 0) {
            StringBuilder message = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                message.append(" ").append(args[i]);
            }

            if (SoulNotes.getInstance().getNoteManager().getPlayerNoteCount(p.getUniqueId()) >= SoulNotes.getInstance().getConfiguration().getPlayerMaxNotes() && !p.hasPermission(Permissions.CREATE_INFINITE_NOTES.perm())) {
                p.sendMessage(Prefix.SYSTEM.key() + "You reached your §emax notes§7 of " + SoulNotes.getInstance().getConfiguration().getPlayerMaxNotes());
                return true;
            }

            if (SoulNotes.getInstance().getConfiguration().getMinNoteDistance() != 0 && !SoulNotes.getInstance().getNoteManager().getNotesInRadius(p.getLocation(), SoulNotes.getInstance().getConfiguration().getMinNoteDistance()).isEmpty()) {
                p.sendMessage(Prefix.SYSTEM.key() + "Notes need to have a §emin distance§7 of " + SoulNotes.getInstance().getConfiguration().getMinNoteDistance() + " blocks apart!");
                return true;
            }

            SoulNotes.getInstance().getNoteManager().createNote(p.getUniqueId(), message.toString(), p.getLocation());
            p.sendMessage(Prefix.SYSTEM.key() + "Your §anote§7 was created!");
            return true;
        }

        p.sendMessage(Prefix.SYSTEM.key() + "Please add your §emessage§7!");
        return true;
    }
}
