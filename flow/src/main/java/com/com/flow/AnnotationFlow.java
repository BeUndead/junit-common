package com.com.flow;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.TestExtensionContext;
import org.junit.platform.commons.util.AnnotationUtils;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link org.junit.jupiter.api.extension.Extension} which handles the {@link Method#invoke(Object, Object...)
 * invocation} of methods annotated by annotation-flow {@link Annotation Annotations}.
 *
 * @see BeforeAnnotated
 * @see BeforeNotAnnotated
 * @see AfterAnnotated
 * @see AfterNotAnnotated
 */
public final class AnnotationFlow implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    /**
     * Generates a {@link List} of the {@link FlowMethodRegistration FlowMethodRegistrations} for the specified {@link
     * Class}.
     *
     * @param testClass       The {@code Class} of the currently executing {@code Test}.  Methods for this {@code Class}
     *                        and all {@code super} {@code classes} will be scanned for relevant methods.
     * @param annotationClass The {@code Class} of the {@code Annotation} to scan for {@link Method Methods} annotated
     *                        by.
     * @param expectsPresence {@code true} if the given {@code Annotation} expects the presence of the {@code
     *                        Annotations} specified in its {@code value} {@code method} (i.e. {@link BeforeAnnotated}
     *                        or {@link AfterAnnotated}); otherwise {@code false}.
     * @param <T>             The {@code Type} of the {@code annotationClass}.
     * @return A {@code List} of the {@code FlowMethodRegistrations} generated from the provided {@code testClass}.
     * Upon return, each method will have had its declaration validated.
     */
    private static <T extends Annotation> List<FlowMethodRegistration<T>> buildRegistrations(
            final Class<?> testClass, final Class<T> annotationClass, final boolean expectsPresence) {

        final List<Method> annotatedMethods = ReflectionUtils.findMethods(testClass,
                method -> !AnnotationUtils.findRepeatableAnnotations(method, annotationClass).isEmpty());

        return annotatedMethods.stream()
                .map(method -> expectsPresence ?
                        new PositiveFlowMethodRegistration<>(method, annotationClass) :
                        new NegativeFlowMethodRegistration<>(method, annotationClass))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Invokes the {@link BeforeAnnotated} and {@link BeforeNotAnnotated} {@link
     * java.lang.reflect.AnnotatedElement#isAnnotationPresent(Class) annotated} {@link Method Methods} of the currently
     * executing {@link org.junit.jupiter.api.Test}, for which the {@code Annotations} of the current {@code Test}
     * {@code Method} honour.
     */
    @Override
    public void beforeTestExecution(final TestExtensionContext context) throws Exception {
        invokeHonouredAnnotatedMethods(context, BeforeAnnotated.class, BeforeNotAnnotated.class);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Invokes the {@link AfterAnnotated} and {@link AfterNotAnnotated} {@link
     * java.lang.reflect.AnnotatedElement#isAnnotationPresent(Class) annotated} {@link Method Methods} of the currently
     * executing {@link org.junit.jupiter.api.Test}, for which the {@code Annotations} of the current {@code Test}
     * {@code Method} honour.
     */
    @Override
    public void afterTestExecution(final TestExtensionContext context) throws Exception {
        invokeHonouredAnnotatedMethods(context, AfterAnnotated.class, AfterNotAnnotated.class);
    }

    /**
     * Invokes the {@link Method Methods} {@code annotated} by either of the given {@link Class classes} which are
     * honoured by the currently executing {@code Test} {@code Method}.
     *
     * @param context                         The {@link TestExtensionContext} which is currently underway.
     * @param expectantFlowAnnotationClass    The {@code Class} of the {@link Annotation} which expects the presence of
     *                                        the {@code Annotations} specified in its {@code value} {@code Method}.
     *                                        Should be either:
     *                                        <ul>
     *                                        <li>{@link BeforeAnnotated}; or</li>
     *                                        <li>{@link AfterAnnotated}.</li>
     *                                        </ul>
     * @param nonExpectantFlowAnnotationClass The {@code Class} of the {@code Annotation} which expects the absence of
     *                                        the {@code Annotations} specified in its {@code value} {@code Method}.
     *                                        Should be either:
     *                                        <ul>
     *                                        <li>{@link BeforeNotAnnotated}; or</li>
     *                                        <li>{@link AfterNotAnnotated}.</li>
     *                                        </ul>
     * @param <E>                             The {@code Type} of the {@code expectantFlowAnnotationClass}.
     * @param <N>                             The {@code Type} of the {@code nonExpectantFlowAnnotationClass}.
     */
    private <E extends Annotation, N extends Annotation> void invokeHonouredAnnotatedMethods(
            final TestExtensionContext context,
            final Class<E> expectantFlowAnnotationClass,
            final Class<N> nonExpectantFlowAnnotationClass) {

        @SuppressWarnings("OptionalGetWithoutIsPresent") // Private method, ensured to be present.
        final Method testMethod = context.getTestMethod().get();
        @SuppressWarnings("OptionalGetWithoutIsPresent") // Private method, ensured to be present.
        final Class<?> testClass = context.getTestClass().get();

        // Find all of the annotated methods (checking their declarations).
        final List<FlowMethodRegistration<E>> expectantAnnotatedRegistrations =
                buildRegistrations(testClass, expectantFlowAnnotationClass, true);

        final List<FlowMethodRegistration<N>> nonExpectantAnnotatedRegistrations =
                buildRegistrations(testClass, nonExpectantFlowAnnotationClass, false);

        // Invoke all of the *Annotated methods which require invocation.
        final List<Method> invoked = new ArrayList<>();
        expectantAnnotatedRegistrations.stream().filter(registration ->
                registration.shouldInvokeFor(testMethod)).forEachOrdered(registration -> {
            registration.invokeFor(testMethod, context.getTestInstance());
            invoked.add(registration.getMethod());
        });

        // Invoke all of the *NotAnnotated methods which require invocation, and have not yet been.
        nonExpectantAnnotatedRegistrations.stream().filter(registration ->
                !invoked.contains(registration.getMethod()) &&
                        registration.shouldInvokeFor(testMethod)).forEachOrdered(registration -> {
            registration.invokeFor(testMethod, context.getTestInstance());
            invoked.add(registration.getMethod());
        });
    }

    // TODO (06-09-2016): Implement as TestRule for junit 4.x compatability?
}
