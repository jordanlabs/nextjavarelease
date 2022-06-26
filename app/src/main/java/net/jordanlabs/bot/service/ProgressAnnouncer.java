package net.jordanlabs.bot.service;

import net.jordanlabs.bot.domain.JdkRelease;
import net.jordanlabs.bot.domain.ReleaseDate;
import net.jordanlabs.bot.domain.Schedule;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Singleton
public class ProgressAnnouncer {
    private static final DateTimeFormatter RELEASE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int TOTAL_BLOCKS = 12;
    private static final float BLOCKS_PER_PERCENTAGE = TOTAL_BLOCKS / 100.f;
    private static final String FILLED = "⬛";
    private static final String EMPTY = "⬜";
    private static final StringBuffer sb = new StringBuffer();

    private static final String progressTweet = """
        #Java #JDK%s
        %s more days until #Java%s is released on %s%s
        %s
        """;

    private static final String releaseTweet = """
        #Java #JDK%s
        #Java%s is released today! ✨🥳☕🚀✨
        %s
        See release notes for changes: https://jdk.java.net/18/release-notes
        """;

    @Inject
    public ProgressAnnouncer() {
    }

    public String createTweet(final JdkRelease jdkRelease, final LocalDate startDate, final LocalDate todayDate, final ReleaseDate releaseDate) {
        final int totalDays = (int) startDate.until(releaseDate.releaseDate(), ChronoUnit.DAYS);
        final int progressedDays = (int) startDate.until(todayDate, ChronoUnit.DAYS);
        final String progressBar = generateProgressBar(totalDays, progressedDays);

        final Schedule schedule = jdkRelease.schedule();
        if (progressedDays < totalDays) {
            return progressTweet.formatted(
                jdkRelease.releaseNumber(),
                todayDate.until(releaseDate.releaseDate(), ChronoUnit.DAYS),
                jdkRelease.releaseNumber(),
                releaseDate.releaseDate().format(RELEASE_DATE_FORMAT),
                releaseDate.isEstimate() ? " (estimated)" : schedule.isProposed() ? " (proposed)" : "",
                progressBar
            );
        }
        return releaseTweet.formatted(
            jdkRelease.releaseNumber(),
            jdkRelease.releaseNumber(),
            progressBar
        );
    }

    // Eg. ⬛⬛⬛⬛⬛⬛⬜⬜⬜⬜⬜⬜⬜⬜⬜ 40%
    private String generateProgressBar(final int totalDays, final int progressedDays) {
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
