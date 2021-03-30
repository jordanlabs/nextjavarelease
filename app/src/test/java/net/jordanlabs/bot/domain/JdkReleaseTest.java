package net.jordanlabs.bot.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.jordanlabs.bot.domain.JdkRelease.NO_RELEASE_DATE;
import static org.assertj.core.api.Assertions.assertThat;

class JdkReleaseTest {
    private final static String RELEASE_URL = "https://next.jdk.net/releases/18";
    private final static String RELEASE_NUMBER = "18";
    private static final String LATEST_MILESTONE = "in development";
    private static final LocalDateTime PAGE_LAST_UPDATED = LocalDateTime.of(2021, 1, 2, 3, 4, 0);
    private static final List<Milestone> NO_MILESTONES = Collections.emptyList();
    private static final List<Feature> NO_FEATURES = Collections.emptyList();

    private JdkRelease jdkRelease;

    @Test
    void jdkWithNoMilestonesIsReleasedInTheFuture() {
        // Given
        jdkRelease = newJdkRelease(NO_MILESTONES);

        // When
        var isReleasedAfter = jdkRelease.isReleasedAfter(LocalDate.MAX);

        // Then
        assertThat(isReleasedAfter).isTrue();
        assertThat(jdkRelease.hasReleaseDate()).isFalse();
    }

    @ParameterizedTest(name = "jdk with ga milestone 2021-03-05 is released after {0}: {1}")
    @CsvSource({
        "2021-03-04,true",
        "2021-03-05,false",
        "2021-03-06,false"
    })
    void jdkIsReleasedAfterDateWhenMilestoneInTheFuture(LocalDate date, boolean expected) {
        // Given
        var milestones = Collections.singletonList(
            new Milestone(LocalDate.of(2021, 3, 5), Phase.GENERAL_AVAILABILITY, "")
        );
        jdkRelease = newJdkRelease(milestones);

        // When
        var isReleasedAfter = jdkRelease.isReleasedAfter(date);

        // Then
        assertThat(isReleasedAfter).isEqualTo(expected);
        assertThat(jdkRelease.hasReleaseDate()).isTrue();
    }

    @ParameterizedTest(name = "jdk with non-ga milestone 2021-03-05 is released after {0}: {1}")
    @CsvSource({
        "2021-03-04,false",
        "2021-03-05,false",
        "2021-03-06,false"
    })
    void jdkNotReleasedAfterWhenNoMilestoneIsGenerallyAvailable(LocalDate date, boolean expected) {
        // Given
        var milestones = Collections.singletonList(
            new Milestone(LocalDate.of(2021, 3, 5), Phase.ALL_TESTS_RUN, "")
        );
        jdkRelease = newJdkRelease(milestones);

        // When
        var isReleasedAfter = jdkRelease.isReleasedAfter(date);

        // Then
        assertThat(isReleasedAfter).isEqualTo(expected);
        assertThat(jdkRelease.hasReleaseDate()).isFalse();
    }

    @Test
    void releaseDateReturnsDateForGenerallyAvailableMilestone() {
        // Given
        var milestones = Arrays.asList(
            new Milestone(LocalDate.of(2021, 3, 1), Phase.GENERAL_AVAILABILITY, ""),
            new Milestone(LocalDate.of(2021, 2, 1), Phase.RAMPDOWN_PHASE_TWO, ""),
            new Milestone(LocalDate.of(2021, 1, 1), Phase.RAMPDOWN_PHASE_ONE, "")
        );
        jdkRelease = newJdkRelease(milestones);

        // When
        var releaseDate = jdkRelease.releaseDate();

        // Then
        assertThat(releaseDate).isEqualTo("2021-03-01");
        assertThat(jdkRelease.hasReleaseDate()).isTrue();
    }

    @Test
    void noReleaseDateReturnedWhenNoGenerallyAvailableMilestoneExists() {
        // Given
        var milestones = Arrays.asList(
            new Milestone(LocalDate.of(2021, 2, 1), Phase.RAMPDOWN_PHASE_TWO, ""),
            new Milestone(LocalDate.of(2021, 1, 1), Phase.RAMPDOWN_PHASE_ONE, "")
        );
        jdkRelease = newJdkRelease(milestones);

        // When
        var releaseDate = jdkRelease.releaseDate();

        // Then
        assertThat(releaseDate).isEqualTo(NO_RELEASE_DATE);
        assertThat(jdkRelease.hasReleaseDate()).isFalse();
    }

    @Test
    void jdkReleaseHasToString() {
        // Given
        jdkRelease = newJdkRelease(NO_MILESTONES);

        // When, Then
        assertThat(jdkRelease).hasToString(String.format("%s (%s)", RELEASE_NUMBER, LATEST_MILESTONE));
    }

    private JdkRelease newJdkRelease(final List<Milestone> milestones) {
        return new JdkRelease(
            RELEASE_URL,
            RELEASE_NUMBER,
            LATEST_MILESTONE,
            milestones,
            NO_FEATURES,
            PAGE_LAST_UPDATED
        );
    }
}
