package de.gupta.validation.aegis.api.violation;

public final class ViolationFactory
{
	public static Violation with(final String message, final Severity severity)
	{
		return ViolationImpl.with(message, severity);
	}

	public static Violation critical(final String message)
	{
		return ViolationImpl.critical(message);
	}

	public static Violation high(final String message)
	{
		return ViolationImpl.high(message);
	}

	public static Violation low(final String message)
	{
		return ViolationImpl.low(message);
	}

	public static Violation consideration(final String message)
	{
		return ViolationImpl.consideration(message);
	}

	private ViolationFactory()
	{
	}
}