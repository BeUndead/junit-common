package com.com.flow;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * {@inheritDoc}
 * <p>
 * Implementation handles {@link BeforeAnnotated} and {@link AfterAnnotated}.
 */
final class PositiveFlowMethodRegistration<T extends Annotation> extends FlowMethodRegistration<T> {


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

    PositiveFlowMethodRegistration(final Method method, final Class<T> annotationClass) {
        super(method, annotationClass);
    }


    /**
     * {@inheritDoc}
     * <p>
     * Implementation verifies that all parameters are {@link Annotation Annotations}, and that they are a subset of
     * those which the given {@code Annotation} declares an interest in.
     */
    @Override
    final void validateParametersForAnnotation(final T annotation) {
        final Class<?>[] parameterClasses = getMethod().getParameterTypes();
        final Class<? extends Annotation>[] expectedAnnotationClasses = getRelevantAnnotationClasses(annotation);

        if (expectedAnnotationClasses.length == 0) {
            throw new IllegalArgumentException(String.format("Empty value in annotation %s on method %s",
                    annotation.getClass().getSimpleName(), getMethod().getName()));
        }

        parameterLoop:
        for (final Class<?> parameterClass : parameterClasses) {
            if (!Annotation.class.isAssignableFrom(parameterClass)) {
                throw new IllegalArgumentException(String.format("Parameter %s is not an Annotation",
                        parameterClass.getSimpleName()));
            }
            for (final Class<? extends Annotation> expectedAnnotationClass : expectedAnnotationClasses) {
                if (parameterClass.isAssignableFrom(expectedAnnotationClass)) {
                    continue parameterLoop;
                }
            }
            throw new IllegalArgumentException(String.format("No candidate for parameter %s on method %s",
                    parameterClass.getSimpleName(), getMethod().getName()));
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation verifies that, for <strong>some</strong> {@code annotation-flow} {@link Annotation} present on the
     * provided {@link Method}, the given {@code testMethod} contains <strong>all</strong> of the {@code Annotations}
     * which the {@code annotation-flow} {@code Annotation} declares an interest in.
     */
    @Override
    boolean shouldInvokeFor(final Method testMethod) {
        annotationLoop:
        for (final T annotation : getAnnotations()) {
            final Class<? extends Annotation>[] relevantAnnotationClasses = getRelevantAnnotationClasses(annotation);

            relevantAnnotationLoop:
            for (final Class<? extends Annotation> relevantAnnotationClass : relevantAnnotationClasses) {

                // We want to check all the present annotations for one of the annotation class of interest.
                for (final Annotation presentAnnotation : getPresentAnnotations()) {
                    if (relevantAnnotationClass.isAssignableFrom(presentAnnotation.getClass())) {
                        // We've found an appropriate annotation for this one.
                        // Continue to check the next annotation class of interest.
                        continue relevantAnnotationLoop;
                    }
                }
                // The current annotation class of interest had no present annotation.
                // Hence it should not be invoked for this annotation; check the next.
                continue annotationLoop;
            }

            // All annotation classes of interest had a present annotation for the current annotation.
            // Hence the method should be invoked.
            return true;
        }

        // All annotation-flow annotations were not satisfied, hence the method should not be invoked.
        return false;
    }
}
