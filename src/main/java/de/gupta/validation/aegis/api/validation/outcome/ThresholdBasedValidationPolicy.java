package de.gupta.validation.aegis.api.validation.outcome;

import de.gupta.validation.aegis.api.validation.result.ValidationResult;
import de.gupta.validation.aegis.api.violation.Severity;

final class ThresholdBasedValidationPolicy implements ValidationPolicy
{
	private final Severity threshold;

	@Override
	public boolean isValid(final ValidationResult validationResult)
	{
		return validationResult.isValid(threshold);
	}

	ThresholdBasedValidationPolicy(final Severity threshold)
	{
		this.threshold = threshold;
	}
}