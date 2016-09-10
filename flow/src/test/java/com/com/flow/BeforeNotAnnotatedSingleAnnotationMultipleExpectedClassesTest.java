package com.com.flow;

import com.com.flow.annotations._0;
import com.com.flow.annotations._1;
import com.com.flow.annotations._2;
import com.com.flow.annotations._3;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(AnnotationFlow.class)
class BeforeNotAnnotatedSingleAnnotationMultipleExpectedClassesTest {

    private volatile boolean beforeNotAnnotatedBy0And1Invoked = false;

    @BeforeNotAnnotated({_0.class, _1.class})
    void beforeNotAnnotatedBy0And1() {
        this.beforeNotAnnotatedBy0And1Invoked = true;
    }

    @DisplayName("Test method not annotated invokes BeforeNotAnnotated method")
    @Test
    void testNotAnnotatedTestInvokesMethod() {
        assertTrue(beforeNotAnnotatedBy0And1Invoked);
    }

    @_0
    @DisplayName("Test method annotated by only 1 expected class invokes BeforeNotAnnotated method")
    @Test
    void testAnnotatedByOnlyOneExpectedClassInvokesMethod() {
        assertTrue(beforeNotAnnotatedBy0And1Invoked);
    }

    @_0
    @_1
    @DisplayName("Test method annotated by all expected classes does not invoke BeforeNotAnnotated method")
    @Test
    void testAnnotatedByBothExpectedClassesDoesNotInvokeMethod() {
        assertFalse(beforeNotAnnotatedBy0And1Invoked);
    }

    @_0
    @_2
    @DisplayName("Test method annotated by multiple including one expected class but not other invokes BeforeNotAnnotated method")
    @Test
    void testAnnotatedByMultipleIncludingOnlyOneExpectedClassInvokesMethod() {
        assertTrue(beforeNotAnnotatedBy0And1Invoked);
    }

    @_2
    @_3
    @DisplayName("Test method annotated by multiple classes not including any expected classes invokes BeforeNotAnnotated method")
    @Test
    void testAnnotatedByMultipleNotIncludingAnyExpectedClassesInvokesMethod() {
        assertTrue(beforeNotAnnotatedBy0And1Invoked);
    }

    @_0
    @_1
    @_2
    @DisplayName("Test method annotated by all expected classes and others does not invoke BeforeNotAnnotated method")
    @Test
    void testAnnotatedByAllExpectedClassesAndOthersDoesNotInvokeMethod() {
        assertFalse(beforeNotAnnotatedBy0And1Invoked);
    }
}
