package net.jordanlabs.bot.provider;

import dagger.Binds;
import dagger.Module;

@Module
public interface UrlHtmlParserModule {
    @Binds
    HtmlParser urlHtmlParser(UrlHtmlParser urlHtmlParser);
}
