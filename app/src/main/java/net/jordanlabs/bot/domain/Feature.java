package net.jordanlabs.bot.domain;

public class Feature implements Comparable<Feature> {
    private final int jepNumber;
    private final String description;
    private final String jepUrl;
    private final Status status;

    public Feature(final int jepNumber, final String description, final String jepUrl, final Status status) {
        this.jepNumber = jepNumber;
        this.description = description;
        this.jepUrl = jepUrl;
        this.status = status;
    }

    public int jepNumber() {
        return jepNumber;
    }

    @Override
    public int compareTo(final Feature other) {
        return Integer.compare(jepNumber, other.jepNumber);
    }

    @Override
    public String toString() {
        return String.format("%d: %s [%s]", jepNumber, description, jepUrl);
    }

    public enum Status {
        INCLUDED_IN_JDK,
        TARGETED_FOR_JDK,
        PROPOSED_FOR_JDK
    }
}
