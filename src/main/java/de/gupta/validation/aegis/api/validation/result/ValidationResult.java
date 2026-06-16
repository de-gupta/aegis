package de.gupta.validation.aegis.api.validation.result;

public interface ValidationResult
{
	boolean isValid();

	ValidationResult and(ValidationResult other);
}