package net.jordanlabs.bot.extractor.feature;

import net.jordanlabs.bot.domain.Feature;
import org.jsoup.nodes.Document;

import java.util.List;

class MultipleTableFeatures extends FeatureExtractor {

    public MultipleTableFeatures(final Document releaseDoc) {
        super(releaseDoc);
    }

    @Override
    public List<Feature> extractFeatures() {
        throw new UnsupportedOperationException("Not implemented");
    }
}
