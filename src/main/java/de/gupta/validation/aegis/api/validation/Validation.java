package de.gupta.validation.aegis.api.validation;

@FunctionalInterface
public interface Validation<T>
{
	default Validation<T> and(Validation<T> other)
	{
		return t ->
		{
			validate(t);
			other.validate(t);
		};
	}

	void validate(T t);
}