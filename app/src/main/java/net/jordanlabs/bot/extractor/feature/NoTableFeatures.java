package net.jordanlabs.bot.extractor.feature;

import net.jordanlabs.bot.domain.Feature;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

class NoTableFeatures extends FeatureExtractor {

    public NoTableFeatures(final Document releaseDoc) {
        super(releaseDoc);
    }

    @Override
    public List<Feature> extractFeatures() {
        final List<Feature> features = new ArrayList<>();
        final Element allFeatures = releaseDoc.selectFirst("h2#Features + blockquote");
        if (allFeatures != null) { // new jdk releases might not even have features in a blockquote
            final List<TextNode> featureJeps = allFeatures.textNodes().stream().filter(not(TextNode::isBlank)).collect(Collectors.toUnmodifiableList());
            final Elements featureLinks = allFeatures.select("a");
            for (int i = 0; i < featureJeps.size(); i++) {
                final Element featureLink = featureLinks.get(i);
                features.add(createFeature(
                    featureJeps.get(i).text(),
                    featureLink.text(),
                    featureLink.attr("abs:href")
                ));
            }
        }
        return features;
    }
}
