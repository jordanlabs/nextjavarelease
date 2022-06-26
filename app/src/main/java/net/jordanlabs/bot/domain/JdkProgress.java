package net.jordanlabs.bot.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class JdkProgress {
    private static final int DAYS_IN_THREE_MONTHS = 3 * 30;
    private final List<JdkRelease> jdkReleases;

    public JdkProgress(final Collection<JdkRelease> jdkReleases) {
        this.jdkReleases = new ArrayList<>(jdkReleases);
    }

    /**
     * The next Java release is either: the first
     * - release with a generally available date today or after
     * - release that doesn't have a generally available date
     * @param todayDate Date for today, UTC
     * @return The upcoming JdkRelease or empty
     */
    public Optional<JdkRelease> nextJavaRelease(final LocalDate todayDate) {
        for (final JdkRelease release : jdkReleases) {
            if (release.isReleasedOnOrAfter(todayDate)) {
                return Optional.of(release);
            }
        }
        return Optional.empty();
    }

    public ReleaseDate releaseDateOrEstimate(final JdkRelease jdkRelease, final LocalDate previousReleaseDate) {
        if (jdkRelease.hasReleaseDate()) {
            return ReleaseDate.ofActual(jdkRelease.releaseDate());
        }

        final LocalDate estimateDate = previousReleaseDate.plusDays(averageDaysBetweenReleases());
        return ReleaseDate.ofEstimate(estimateDate);
    }

    public LocalDate previousReleaseDateFor(final JdkRelease jdkRelease) {
        final var releaseIndex = jdkReleases.indexOf(jdkRelease);
        if (releaseIndex > 0) {
            return jdkReleases.get(releaseIndex - 1).releaseDate();
        }
        throw new IllegalStateException("Logic error: No previous release for jdk " + jdkRelease);
    }

    private int averageDaysBetweenReleases() {
        int numberOfReleases = 0;
        long totalDaysBetweenReleases = 0;
        for (int i = 1; i < jdkReleases.size(); i++) {
            final var lastRelease = jdkReleases.get(i - 1);
            final var currentRelease = jdkReleases.get(i);

            if (lastRelease.hasReleaseDate() && currentRelease.hasReleaseDate()) {
                final var lastDate = lastRelease.releaseDate();
                final var currentDate = currentRelease.releaseDate();
                totalDaysBetweenReleases += lastDate.until(currentDate, ChronoUnit.DAYS);
                numberOfReleases++;
            }
        }
        if (numberOfReleases == 0) {
            return DAYS_IN_THREE_MONTHS;
        }
        return Math.round((float) totalDaysBetweenReleases / numberOfReleases);
    }
}
