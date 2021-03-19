package net.jordanlabs.bot.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class JdkRelease {
    private final String releaseUrl;
    private final String latestMilestone;
    private final String releaseNumber;
    private final Set<Milestone> milestones = new TreeSet<>();
    private final Set<Feature> features = new TreeSet<>();

    private JdkRelease previousRelease;
    private JdkRelease nextRelease;

    public JdkRelease(final String releaseUrl, final String releaseNumber, final String latestMilestone, final List<Milestone> milestones, final List<Feature> features, final LocalDateTime lastUpdated) {
        this.releaseUrl = releaseUrl;
        this.releaseNumber = releaseNumber;
        this.latestMilestone = latestMilestone;
        this.milestones.addAll(milestones);
        this.features.addAll(features);
    }

    public boolean isReleasedAfter(final LocalDate date) {
        if (milestones.isEmpty()) {
            return true;
        }
        for (final Milestone milestone : milestones) {
            if (milestone.isGenerallyAvailable() && milestone.milestoneDate().isAfter(date)) {
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
        return LocalDate.EPOCH;
    }

    public int releaseDayOfMonth() {
        for (final Milestone milestone : milestones) {
            if (milestone.isGenerallyAvailable()) {
                return milestone.milestoneDate().getDayOfMonth();
            }
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
