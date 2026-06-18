package de.gupta.validation.aegis.api.violation;

record ViolationImpl(String message, Severity severity) implements Violation
{
	static Violation critical(final String message)
	{
		return with(message, Severity.CRITICAL);
	}

	static Violation with(final String message, final Severity severity)
	{
		return new ViolationImpl(message, severity);
	}

	static Violation high(final String message)
	{
		return with(message, Severity.HIGH);
	}

	static Violation low(final String message)
	{
		return with(message, Severity.LOW);
	}

	static Violation consideration(final String message)
	{
		return with(message, Severity.CONSIDERATION);
	}
}