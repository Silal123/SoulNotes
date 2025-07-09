package dev.silal.soulnotes.utils.database;

public enum ColType {
    INTEGER,
    BIGINT,
    TEXT,
    REAL,
    BLOB;

    @Override
    public String toString() {
        return name();
    }
}
