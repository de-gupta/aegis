package de.gupta.validation.aegis.api.validation.validator;

import de.gupta.validation.aegis.api.validation.validation.result.ValidationResult;

@FunctionalInterface
public interface Validator<T>
{
	ValidationResult validate(T t);
}