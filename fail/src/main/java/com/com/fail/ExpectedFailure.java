package com.com.fail;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestExtensionContext;

import java.lang.reflect.Method;

/**
 * {@link org.junit.jupiter.api.extension.Extension} used to mark {@link org.junit.jupiter.api.Test Tests} which {@link
 * ShouldFail should fail}.
 * <p>
 * If a {@code Test} marked with {@code ShouldFail} does <i>not</i> {@code throw} an {@link Throwable}, then the {@code
 * Test} is deemed to have failed.
 */
public final class ExpectedFailure
        implements BeforeTestExecutionCallback, AfterTestExecutionCallback, TestExecutionExceptionHandler {

    /**
     * Used to store state from before to after the test.  Set to {@code true} if the {@link
     * TestExtensionContext#getTestMethod() Test Method} is {@link java.lang.annotation.Annotation annotated} by {@link
     * ShouldFail}; otherwise {@code false}.
     */
    private volatile boolean expectsFailure;

    /**
     * Used to store whether the current {@link org.junit.jupiter.api.Test} originally threw a {@link Throwable}, which
     * was swallowed by the {@link #handleTestExecutionException(TestExtensionContext, Throwable) Exception handler}.
     * <p>
     * This is used to detect {@code Tests} which {@link ShouldFail should fail}, but haven't.
     */
    private volatile boolean passedThroughExceptionHandler;

    /**
     * {@inheritDoc}
     * <p>
     * Resets the {@link #expectsFailure} and {@link #passedThroughExceptionHandler} flags.  If the {@link ShouldFail}
     * {@link java.lang.annotation.Annotation} is present on the executing {@code test} {@code method}, sets the {@code
     * expectsFailure} flag to {@code true}.
     */
    @Override
    public void beforeTestExecution(final TestExtensionContext context) throws Exception {

        // Reset our flags.
        this.expectsFailure = false;
        this.passedThroughExceptionHandler = false;

        @SuppressWarnings("OptionalGetWithoutIsPresent") // Internal call, confirmed as present.
        final Method testMethod = context.getTestMethod().get();
        if (testMethod.isAnnotationPresent(ShouldFail.class)) {
            // If the method has the ShouldFail annotation, set the 'expectsFailure' flag.
            this.expectsFailure = true;
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Sets the {@link #passedThroughExceptionHandler} flag to {@code true}.  If the currently executing test was
     * <i>not</i> {@link #expectsFailure expected to fail}, then throws a {@link ExpectedFailureException}.
     * <p>
     * Note: This is safe to do, since this method is only invoked if the currently executing {@code test} <i>has</i>
     * failed.
     *
     * @throws ExpectedFailureException If the method was not {@code expected to fail}.
     */
    @Override
    public void handleTestExecutionException(final TestExtensionContext context, final Throwable throwable)
            throws ExpectedFailureException {

        // Set the 'passedThroughExceptionHandler' flag.
        this.passedThroughExceptionHandler = true;

        if (!expectsFailure) {
            // The test threw a Throwable, but we weren't expecting it to fail.
            // Throw a new Exception.
            //noinspection OptionalGetWithoutIsPresent
            throw new ExpectedFailureException(String.format("Test method %s failed, but shouldn't have.%n%s",
                    context.getTestMethod().get().getName(), throwable));
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * If the currently executing {@code test} was {@link #expectsFailure expected to fail}, but {@link
     * #passedThroughExceptionHandler it did not}, throws a {@link ExpectedFailureException}.
     *
     * @throws ExpectedFailureException If the method was {@code expected to fail} but didn't.
     */
    @Override
    public void afterTestExecution(final TestExtensionContext context) throws ExpectedFailureException {

        if (expectsFailure) {
            if (!passedThroughExceptionHandler) {

                // If we were expecting a failure, but we didn't pass through the exceptionHandler method, the test
                // did not throw a Throwable (as expected in the case of a failure).
                // Throw a new Exception.
                //noinspection OptionalGetWithoutIsPresent
                throw new ExpectedFailureException(String.format("Test method %s did not fail as expected",
                        context.getTestMethod().get().getName()));
            }
        }
    }
}
