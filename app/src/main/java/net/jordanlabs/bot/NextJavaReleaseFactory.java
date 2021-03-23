package net.jordanlabs.bot;

import dagger.Component;
import net.jordanlabs.bot.provider.UrlHtmlParserModule;
import net.jordanlabs.bot.service.ServiceModule;

import javax.inject.Singleton;

@Singleton
@Component(modules = { ServiceModule.class, UrlHtmlParserModule.class })
interface NextJavaReleaseFactory {
    NextJavaRelease nextJavaRelease();

    static NextJavaRelease createNextJavaRelease() {
        return DaggerNextJavaReleaseFactory.create().nextJavaRelease();
    }
}
