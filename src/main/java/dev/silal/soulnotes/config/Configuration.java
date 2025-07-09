package dev.silal.soulnotes.config;

import dev.silal.soulnotes.SoulNotes;
import dev.silal.soulnotes.utils.config.ConfigValue;
import dev.silal.soulnotes.utils.config.ListConfigValue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Configuration extends dev.silal.soulnotes.utils.config.Configuration {
    private final SoulNotes plugin;

    private final ConfigValue<String> databaseType = new ConfigValue<>("database.type", String.class, this, "sqlite");

    private final ConfigValue<String> sqliteDatabaseFile = new ConfigValue<>("database.sqlite.file", String.class, this, "database.db");

    private final ConfigValue<String> mysqlHost = new ConfigValue<>("database.mysql.host", String.class, this, "127.0.0.1");
    private final ConfigValue<Integer> mysqlPort = new ConfigValue<>("database.mysql.port", Integer.class, this, 3306);
    private final ConfigValue<String> mysqlDatabase = new ConfigValue<>("database.mysql.database", String.class, this, "soulnotes");
    private final ConfigValue<String> mysqlUsername = new ConfigValue<>("database.mysql.user", String.class, this);
    private final ConfigValue<String> mysqlPassword = new ConfigValue<>("database.mysql.password", String.class, this);

    private final ConfigValue<Integer> minNoteDistance = new ConfigValue<>("note.distance", Integer.class, this, 5);
    private final ConfigValue<Integer> playerNotesMax = new ConfigValue<>("notes.player.max", Integer.class, this, 50);

    private final ConfigValue<Boolean> needsPermissionToCreate = new ConfigValue<>("notes.create.permission", Boolean.class, this, false);

    private final ListConfigValue<String> blackListedWorlds = new ListConfigValue<>("notes.worlds.blacklist", String.class, this, List.of("no_notes"));

    private final ConfigValue<Boolean> spawnProtectionEnabled = new ConfigValue<>("protection.spawn.enabled", Boolean.class, this, true);
    private final ConfigValue<Integer> spawnProtection = new ConfigValue<>("protection.spawn.radius", Integer.class, this, 16);
    private final ConfigValue<Boolean> worldGuardProtectionEnabled = new ConfigValue<>("protection.worldguard", Boolean.class, this, true);

    public Configuration(SoulNotes plugin) {
        super(plugin.getDataFolder().getPath() + File.separator + "config.yml");
        this.plugin = plugin;
    }

    public String getDatabaseType() {
        return databaseType.get();
    }

    public String getSqliteDatabaseFile() {
        return sqliteDatabaseFile.get();
    }

    public String getMysqlHost() {
        return mysqlHost.get();
    }

    public int getMysqlPort() {
        return mysqlPort.get();
    }

    public String getMysqlDatabase() {
        return mysqlDatabase.get();
    }

    public String getMysqlUsername() {
        return mysqlUsername.get();
    }

    public String getMysqlPassword() {
        return mysqlPassword.get();
    }

    public int getMinNoteDistance() {
        return minNoteDistance.get();
    }

    public int getPlayerMaxNotes() {
        return playerNotesMax.get();
    }

    public boolean getNeedsPermissionToCreate() {
        return needsPermissionToCreate.get();
    }

    public List<String> getBlacklistedWorlds() {
        return blackListedWorlds.get();
    }

    public boolean getSpawnProtectionEnabled() {
        return spawnProtectionEnabled.get();
    }

    public int getSpawnProtectionRadius() {
        return spawnProtection.get();
    }

    public boolean getWorldGuardProtectionEnabled() {
        return worldGuardProtectionEnabled.get();
    }
}
