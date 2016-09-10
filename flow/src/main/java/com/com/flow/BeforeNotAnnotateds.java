package com.com.flow;

import java.lang.annotation.*;

/**
 * Grouping {@link Annotation} for {@link BeforeNotAnnotated}.  Allows multiple {@code BeforeNotAnnotated} {@code
 * Annotations} to be given on a single {@link java.lang.reflect.Method}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface BeforeNotAnnotateds {

    /**
     * @return An {@link java.lang.reflect.Array} of the {@link BeforeNotAnnotated} {@link Annotation Annotations} which should
     * be honoured on the {@link java.lang.reflect.AnnotatedElement#isAnnotationPresent(Class) annotated} {@code
     * Method}.
     */
    BeforeNotAnnotated[] value();
}
