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
class BeforeAnnotatedSingleAnnotationMultipleExpectedClassesTest {

    private volatile boolean beforeAnnotatedBy0And1Invoked = false;

    @BeforeAnnotated({_0.class, _1.class})
    void beforeAnnotatedBy0And1() {
        this.beforeAnnotatedBy0And1Invoked = true;
    }

    @DisplayName("Test method not annotated does not invoke BeforeAnnotated method")
    @Test
    void testMethodNotAnnotatedDoesNotInvokeMethod() {
        assertFalse(beforeAnnotatedBy0And1Invoked);
    }

    @_0
    @DisplayName("Test method annotated by only 1 expected class does not invoke BeforeAnnotated method")
    @Test
    void testMethodAnnotatedByOnlyOneExpectedClassDoesNotInvokeMethod() {
        assertFalse(beforeAnnotatedBy0And1Invoked);
    }

    @_0
    @_1
    @DisplayName("Test method annotated by both expected classes invokes BeforeAnnotated method")
    @Test
    void testMethodAnnotatedByBothExpectedClassesInvokesMethod() {
        assertTrue(beforeAnnotatedBy0And1Invoked);
    }

    @_0
    @_1
    @_2
    @DisplayName("Test method annotated by both expected classes and others invokes BeforeAnnotated method")
    @Test
    void testMethodAnnotatedByBothExpectedClassesAndMoreInvokesMethod() {
        assertTrue(beforeAnnotatedBy0And1Invoked);
    }

    @_0
    @_2
    @DisplayName("Test method annotated by multiple classes including only 1 expected class does not invoke BeforeAnnotated method")
    @Test
    void testMethodAnnotatedByMultipleClassesIncludingOnlyOneExpectedClassDoesNotInvokeMethod() {
        assertFalse(beforeAnnotatedBy0And1Invoked);
    }

    @_2
    @_3
    @DisplayName("Test method annotated by multiple classes not including expected classes does not invoke BeforeAnnotated method")
    @Test
    void testMethodAnnotatedByMultipleClassesNotIncludingAnyExpectedClassesDoesNotInvokeMethod() {
        assertFalse(beforeAnnotatedBy0And1Invoked);
    }

}
