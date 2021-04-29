package net.jordanlabs.bot;

import com.github.redouane59.twitter.TwitterClient;
import net.jordanlabs.bot.domain.JdkProgress;
import net.jordanlabs.bot.domain.JdkRelease;
import net.jordanlabs.bot.domain.ReleaseDate;
import net.jordanlabs.bot.provider.PropertiesLoader;
import net.jordanlabs.bot.service.JdkProgressFetcher;
import net.jordanlabs.bot.service.ProgressAnnouncer;

import javax.inject.Inject;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.Properties;

public class NextJavaRelease {
    private final JdkProgressFetcher jdkProgressFetcher;
    private final ProgressAnnouncer progressAnnouncer;
    private final TwitterClient twitterClient;
    private final Properties properties;

    @Inject
    public NextJavaRelease(final JdkProgressFetcher jdkProgressFetcher,
                           final ProgressAnnouncer progressAnnouncer,
                           final TwitterClient twitterClient,
                           final PropertiesLoader loader) {
        this.jdkProgressFetcher = jdkProgressFetcher;
        this.progressAnnouncer = progressAnnouncer;
        this.twitterClient = twitterClient;
        this.properties = loader.loadProperties("config.properties");
    }

    public void runTwitterBot() throws IOException {
        final JdkProgress jdkProgress = jdkProgressFetcher.fetchProgress(properties.getProperty("openjdk.project.url"));

        final LocalDate todayDate = LocalDate.now(ZoneOffset.UTC);
        final Optional<JdkRelease> nextJavaRelease = jdkProgress.nextJavaRelease(todayDate);

        nextJavaRelease.ifPresent(jdkRelease -> {
            final LocalDate previousReleaseDate = jdkProgress.previousReleaseDateFor(jdkRelease);
            final ReleaseDate nextReleaseDate = jdkProgress.releaseDateOrEstimate(jdkRelease, previousReleaseDate);
            final String releaseTweet = progressAnnouncer.createTweet(
                jdkRelease,
                previousReleaseDate,
                todayDate,
                nextReleaseDate
            );

            System.out.println(releaseTweet);
            twitterClient.postTweet(releaseTweet);
        });
    }

}
