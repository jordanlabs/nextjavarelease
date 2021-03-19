package net.jordanlabs.bot.domain;

public class Feature implements Comparable<Feature> {
    private final int jepNumber;
    private final String description;
    private final String jepUrl;

    private FeatureStatus featureStatus;

    public Feature(final int jepNumber, final String description, final String jepUrl, final FeatureStatus featureStatus) {
        this.jepNumber = jepNumber;
        this.description = description;
        this.jepUrl = jepUrl;
        this.featureStatus = featureStatus;
    }

    @Override
    public int compareTo(final Feature other) {
        return Integer.compare(jepNumber, other.jepNumber);
    }

    @Override
    public String toString() {
        return String.format("%d: %s [%s]", jepNumber, description, jepUrl);
    }
}
