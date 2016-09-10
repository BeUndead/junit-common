package com.com.flow;

import com.com.flow.annotations._0;
import com.com.flow.annotations._1;
import com.com.flow.annotations._2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(AnnotationFlow.class)
class BeforeNotAnnotatedSingleAnnotationSingleExpectedClassTest {

    private volatile boolean beforeNotAnnotatedBy0Invoked = false;

    @BeforeNotAnnotated(_0.class)
    void beforeNotAnnotatedBy0() {
        this.beforeNotAnnotatedBy0Invoked = true;
    }

    @DisplayName("Test method with no annotations invokes BeforeNotAnnotated method")
    @Test
    void testMethodWithNoAnnotationsInvokesBeforeNotAnnotatedMethod() {
        assertTrue(beforeNotAnnotatedBy0Invoked);
    }

    @_0
    @DisplayName("Test method with expected annotation does not invoke BeforeNotAnnotated method")
    @Test
    void testMethodWithExpectedAnnotationDoesNotInvokeBeforeNotAnnotatedMethod() {
        assertFalse(beforeNotAnnotatedBy0Invoked);
    }

    @_1
    @DisplayName("Test method with different annotation invokes BeforeNotAnnotated method")
    @Test
    void testMethodWithDifferentAnnotationInvokesMethod() {
        assertTrue(beforeNotAnnotatedBy0Invoked);
    }

    @_0
    @_1
    @DisplayName("Test method with multiple annotations including expected does not invoke BeforeNotAnnotated method")
    @Test
    void testMethodWithMultipleAnnotationsIncludingExpectedDoesNotInvokeMethod() {
        assertFalse(beforeNotAnnotatedBy0Invoked);
    }

    @_1
    @_2
    @DisplayName("Test method with multiple annotations not including expected invokes BeforeNotAnnotated method")
    @Test
    void testMethodWithMultipleAnnotationsNotIncludingExpectedInvokesMethod() {
        assertTrue(beforeNotAnnotatedBy0Invoked);
    }

}
