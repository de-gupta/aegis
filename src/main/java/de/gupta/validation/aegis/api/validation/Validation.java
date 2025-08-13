package de.gupta.validation.aegis.api.validation;

@FunctionalInterface
public interface Validation<T>
{
	void validate(T t);
}