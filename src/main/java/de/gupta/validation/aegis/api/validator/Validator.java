package de.gupta.validation.aegis.api.validator;

public interface Validator<T>
{
	void validate(final T t);
}