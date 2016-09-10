package com.com.flow;

import java.lang.annotation.*;

/**
 * Grouping {@link Annotation} for {@link AfterNotAnnotated}.  Allows multiple {@code AfterNotAnnotated} {@code
 * Annotations} to be given on a single {@link java.lang.reflect.Method}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface AfterNotAnnotateds {

    /**
     * @return An {@link java.lang.reflect.Array} of the {@link AfterNotAnnotated} {@link Annotation Annotations} which should
     * be honoured on the {@link java.lang.reflect.AnnotatedElement#isAnnotationPresent(Class) annotated} {@code
     * Method}.
     */
    AfterNotAnnotated[] value();
}
