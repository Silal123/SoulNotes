package dev.silal.soulnotes;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {

    public final static int ONE_TICK = 1;
    public final static int ONE_SECOND = 20 * ONE_TICK;
    public final static int ONE_MINUTE = 60 * ONE_SECOND;

    private final static List<Integer> schedulers = new ArrayList<>();
    private static boolean isFirstRun = true;

    public static void stop() {
        for (int task : schedulers) {
            Bukkit.getScheduler().cancelTask(task);
        }
    }

    public static void start() {
        /*
        * 5 * ONE_SECOND = 5 Second scheduler
        * */
        schedulers.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(SoulNotes.getInstance(), () -> {

        }, 0, ONE_SECOND * 5));

        /*
        * ONE_MINUTE scheduler
        * */
        schedulers.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(SoulNotes.getInstance(), () -> {
            SoulNotes.getInstance().getNoteSpawner().removeAllNotes();
            SoulNotes.getInstance().getNoteSpawner().spawnAllNotes();
        }, 0, ONE_MINUTE));

        /*
        * 10 * ONE_MINUTE = 10 Minute scheduler
        * */
        schedulers.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(SoulNotes.getInstance(), () -> {

        }, 0, ONE_MINUTE * 10));

        Bukkit.getScheduler().runTaskLaterAsynchronously(SoulNotes.getInstance(), () -> { isFirstRun = false; }, ONE_SECOND);
    }

}
