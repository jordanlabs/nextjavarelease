package net.jordanlabs.bot;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {
    @ParameterizedTest
    @CsvSource({
        "NaN,false",
        "0.0,false",
        "-0.0,false",
        "-1.0,true",
        "1.0,true"
    })
    void fastIsNumberNotZero(final double input, final boolean expected) {
        assertThat(isNumberNotZero2(input)).isEqualTo(expected);
    }

    private boolean isNumberNotZero2(final double value) {
        return value != 0 && !Double.isNaN(value);
    }

    private boolean isNumberNotZero(final double value) {
        return value < 0 || value > 0;
    }
}
