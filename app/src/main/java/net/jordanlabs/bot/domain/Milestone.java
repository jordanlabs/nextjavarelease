package net.jordanlabs.bot.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Milestone implements Comparable<Milestone>{
    public static final DateTimeFormatter MILESTONE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private final LocalDate milestoneDate;
    private final Phase phase;
    private final String phaseUrl;

    public Milestone(final LocalDate milestoneDate, final Phase phase, final String phaseUrl) {
        this.milestoneDate = milestoneDate;
        this.phase = phase;
        this.phaseUrl = phaseUrl;
    }

    public LocalDate milestoneDate() {
        return milestoneDate;
    }

    public Phase phase() {
        return phase;
    }

    public String phaseUrl() {
        return phaseUrl;
    }

    public boolean isGenerallyAvailable() {
        return phase == Phase.GENERAL_AVAILABILITY;
    }

    public boolean isAfter(final LocalDate date) {
        return date.isAfter(date);
    }

    @Override
    public int compareTo(final Milestone other) {
        return milestoneDate.compareTo(other.milestoneDate);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Milestone)) {
           return false;
        }

        final Milestone milestone = (Milestone) other;
        return milestoneDate.equals(milestone.milestoneDate) &&
            phase.equals(milestone.phase) &&
            phaseUrl.equals(milestone.phaseUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(milestoneDate, phase, phaseUrl);
    }

    @Override
    public String toString() {
        return String.format("%s %s [%s]", milestoneDate.format(MILESTONE_DATE_FORMAT), phase, phaseUrl);
    }
}
