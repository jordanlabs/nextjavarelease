package net.jordanlabs.bot.extractor.feature;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import static net.jordanlabs.bot.extractor.feature.FeatureExtractor.ALL_FEATURE_TABLES_QUERY;

public class Features {
    private Features() {
    }

    public static FeatureExtractor createExtractor(final Document releaseDoc) {
        final Elements jepTables = releaseDoc.select(ALL_FEATURE_TABLES_QUERY);
        if (jepTables.isEmpty()) {
            return new NoTableFeatures(releaseDoc);
        } else if (jepTables.size() == 1) {
            return new SingleTableFeatures(releaseDoc);
        }
        return new MultipleTableFeatures(releaseDoc);
    }
}
