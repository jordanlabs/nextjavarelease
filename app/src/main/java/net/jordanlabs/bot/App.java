package net.jordanlabs.bot;

import net.jordanlabs.bot.domain.JdkProgress;
import net.jordanlabs.bot.domain.JdkRelease;
import net.jordanlabs.bot.service.JdkProgressFetcher;
import net.jordanlabs.bot.service.ProgressAnnouncer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;

public class App {
    public static void main(String[] args) throws IOException {
        final String jdkProjectUrl = "https://openjdk.java.net/projects/jdk/";
        final JdkProgressFetcher jdkProgressFetcher = new JdkProgressFetcher(jdkProjectUrl);
        final JdkProgress jdkProgress = jdkProgressFetcher.fetchProgress();

        final LocalDate todayDate = LocalDate.now(ZoneOffset.UTC);
        final Optional<JdkRelease> nextJavaRelease = jdkProgress.nextJavaRelease(todayDate);

        final ProgressAnnouncer progressAnnouncer = new ProgressAnnouncer();
        nextJavaRelease.ifPresent(jdkRelease -> {
            final String progressBar = progressAnnouncer.generateProgressBar(
                jdkRelease.previousReleaseDate(),
                todayDate,
                jdkRelease.releaseDateOrEstimate(jdkProgress.averageReleaseDayOfMonth()));
            System.out.println(progressBar);
        });
    }
}
