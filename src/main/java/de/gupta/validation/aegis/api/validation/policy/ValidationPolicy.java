package de.gupta.validation.aegis.api.validation.policy;

import de.gupta.validation.aegis.api.validation.result.ValidationResult;

@FunctionalInterface
public interface ValidationPolicy
{
	boolean isValid(final ValidationResult validationResult);
}