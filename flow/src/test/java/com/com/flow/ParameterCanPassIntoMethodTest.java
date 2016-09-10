package com.com.flow;

import com.com.flow.annotations._0;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(AnnotationFlow.class)
class ParameterCanPassIntoMethodTest {

    private static final int EXPECTED_INT = 10;

    private int foundInt = Integer.MIN_VALUE;

    @BeforeAnnotated(_0.class)
    void beforeAnnotatedBy0(final _0 annotation) {
        assertNotNull(annotation);
        foundInt = annotation.value();
    }

    @_0
    @DisplayName("Passed annotation is not null")
    @Test
    void testPassedAnnotationIsNotNull() {
        assertEquals(1, 1);
    }

    @_0(EXPECTED_INT)
    @DisplayName("Passed annotation matches value from test method")
    @Test
    void testPassedAnnotation() {
        assertEquals(EXPECTED_INT, foundInt);
    }
}
