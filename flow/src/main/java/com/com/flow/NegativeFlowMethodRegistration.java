package com.com.flow;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * {@inheritDoc}
 * <p>
 * Implementation handles {@link BeforeNotAnnotated} and {@link AfterNotAnnotated}.
 */
final class NegativeFlowMethodRegistration<T extends Annotation> extends FlowMethodRegistration<T> {


    /**
     * Constructor; generates a {@link FlowMethodRegistration} for the provided {@code Method}, searching for the {@code
     * annotations} of the provided {@code annotationClass}.
     *
     * @param method          The {@link Method} which is {@code annotated} by an {@code annotation-flow} {@link
     *                        Annotation}.
     * @param annotationClass The {@link Class} of the {@code annotation-flow} {@code Annotation} which this
     *                        registration is for.
     * @throws IllegalArgumentException If the {@code Method's} declaration does not match the expectations for the
     *                                  considered {@code annotation-flow} {@code Annotations}.
     */
    NegativeFlowMethodRegistration(final Method method, final Class<T> annotationClass) {
        super(method, annotationClass);
    }


    /**
     * {@inheritDoc}
     * <p>
     * Implementation verifies that there are <i>no</i> parameters on the provided {@link Method}.
     */
    @Override
    final void validateParametersForAnnotation(final T annotation) {
        if (getMethod().getParameterTypes().length != 0) {
            throw new IllegalArgumentException("Illegal parameter list for method " + getMethod().getName());
        }

        if (getRelevantAnnotationClasses(annotation).length == 0) {
            throw new IllegalArgumentException(String.format("Empty value in annotation %s on method %s",
                    annotation.getClass().getSimpleName(), getMethod().getName()));
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation verifies that, for <strong>some</strong> {@code annotation-flow} {@link Annotation} present on the
     * provided {@link Method}, the given {@code testMethod} meets the requirements; i.e. has <strong>none</strong> of
     * the {@code Annotations} present which the {@code annotation-flow} {@code Annotation} declares an interest in.
     */
    @Override
    boolean shouldInvokeFor(final Method testMethod) {
        final Annotation[] presentAnnotations = testMethod.getAnnotations();

        for (final T annotation : getAnnotations()) {
            final Class<? extends Annotation>[] relevantAnnotationClasses = getRelevantAnnotationClasses(annotation);

            relevantAnnotationLoop:
            for (final Class<? extends Annotation> relevantAnnotationClass : relevantAnnotationClasses) {

                // For each annotation class of interest, we want to check if there's no present annotation of that
                // class.
                for (final Annotation presentAnnotation : presentAnnotations) {
                    if (relevantAnnotationClass.isAssignableFrom(presentAnnotation.getClass())) {
                        // The expected annotation was present; continue to see if all others are present.
                        continue relevantAnnotationLoop;
                    }
                }
                // There was some expected annotation which wasn't present; hence we should invoke the method.
                return true;
            }

            // All of the expected annotations were present; hence we shouldn't invoke the method.
            // Check the next annotation.
        }

        return false;
    }
}
