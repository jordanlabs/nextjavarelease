package net.jordanlabs.bot;

import net.jordanlabs.bot.domain.JdkProgress;
import net.jordanlabs.bot.domain.JdkRelease;
import net.jordanlabs.bot.domain.ReleaseDate;
import net.jordanlabs.bot.service.JdkProgressFetcher;
import net.jordanlabs.bot.service.ProgressAnnouncer;

import javax.inject.Inject;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;

public class NextJavaRelease {
    private final JdkProgressFetcher jdkProgressFetcher;
    private final ProgressAnnouncer progressAnnouncer;

    @Inject
    public NextJavaRelease(final JdkProgressFetcher jdkProgressFetcher, final ProgressAnnouncer progressAnnouncer) {
        this.jdkProgressFetcher = jdkProgressFetcher;
        this.progressAnnouncer = progressAnnouncer;
    }

    public void runTwitterBot() throws IOException {
        final String jdkProjectUrl = "https://openjdk.java.net/projects/jdk/";
        final JdkProgress jdkProgress = jdkProgressFetcher.fetchProgress(jdkProjectUrl);

        final LocalDate todayDate = LocalDate.now(ZoneOffset.UTC);
        final Optional<JdkRelease> nextJavaRelease = jdkProgress.nextJavaRelease(todayDate);

        nextJavaRelease.ifPresent(jdkRelease -> {
            final LocalDate previousReleaseDate = jdkProgress.previousReleaseDateFor(jdkRelease);
            final ReleaseDate nextReleaseDate = jdkProgress.releaseDateOrEstimate(jdkRelease, previousReleaseDate);
            final String progressBar = progressAnnouncer.generateProgressBar(
                previousReleaseDate,
                todayDate,
                nextReleaseDate);
            System.out.println(progressBar);
        });
    }

}
