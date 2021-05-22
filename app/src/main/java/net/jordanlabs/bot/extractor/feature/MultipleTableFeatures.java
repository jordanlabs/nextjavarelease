package net.jordanlabs.bot.extractor.feature;

import net.jordanlabs.bot.domain.Feature;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

class MultipleTableFeatures extends FeatureExtractor {

    public MultipleTableFeatures(final Document releaseDoc) {
        super(releaseDoc);
    }

    @Override
    public List<Feature> extractFeatures() {
        final List<Feature> features = new ArrayList<>();
        final Elements featureTables = releaseDoc.select(ALL_FEATURE_TABLES_QUERY);
        for (final Element table : featureTables) {
            final Feature.Status status = determineStatus(table);
            for (final Element feature : table.select("tr:has(td)")) {
                final Element featureLink = feature.selectFirst("a");
                features.add(createFeature(
                    feature.child(0).text(),
                    featureLink.text(),
                    featureLink.attr("abs:href"),
                    status
                ));
            }
        }
        return features;
    }
}
