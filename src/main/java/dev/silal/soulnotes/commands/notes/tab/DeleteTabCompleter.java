package dev.silal.soulnotes.commands.notes.tab;

import dev.silal.soulnotes.SoulNotes;
import dev.silal.soulnotes.utils.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class DeleteTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        List<String> result = new ArrayList<>();

        if (args.length == 1) {
            if ("all".startsWith(args[0]) && sender.hasPermission(Permissions.MANAGE_NOTES.perm())) result.add("all");
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("all") && sender.hasPermission(Permissions.MANAGE_NOTES.perm())) {
            SoulNotes.getInstance().getServer().getOnlinePlayers().forEach(player -> {
                result.add(player.getName());
            });
        }

        return result;
    }
}
