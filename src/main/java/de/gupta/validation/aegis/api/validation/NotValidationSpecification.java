package de.gupta.validation.aegis.api.validation;

record NotValidationSpecification<T>(ValidationSpecification<T> specification)
		implements ValidationSpecification<T>
{
	static <T> ValidationSpecification<T> from(final ValidationSpecification<T> specification)
	{
		return new NotValidationSpecification<>(specification);
	}

	@Override
	public void validate(final T t)
	{
		// TODO
	}

	@Override
	public boolean isSatisfiedBy(final T t)
	{
		return !specification.isSatisfiedBy(t);
	}
}