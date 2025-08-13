package de.gupta.validation.aegis.api.validator;

import de.gupta.validation.aegis.api.specification.validation.ValidationSpecification;

public abstract class AbstractValidator<T> implements Validator<T>
{
	private final ValidationSpecification<T> validationSpecification;

	@Override
	public void validate(final T t)
	{
		validationSpecification.validate(t);
	}

	protected AbstractValidator(final ValidationSpecification<T> validationSpecification)
	{
		this.validationSpecification = validationSpecification;
	}
}