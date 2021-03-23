package net.jordanlabs.bot.provider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

@Singleton
public class UrlHtmlParser implements HtmlParser {
    @Inject
    UrlHtmlParser() {
    }

    @Override
    public Document loadDocumentFrom(final String location) throws IOException {
        return Jsoup.connect(location).get();
    }
}
