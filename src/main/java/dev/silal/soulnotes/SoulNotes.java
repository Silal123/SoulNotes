package dev.silal.soulnotes;

import dev.silal.soulnotes.commands.notes.DeleteNoteCommand;
import dev.silal.soulnotes.commands.notes.NoteCommand;
import dev.silal.soulnotes.commands.notes.tab.DeleteTabCompleter;
import dev.silal.soulnotes.config.Configuration;
import dev.silal.soulnotes.config.database.Database;
import dev.silal.soulnotes.config.database.MySQLDatabase;
import dev.silal.soulnotes.config.database.SQLiteDatabase;
import dev.silal.soulnotes.listeners.player.PlayerInteractEntityListener;
import dev.silal.soulnotes.listeners.player.PlayerInteractListener;
import dev.silal.soulnotes.notes.Note;
import dev.silal.soulnotes.notes.NoteManager;
import dev.silal.soulnotes.notes.spawner.NoteSpawner;
import dev.silal.soulnotes.placeholder.SoulNotesPlaceholder;
import dev.silal.soulnotes.protektion.ProtectionManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.xml.crypto.Data;
import java.io.File;

public final class SoulNotes extends JavaPlugin {

    private static SoulNotes instance;
    public static SoulNotes getInstance() { return instance; }

    private Configuration configuration;
    public Configuration getConfiguration() { return configuration; }

    private Database database;
    public Database getDatabase() { return database; }

    private NoteSpawner noteSpawner;
    public NoteSpawner getNoteSpawner() { return noteSpawner; }

    private NoteManager noteManager;
    public NoteManager getNoteManager() { return noteManager; }

    private ProtectionManager protectionManager;
    public ProtectionManager getProtectionManager() { return protectionManager; }

    @Override
    public void onEnable() {
        instance = this;
        if (!getDataFolder().exists()) getDataFolder().mkdirs();

        this.configuration = new Configuration(this);
        connectDatabase();

        this.noteSpawner = new NoteSpawner(this);
        this.noteManager = new NoteManager(this);

        noteSpawner.removeAllNotes();
        noteSpawner.spawnAllNotes();

        this.protectionManager = new ProtectionManager(this);

        initListener();

        getCommand("soulnote").setExecutor(new NoteCommand());

        getCommand("deletesoulnote").setExecutor(new DeleteNoteCommand());
        getCommand("deletesoulnote").setTabCompleter(new DeleteTabCompleter());

        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new SoulNotesPlaceholder(this).register();
        }

        Scheduler.start();
    }

    private void initListener() {
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new PlayerInteractEntityListener(), this);
        manager.registerEvents(new PlayerInteractListener(),this);
    }

    private void connectDatabase() {
        String type = configuration.getDatabaseType();

        if (type.equalsIgnoreCase("mysql")) {
            database = new MySQLDatabase(configuration.getMysqlHost(), configuration.getMysqlPort(), configuration.getMysqlDatabase(), configuration.getMysqlUsername(), configuration.getMysqlPassword());
        } else if (type.equalsIgnoreCase("sqlite")) {
            File file = new File(getDataFolder(), configuration.getSqliteDatabaseFile());
            database = new SQLiteDatabase(file);
        } else {
            getLogger().severe("Database type " + type + " not found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        try {
            database.connect();
            database.setup();
            getLogger().info("Database connected successfully: " + type.toUpperCase());
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().severe("Failed to connect to database");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        database.disconnect();
        Scheduler.stop();
    }
}
