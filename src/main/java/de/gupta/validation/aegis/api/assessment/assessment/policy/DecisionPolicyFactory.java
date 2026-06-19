package de.gupta.validation.aegis.api.assessment.assessment.policy;

import de.gupta.commons.utility.comparison.ComparisonType;
import de.gupta.validation.aegis.api.violation.Severity;

public final class DecisionPolicyFactory
{
	public static <D> DecisionPolicy<D> withComparingSeverity(final Severity threshold,
	                                                          final ComparisonType comparisonType,
	                                                          final D matchingDecision,
	                                                          final D nonMatchingDecision)
	{
		return result -> result.highestSeverityMatches(threshold, comparisonType)
				? matchingDecision
				: nonMatchingDecision;
	}

	public static <D> DecisionPolicy<D> withSeverityLessThan(final Severity threshold, final D lowerDecision,
	                                                         final D greaterOrEqualDecision)
	{
		return result -> result.highestSeverityLessThan(threshold) ? lowerDecision : greaterOrEqualDecision;
	}

	public static <D> DecisionPolicy<D> constant(final D decision)
	{
		return _ -> decision;
	}

	private DecisionPolicyFactory()
	{
	}
}