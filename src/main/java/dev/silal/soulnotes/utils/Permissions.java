package dev.silal.soulnotes.utils;

public enum Permissions {


    CREATE_NOTES("soulnotes.create"),
    MANAGE_NOTES("soulnotes.manage"),
    CREATE_INFINITE_NOTES("soulnotes.create.infinite");

    Permissions(String perm) {
        this.perm = perm;
    }
    private final String perm;
    public String perm() { return perm; }
}
