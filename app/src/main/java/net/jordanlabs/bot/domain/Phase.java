package net.jordanlabs.bot.domain;

public enum Phase {
    UNKNOWN("Unknown"),
    ALL_TESTS_RUN("All Tests Run"),
    RAMPDOWN_PHASE_ONE("Rampdown Phase One"),
    RAMPDOWN_PHASE_TWO("Rampdown Phase Two"),
    RELEASE_CANDIDATE_PHASE("Release-Candidate Phase"),
    INITIAL_RELEASE_CANDIDATE("Initial Release Candidate"),
    FINAL_RELEASE_CANDIDATE("Final Release Candidate"),
    GENERAL_AVAILABILITY("General Availability");

    private final String description;

    Phase(final String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }

    public static Phase fromDescription(final String description) {
        for (final Phase phase : values()) {
            if (description.equals(phase.description())) {
                return phase;
            }
        }
        return UNKNOWN;
    }
}
