package net.jordanlabs.bot.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MilestoneTest {
    @Test
    void milestonesSortByDate() {
        // Given
        var milestones = new ArrayList<>(List.of(
            new Milestone(LocalDate.of(2020, 6, 20), Phase.UNKNOWN, ""),
            new Milestone(LocalDate.of(2021, 1, 10), Phase.UNKNOWN, ""),
            new Milestone(LocalDate.of(2020, 3, 30), Phase.UNKNOWN, "")
        ));

        // When
        Collections.sort(milestones);

        // Then
        assertThat(milestones)
            .isSorted()
            .extracting(Milestone::milestoneDate)
            .containsExactly(
                LocalDate.of(2020, 3, 30),
                LocalDate.of(2020, 6, 20),
                LocalDate.of(2021, 1, 10)
            );
    }
}
