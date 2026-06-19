package de.gupta.validation.aegis.api.validation.validation.policy;

import de.gupta.validation.aegis.api.policy.Policy;
import de.gupta.validation.aegis.api.validation.validation.result.ValidationResult;

@FunctionalInterface
public interface ValidationPolicy extends Policy<ValidationResult, Boolean>
{
	@Override
	default Boolean apply(final ValidationResult input)
	{
		return isValid(input);
	}

	boolean isValid(final ValidationResult validationResult);
}