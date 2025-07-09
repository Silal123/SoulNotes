package dev.silal.soulnotes;

import dev.silal.soulnotes.commands.notes.DeleteNoteCommand;
import dev.silal.soulnotes.commands.notes.LikeNoteCommand;
import dev.silal.soulnotes.commands.notes.NoteCommand;
import dev.silal.soulnotes.commands.notes.tab.DeleteTabCompleter;
import dev.silal.soulnotes.config.Configuration;
import dev.silal.soulnotes.config.database.Database;
import dev.silal.soulnotes.config.database.MySQLDatabase;
import dev.silal.soulnotes.config.database.SQLiteDatabase;
import dev.silal.soulnotes.config.database.util.DatabaseUtil;
import dev.silal.soulnotes.listeners.player.PlayerInteractEntityListener;
import dev.silal.soulnotes.listeners.player.PlayerInteractListener;
import dev.silal.soulnotes.notes.Note;
import dev.silal.soulnotes.notes.NoteManager;
import dev.silal.soulnotes.notes.spawner.NoteSpawner;
import dev.silal.soulnotes.placeholder.SoulNotesPlaceholder;
import dev.silal.soulnotes.protektion.ProtectionManager;
import dev.silal.soulnotes.utils.JsonManager;
import dev.silal.soulnotes.utils.Metrics;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

    private Metrics metrics;

    @Override
    public void onEnable() {
        instance = this;
        if (!getDataFolder().exists()) getDataFolder().mkdirs();

        getLogger().info("Starting SoulNotes...");

        this.configuration = new Configuration(this);
        connectDatabase();

        this.noteSpawner = new NoteSpawner(this);
        this.noteManager = new NoteManager(this);

        getLogger().info("Creating all Notes...");
        noteSpawner.removeAllNotes();
        noteSpawner.spawnAllNotes();

        this.protectionManager = new ProtectionManager(this);
        this.metrics = new Metrics(this, 26445);

        metrics.addCustomChart(new Metrics.SingleLineChart("average_notes", () -> {
            try {
                return Math.toIntExact(DatabaseUtil.countEntries("notes", null, database.getStatement()));
            } catch (Exception e) {}
            return 0;
        }));

        initListener();
        initCommands();

        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new SoulNotesPlaceholder(this).register();
        }

        Scheduler.start();

        getLogger().info("Checking for updates...");
        checkForUpdates();
    }

    private void checkForUpdates() {
        getServer().getScheduler().runTaskAsynchronously(this, () -> {
            try {
                String apiUrl = "https://api.github.com/repos/Silal123/SoulNotes/releases/latest";
                HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
                connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder json = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }

                JsonManager release = new JsonManager(json.toString());
                String latestVersion = release.getString("tag_name").replace("v", "");

                String currentVersion = getDescription().getVersion();

                if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                    getLogger().warning("There is a new version available: " + latestVersion);
                    getLogger().warning("You are using: " + currentVersion);
                } else {
                    getLogger().info("Your plugin is up to date!");
                }

            } catch (Exception e) {
                getLogger().warning("Error while checking for updates: " + e.getMessage());
            }
        });
    }

    private void initListener() {
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new PlayerInteractEntityListener(), this);
        manager.registerEvents(new PlayerInteractListener(),this);
    }

    private void initCommands() {
        getCommand("soulnote").setExecutor(new NoteCommand());
        getCommand("deletesoulnote").setExecutor(new DeleteNoteCommand());
        getCommand("deletesoulnote").setTabCompleter(new DeleteTabCompleter());
        getCommand("likesoulnote").setExecutor(new LikeNoteCommand());
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
