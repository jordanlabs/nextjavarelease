package net.jordanlabs.bot.provider;

import org.jsoup.nodes.Document;

import java.io.IOException;

public interface HtmlParser {
    Document loadDocumentFrom(final String location) throws IOException;
}
