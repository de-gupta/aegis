package de.gupta.validation.aegis.api.validation;

import de.gupta.validation.aegis.api.validation.result.ValidationResult;

@FunctionalInterface
public interface Validation<T>
{
	default Validation<T> and(Validation<T> other)
	{
		return t -> validate(t).and(other.validate(t));
	}

	ValidationResult validate(T t);
}