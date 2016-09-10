package com.com.fail;

/**
 * Package internal {@link Exception} used to indicate that the executing {@code test} failed due to the {@link
 * ExpectedFailure} processing.  This can either be because a {@code Test} which was expected to fail succeeded; or a
 * {@code Test} which was not expected to fail failed.
 */
class ExpectedFailureException extends AssertionError {

    /**
     * Constructs an {@link ExpectedFailureException} with the given {@code message} and {@code cause}.
     *
     * @param message The {@code message} explaining the reason this {@link Exception} was constructed.
     * @param cause   The {@link Throwable} which caused this {@link Exception} to be constructed.
     */
    ExpectedFailureException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs an {@link ExpectedFailureException} with the given {@code message}.
     * <p>
     * To be used in cases where there is <strong>no</strong> {@link Throwable causing Throwable}.
     *
     * @param message The {@code message} explaining the reason this {@link Exception} was constructed.
     */
    ExpectedFailureException(final String message) {
        super(message);
    }
}
