package com.com.flow;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to generate the {@code parameters} for the provided {@link #toInvoke}, from the {@code Annotations} specified on
 * the {@link #testMethod}.
 */
final class FlowMethodParameterBuilder {

    /**
     * The {@link Method} to be {@code invoked}.
     */
    private final Method toInvoke;

    /**
     * The currently executing {@code Test} {@link Method}.
     */
    private final Method testMethod;


    /**
     * Generates a {@link FlowMethodParameterBuilder} for the given {@link Method} {@link #toInvoke}, from the {@code
     * Annotations} present on the {@link #testMethod currently executing Test Method}.
     *
     * @param toInvoke   The {@code Method} which is to be invoked.  Expects that all arguments have been validated for
     *                   the {@code AnnotationFlow} to be as expected.
     * @param testMethod The {@code testMethod} currently executing.
     */
    FlowMethodParameterBuilder(final Method toInvoke, final Method testMethod) {
        this.toInvoke = toInvoke;
        this.testMethod = testMethod;
    }

    /**
     * Generates the {@link java.lang.reflect.Array} of {@code parameters} from the provided {@link #toInvoke Method to
     * invoke} from the provided {@link #testMethod}.
     *
     * @return An {@code Array} of {@code Objects} which can be used directly in the call to {@link
     * Method#invoke(Object, Object...) invoke} the provided {@link #toInvoke} {@link Method}.
     * @throws IllegalStateException If there is an {@code parameter} on the {@code toInvoke} {@code Method} which does
     *                               not have a candidate {@code Annotation} from the provided {@code testMethod}.  This
     *                               should <i>not</i> happen, as the {@code Method} should have been validated to be
     *                               as expected by the time this is constructed.
     */
    Object[] getParameters() {

        // Get all the parameter types.
        // These should be verified to be as expected.
        final Class<?>[] parameterTypes = toInvoke.getParameterTypes();

        // Find all of the annotations present on the testMethod.
        final Annotation[] presentAnnotations = testMethod.getAnnotations();

        // Used to collect the parameters (List to preserve order on Array conversion).
        final List<Object> parameters = new ArrayList<>();

        parameterLoop:
        for (final Class<?> parameterType : parameterTypes) { // Every parameter should have a candidate
            for (final Annotation annotation : presentAnnotations) { // from the present annotations.
                if (parameterType.isAssignableFrom(annotation.getClass())) {
                    // We have found a candidate, so add it to the parameters.
                    parameters.add(annotation);
                    continue parameterLoop;
                }
            }

            // Current parameter had no candidate; throw RuntimeException as this should not occur.
            throw new IllegalStateException(String.format("No candidate for parameter %s on method %s",
                    parameterType.getSimpleName(), toInvoke.getName()));
        }

        // Method.invoke expects an array, make sure we used a List so we preserve order.
        return parameters.toArray();
    }
}
