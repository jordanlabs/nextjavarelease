package net.jordanlabs.bot.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class JdkRelease {
    public static final LocalDate NO_RELEASE_DATE = LocalDate.EPOCH;
    private final String releaseUrl;
    private final String latestMilestone;
    private final String releaseNumber;
    private final Set<Milestone> milestones = new TreeSet<>();
    private final Set<Feature> features = new TreeSet<>();
    private final LocalDateTime pageLastUpdated;

    private JdkRelease previousRelease;
    private JdkRelease nextRelease;

    public JdkRelease(final String releaseUrl, final String releaseNumber, final String latestMilestone, final List<Milestone> milestones, final List<Feature> features, final LocalDateTime pageLastUpdated) {
        this.releaseUrl = releaseUrl;
        this.releaseNumber = releaseNumber;
        this.latestMilestone = latestMilestone;
        this.milestones.addAll(milestones);
        this.features.addAll(features);
        this.pageLastUpdated = pageLastUpdated;
    }

    public boolean isReleasedAfter(final LocalDate date) {
        if (milestones.isEmpty()) {
            return true;
        }
        for (final Milestone milestone : milestones) {
            if (milestone.isGenerallyAvailable() && milestone.isAfter(date)) {
                return true;
            }
        }
        return false;
    }

    public ReleaseDate releaseDateOrEstimate(final int averageReleaseDayOfMonth) {
        for (final Milestone milestone : milestones) {
            if (milestone.isGenerallyAvailable()) {
                return ReleaseDate.ofActual(milestone.milestoneDate());
            }
        }

        assert previousRelease != null; // Logic error

        final LocalDate previousDate = previousRelease.releaseDate();
        final LocalDate estimateDate = LocalDate.of(
            previousDate.getYear(),
            previousDate.getMonth() == Month.MARCH ? Month.SEPTEMBER : Month.MARCH,
            averageReleaseDayOfMonth
        );
        return ReleaseDate.ofEstimate(estimateDate);
    }

    public LocalDate releaseDate() {
        for (final Milestone milestone : milestones) {
            if (milestone.isGenerallyAvailable()) {
                return milestone.milestoneDate();
            }
        }
        return NO_RELEASE_DATE;
    }

    public int releaseDayOfMonth() {
        final var releaseDate = releaseDate();
        if (releaseDate != NO_RELEASE_DATE) {
            return releaseDate.getDayOfMonth();
        }
        return 0;
    }

    public void previousRelease(final JdkRelease previousRelease) {
        this.previousRelease = previousRelease;
    }

    public void nextRelease(final JdkRelease nextRelease) {
        this.nextRelease = nextRelease;
    }

    public LocalDate previousReleaseDate() {
        return previousRelease.releaseDate();
    }
}
