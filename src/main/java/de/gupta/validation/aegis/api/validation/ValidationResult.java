package de.gupta.validation.aegis.api.validation;

public interface ValidationResult
{
	boolean isValid();

	ValidationResult and(ValidationResult other);
}