package com.com.fail;

import java.lang.annotation.*;

/**
 * Marker {@link java.lang.annotation.Annotation} used to annotate {@link org.junit.jupiter.api.Test Tests} which
 * should fail.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface ShouldFail {
}
