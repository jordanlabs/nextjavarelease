package net.jordanlabs.bot.domain;

import java.time.LocalDate;

public class ReleaseDate {
    private final LocalDate releaseDate;
    private final boolean isEstimate;

    private ReleaseDate(final LocalDate releaseDate, final boolean isEstimate) {
        this.releaseDate = releaseDate;
        this.isEstimate = isEstimate;
    }

    public static ReleaseDate ofActual(final LocalDate releaseDate) {
        return new ReleaseDate(releaseDate, false);
    }

    public static ReleaseDate ofEstimate(final LocalDate releaseDate) {
        return new ReleaseDate(releaseDate, true);
    }

    public LocalDate releaseDate() {
        return releaseDate;
    }

    public boolean isEstimate() {
        return isEstimate;
    }
}
