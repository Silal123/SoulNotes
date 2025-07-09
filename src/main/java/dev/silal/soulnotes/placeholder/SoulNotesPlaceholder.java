package dev.silal.soulnotes.placeholder;

import com.sun.jdi.IntegerType;
import dev.silal.soulnotes.SoulNotes;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Note;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SoulNotesPlaceholder extends PlaceholderExpansion {

    private final SoulNotes plugin;

    public SoulNotesPlaceholder(SoulNotes plugin) {
        this.plugin = plugin;
    }


    @Override
    public @NotNull String getIdentifier() {
        return "snotes";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Silal";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {

        return null;
    }
}
