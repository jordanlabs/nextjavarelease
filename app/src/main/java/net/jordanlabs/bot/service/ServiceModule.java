package net.jordanlabs.bot.service;

import io.github.redouane59.twitter.TwitterClient;
import dagger.Module;
import dagger.Provides;
import net.jordanlabs.bot.provider.HtmlParser;

import javax.inject.Singleton;

@Module
public abstract class ServiceModule {
    @Provides
    static JdkProgressFetcher jdkProgressFetcher(HtmlParser htmlParser) {
        return new JdkProgressFetcher(htmlParser);
    }

    @Provides
    static ProgressAnnouncer progressAnnouncer() {
        return new ProgressAnnouncer();
    }

    @Provides
    @Singleton
    static TwitterClient twitterClient() {
        return new TwitterClient();
    }
}
