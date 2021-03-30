package net.jordanlabs.bot.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public boolean hasReleaseDate() {
        return releaseDate() != NO_RELEASE_DATE;
    }

    public LocalDate releaseDate() {
        for (final Milestone milestone : milestones) {
            if (milestone.isGenerallyAvailable()) {
                return milestone.milestoneDate();
            }
        }
        return NO_RELEASE_DATE;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", releaseNumber, latestMilestone);
    }
}
