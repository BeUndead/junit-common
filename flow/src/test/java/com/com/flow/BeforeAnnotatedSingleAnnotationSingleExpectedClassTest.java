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
class BeforeAnnotatedSingleAnnotationSingleExpectedClassTest {

    private volatile boolean beforeAnnotatedBy0Invoked = false;

    @BeforeAnnotated(_0.class)
    void beforeAnnotatedBy0() {
        this.beforeAnnotatedBy0Invoked = true;
    }

    @DisplayName("Test method not annotated by expected class does not invoke BeforeAnnotated method")
    @Test
    void testNotAnnotatedByExpectedClassDoesNotInvokeMethod() {
        assertFalse(beforeAnnotatedBy0Invoked);
    }

    @_0
    @DisplayName("Test method annotated by expected class does invoke BeforeAnnotated method")
    @Test
    void testAnnotatedByExpectedClassDoesInvokeMethod() {
        assertTrue(beforeAnnotatedBy0Invoked);
    }

    @_1
    @DisplayName("Test method annotated by different class does not invoke BeforeAnnotated method")
    @Test
    void testAnnotatedByDifferentClassDoesNotInvokeMethod() {
        assertFalse(beforeAnnotatedBy0Invoked);
    }

    @_0
    @_1
    @DisplayName("Test method annotated by multiple classes including expected one invokes BeforeAnnotated method")
    @Test
    void testAnnotatedByMultipleIncludingExpectedInvokesMethod() {
        assertTrue(beforeAnnotatedBy0Invoked);
    }

    @_1
    @_2
    @DisplayName("Test method annotated by multiple classes not including expected one does not invoke BeforeAnnotated method")
    @Test
    void testAnnotatedByMultipleNotIncludingExpectedDoesNotInvokeMethod() {
        assertFalse(beforeAnnotatedBy0Invoked);
    }
}
