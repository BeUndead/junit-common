package com.com.flow;

import java.lang.annotation.*;

/**
 * {@link java.lang.reflect.Method} marker {@link Annotation} to indicate that the marked {@code Method} should be run
 * <strong>only</strong> after those {@code Tests} which are <strong>not</strong> {@link
 * java.lang.reflect.AnnotatedElement#isAnnotationPresent(Class) annotated} by <strong>any</strong> of the {@code
 * Annotations} specified in the {@link #value()}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(AfterNotAnnotateds.class)
@Inherited
@Documented
public @interface AfterNotAnnotated {

    /**
     * @return An {@link java.lang.reflect.Array} of {@link Annotation} {@link Class Classes} which should
     * <strong>all</strong> not be present on the executing {@code Test} for the {@link
     * java.lang.reflect.AnnotatedElement#isAnnotationPresent(Class) annotated} {@link java.lang.reflect.Method} to be
     * {@link java.lang.reflect.Method#invoke(Object, Object...) invoked} after.
     */
    Class<? extends Annotation>[] value();
}
