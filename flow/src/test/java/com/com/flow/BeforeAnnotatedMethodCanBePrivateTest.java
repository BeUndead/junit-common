package com.com.flow;

import com.com.flow.annotations._0;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(AnnotationFlow.class)
class BeforeAnnotatedMethodCanBePrivateTest {

    private volatile boolean methodInvoked = false;

    @BeforeAnnotated(_0.class)
    private void invoke() {
        this.methodInvoked = true;
    }

    @_0
    @DisplayName("Test private method can be invoked")
    @Test
    void testPrivateMethodCanBeInvoked() {
        assertTrue(methodInvoked);
    }
}
