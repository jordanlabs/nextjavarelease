package net.jordanlabs.bot.service;

import net.jordanlabs.bot.domain.ReleaseDate;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ProgressAnnouncer {
    private static final int TOTAL_BLOCKS = 15;
    private static final float BLOCKS_PER_PERCENTAGE = TOTAL_BLOCKS / 100.f;
    private static final String FILLED = "⬛";
    private static final String EMPTY = "⬜";
    private static final StringBuffer sb = new StringBuffer();

    // ⬛⬛⬛⬛⬛⬛⬜⬜⬜⬜⬜⬜⬜⬜⬜ 40%
    public String generateProgressBar(final LocalDate startDate, final LocalDate todayDate, final ReleaseDate releaseDate) {
        final int totalDays = (int) startDate.until(releaseDate.releaseDate(), ChronoUnit.DAYS);
        final int progressedDays = (int) startDate.until(todayDate, ChronoUnit.DAYS);

        final int percentage = (int) (progressedDays / (float) totalDays * 100);
        final int filledBlocks = Math.round(percentage * BLOCKS_PER_PERCENTAGE);
        final int emptyBlocks = Math.max(TOTAL_BLOCKS - filledBlocks, 0);

        sb.setLength(0);
        sb.append(FILLED.repeat(filledBlocks));
        sb.append(EMPTY.repeat(emptyBlocks));
        sb.append(" ");
        sb.append(percentage);
        sb.append("%");
        return sb.toString();
    }
}
