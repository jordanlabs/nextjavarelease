package net.jordanlabs.bot.service;

import dagger.Module;
import dagger.Provides;
import net.jordanlabs.bot.provider.HtmlParser;

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
}
