package de.gupta.validation.aegis.api.validation.validation.policy;

import de.gupta.commons.utility.comparison.ComparisonType;
import de.gupta.validation.aegis.api.violation.Severity;

public final class ValidationPolicyFactory
{
	public static ValidationPolicy withComparingSeverity(final Severity threshold, final ComparisonType comparisonType)
	{
		return result -> result.isValid(threshold, comparisonType);
	}

	public static ValidationPolicy withSeverityLessThan(final Severity threshold)
	{
		return result -> result.isValid(threshold, ComparisonType.LESS_THAN);
	}

	public static ValidationPolicy withAtMostLowSeverity()
	{
		return withSeverityAtMost(Severity.LOW);
	}

	public static ValidationPolicy withSeverityAtMost(final Severity threshold)
	{
		return result -> result.isValid(threshold, ComparisonType.LESS_THAN_OR_EQUAL);
	}

	public static ValidationPolicy withAtMostMediumSeverity()
	{
		return withSeverityAtMost(Severity.MEDIUM);
	}

	public static ValidationPolicy allowing()
	{
		return _ -> true;
	}

	public static ValidationPolicy blocking()
	{
		return _ -> false;
	}

	private ValidationPolicyFactory()
	{
	}
}