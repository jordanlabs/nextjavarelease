package net.jordanlabs.bot.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

public class JdkProgress {
    private final List<JdkRelease> jdkReleases;

    public JdkProgress(final List<JdkRelease> jdkReleases) {
        this.jdkReleases = jdkReleases;
        linkReleases(jdkReleases);
    }

    /**
     * The next Java release is either: the first
     * - release with a generally available date after today
     * - release that doesn't have a generally available date
     * @param todayDate Date for today, UTC
     * @return The upcoming JdkRelease or empty
     */
    public Optional<JdkRelease> nextJavaRelease(final LocalDate todayDate) {
        for (final JdkRelease release : jdkReleases) {
            if (release.isReleasedAfter(todayDate)) {
                return Optional.of(release);
            }
        }
        return Optional.empty();
    }

    public int averageReleaseDayOfMonth() {
        // TODO: Would be more accurate to average time between releases
        final OptionalDouble averageReleaseDay = jdkReleases
            .stream()
            .mapToInt(JdkRelease::releaseDayOfMonth)
            .average();

        return Math.round((float) averageReleaseDay.orElse(15));
    }

    private void linkReleases(final List<JdkRelease> jdkReleases) {
        for (int i = 0; i < jdkReleases.size(); i++) {
            final JdkRelease jdkRelease = jdkReleases.get(i);
            final JdkRelease previousRelease = i > 0 ? jdkReleases.get(i - 1) : null;
            final JdkRelease nextRelease = i < jdkReleases.size() - 1 ? jdkReleases.get(i + 1) : null;

            jdkRelease.previousRelease(previousRelease);
            jdkRelease.nextRelease(nextRelease);
        }
    }
}
