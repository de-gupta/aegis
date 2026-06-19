package de.gupta.validation.aegis.api.validation.validation.policy;

import de.gupta.validation.aegis.api.validation.validation.result.ValidationResult;

@FunctionalInterface
public interface ValidationPolicy
{
	boolean isValid(final ValidationResult validationResult);
}