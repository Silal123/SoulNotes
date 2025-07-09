package dev.silal.soulnotes.utils;

public enum Prefix {

    TUTORIAL("§8(§bTutorial§8) §6Fred§8: §7", false),
    STAR("§e§l[⭐] §7", false),
    REWARD("§8(§cReward§8) §6George§8: §7", false),
    TELEPORT("§8(§dTeleport§8) §6Francis§8: §7", false),
    LEVEL_UP("§8(§aLeveler§8) §6Luisa§8: §7", false),
    HEY("§c§lHey! ", false),
    X("§c§l[x] ", false),
    V("§a§l[✔] ", false),
    SYSTEM("§5§lS§d§lN ", false);

    Prefix(String key, boolean prefix) {
        this.key = key;
        this.prefix = prefix;
    }

    private final String key;
    private final boolean prefix;

    public final String split = "§8: §7";

    public String key() { return key + (prefix ? split : "§7"); }


}
