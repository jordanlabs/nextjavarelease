package net.jordanlabs.bot.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FeatureTest {
    @Test
    void featuresSortByJepNumber() {
        // Given
        var features = new ArrayList<>(List.of(
            new Feature(3, "", "", Feature.Status.INCLUDED_IN_JDK),
            new Feature(1, "", "", Feature.Status.INCLUDED_IN_JDK),
            new Feature(2, "", "", Feature.Status.INCLUDED_IN_JDK)
        ));

        // When
        Collections.sort(features);

        // Then
        assertThat(features)
            .isSorted()
            .extracting(Feature::jepNumber)
            .containsExactly(1, 2, 3);
    }
}
