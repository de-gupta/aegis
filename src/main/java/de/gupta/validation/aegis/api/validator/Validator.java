package de.gupta.validation.aegis.api.validator;

import de.gupta.validation.aegis.api.validation.ValidationResult;

public interface Validator<T>
{
	ValidationResult validate(T t);
}