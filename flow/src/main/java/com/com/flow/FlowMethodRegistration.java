package com.com.flow;

import org.junit.platform.commons.util.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 * Represents the registration for an {@code annotation-flow} {@code annotated} {@link Method}.
 * <p>
 * Subclasses handle the implementations of the {@code shouldInvokeFor} method (which should determine if the currently
 * executing {@code Test} has the annotations present to be invoked as specified by the present {@code annotation-flow}
 * {@link Annotation Annotations}); and the {@code validateParametersForAnnotation(T)} method (which should determine if
 * the arguments of the provided {@code method} match the expectations of the given {@code annotation-flow} {@code
 * Annotation}).
 *
 * @param <T> The {@code Type} of {@code Annotation} under consideration; should be one of:
 *            <ul>
 *            <li>{@link BeforeAnnotated};</li>
 *            <li>{@link BeforeNotAnnotated};</li>
 *            <li>{@link AfterAnnotated}; or</li>
 *            <li>{@link AfterNotAnnotated}.</li>
 *            </ul>
 * @see PositiveFlowMethodRegistration
 * @see NegativeFlowMethodRegistration
 */
abstract class FlowMethodRegistration<T extends Annotation> {

    /**
     * The {@link Class} of the {@code annotation-flow} {@link Annotation} which is referenced by this registration.
     */
    private final Class<T> annotationClass;

    /**
     * The {@link Method} which is {@code annotated} with an {@code annotation-flow} {@link Annotation}, referenced by
     * this registration.
     */
    private final Method method;

    /**
     * The {@code annotations} (of {@code Type} {@code T}) on the provided {@code Method}.  Each of the {@code
     * annotation-flow} {@link Annotation Annotations} are {@link java.lang.annotation.Repeatable}, so multiple can be
     * present.
     */
    private final List<T> annotations;


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
    FlowMethodRegistration(final Method method, final Class<T> annotationClass) {
        this.method = method;
        this.annotationClass = annotationClass;
        this.annotations = AnnotationUtils.findRepeatableAnnotations(method, annotationClass);
        validate();
    }

    /**
     * @return The {@code annotation-flow} {@code annotated} {@link Method} which this registration is for.
     */
    final Method getMethod() {
        return this.method;
    }

    /**
     * @return The {@link Annotation Annotations} of {@code Type} {@code T} which are present on the {@code Method}.
     */
    final Collection<T> getAnnotations() {
        return this.annotations;
    }

    /**
     * Validates that the {@link Method Method's} declaration matches expectations to be used for {@code
     * annotation-flow}.
     */
    private void validate() {
        validateParameters();
    }

    /**
     * Validates that the parameters for the {@link Method} matches expectations to be used for {@code annotation-flow}.
     */
    private void validateParameters() {
        annotations.forEach(this::validateParametersForAnnotation);
    }

    /**
     * Retrieves an {@link java.lang.reflect.Array} of the {@link Class Classes} of the {@link Annotation Annotations}
     * which the given {@code annotation-flow} {@link Annotation} declares an interest in.
     *
     * @param annotation The {@code annotation-flow} {@code Annotation} to retrieve the {@code Annotation} {@code
     *                   Classes} of interest.  Must be one of:
     *                   <ul>
     *                   <li>{@link BeforeAnnotated};</li>
     *                   <li>{@link BeforeNotAnnotated};</li>
     *                   <li>{@link AfterAnnotated}; or</li>
     *                   <li>{@link AfterNotAnnotated}.</li>
     *                   </ul>
     * @return An {@code Array} of the {@code Classes} of {@code Annotations} which the provided {@code Annotation}
     * declares an interest in.
     */
    @SuppressWarnings("unchecked") // ClassCastException is explicitly caught.  Package local only, so shouldn't occur.
    final Class<? extends Annotation>[] getRelevantAnnotationClasses(final Annotation annotation) {
        try {
            return (Class<? extends Annotation>[]) annotation.getClass().getMethod("value").invoke(annotation);
        } catch (final NoSuchMethodException nsmEx) {
            throw new IllegalStateException("Incorrect annotation type given " + annotation.getClass().getSimpleName());
        } catch (final IllegalAccessException iaEx) {
            throw new IllegalStateException("SecurityManager refuses required access", iaEx);
        } catch (final InvocationTargetException itEx) {
            throw new IllegalStateException("Exception when invoking required method", itEx);
        } catch (final ClassCastException ccEx) {
            throw new IllegalStateException(String.format("Incorrect return type for method %s on Annotation %s",
                    ".value()", annotation.getClass().getSimpleName()));
        }
    }

    /**
     * Invokes the provided {@code annotation-flow} {@link Method}, using the {@link Annotation Annotations} from the
     * given {@code testMethod} as parameters (where required).
     *
     * @param testMethod   The currently executing {@code Test's} {@code Method}.
     * @param testInstance The {@link Object} representing the current {@code Test} {@link Class Class'} instance.
     */
    final void invokeFor(final Method testMethod, final Object testInstance) {
        final FlowMethodParameterBuilder parameterBuilder = new FlowMethodParameterBuilder(method, testMethod);
        try {
            method.setAccessible(true);
            method.invoke(testInstance, parameterBuilder.getParameters());
        } catch (final IllegalAccessException iaEx) {
            throw new IllegalStateException("SecurityManager stopped required invocation", iaEx);
        } catch (final InvocationTargetException itEx) {
            throw new RuntimeException("Exception while invoking AnnotationFlow Method", itEx);
        }
    }

    /**
     * Validates the parameters meet expectations to be used for {@code annotation-flow} for the single {@link
     * Annotation} given.
     *
     * @param annotation The {@code annotation-flow} {@code Annotation} to validate the parameters for.
     */
    abstract void validateParametersForAnnotation(final T annotation);

    /**
     * Whether or not the provided {@link Method} should be invoked for the given {@code Test} {@code Method}.  I.e. The
     * {@link Annotation Annotations} present on the {@code testMethod} match those of interest for the {@code
     * annotation-flow} {@code Annotations} registered.
     *
     * @param testMethod The currently executing {@code testMethod}.
     * @return {@code true} if the given {@code testMethod} means that the provided {@code Method}
     * <strong>should</strong> be invoked for the currently executing {@code testMethod}.
     */
    abstract boolean shouldInvokeFor(final Method testMethod);
}
