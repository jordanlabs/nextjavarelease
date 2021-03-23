package net.jordanlabs.bot.extractor.feature;

import net.jordanlabs.bot.domain.Feature;
import net.jordanlabs.bot.domain.FeatureStatus;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;

public abstract class FeatureExtractor {
    public static final String FEATURES_TABLE_QUERY = "h2#Features + blockquote > table.jeps";

    protected final Document releaseDoc;

    public FeatureExtractor(final Document releaseDoc) {
        this.releaseDoc = releaseDoc;
    }

    public abstract List<Feature> extractFeatures();

    protected FeatureStatus determineStatus(final Element featureTable) {
        final Element featureTitle = featureTable.selectFirst("th.title");
        if (featureTitle != null) {
            final String statusDescription = featureTitle.text();
            if (statusDescription.contains("proposed")) {
                return FeatureStatus.PROPOSED_FOR_JDK;
            } else if (statusDescription.contains("targeted")) {
                return FeatureStatus.TARGETED_FOR_JDK;
            }
        }
        return FeatureStatus.INCLUDED_IN_JDK;
    }

    protected Feature createFeature(final String jepNumber, final String description, final String jepUrl) {
        return createFeature(jepNumber, description, jepUrl, FeatureStatus.INCLUDED_IN_JDK);
    }

    protected Feature createFeature(final String jepNumber, final String description, final String jepUrl, final FeatureStatus featureStatus) {
        return new Feature(
            jepNumberToInt(jepNumber),
            description,
            jepUrl,
            featureStatus
        );
    }

    private int jepNumberToInt(final String jepNumber) {
        return Integer.parseInt(jepNumber.replaceAll(":", "").trim());
    }
}