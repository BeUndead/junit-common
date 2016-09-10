package com.com.flow;

import java.lang.annotation.*;

/**
 * Grouping {@link Annotation} for {@link AfterAnnotated}.  Allows multiple {@code AfterAnnotated} {@code Annotations}
 * to be given on a single {@link java.lang.reflect.Method}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface AfterAnnotateds {

    /**
     * @return An {@link java.lang.reflect.Array} of the {@link AfterAnnotated} {@link Annotation Annotations} which should be
     * honoured on the {@link java.lang.reflect.AnnotatedElement#isAnnotationPresent(Class) annotated} {@code Method}.
     */
    AfterAnnotated[] value();
}
