package de.gupta.validation.aegis.api.validator;

import de.gupta.validation.aegis.api.validation.result.ValidationResult;

@FunctionalInterface
public interface Validator<T>
{
	ValidationResult validate(T t);
}