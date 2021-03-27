package net.jordanlabs.bot.extractor.feature;

import net.jordanlabs.bot.domain.Feature;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

class SingleTableFeatures extends FeatureExtractor {

    public SingleTableFeatures(final Document releaseDoc) {
        super(releaseDoc);
    }

    @Override
    public List<Feature> extractFeatures() {
        final List<Feature> features = new ArrayList<>();
        final Element featuresTable = releaseDoc.selectFirst(FEATURES_TABLE_QUERY);
        final Feature.Status status = determineStatus(featuresTable);
        for (final Element feature : featuresTable.select("tr:has(td)")) {
            final Element featureLink = feature.selectFirst("a");
            features.add(createFeature(
                feature.child(0).text(),
                featureLink.text(),
                featureLink.attr("abs:href"),
                status
            ));
        }
        return features;
    }
}
