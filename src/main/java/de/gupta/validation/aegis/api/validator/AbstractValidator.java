package de.gupta.validation.aegis.api.validator;

import de.gupta.validation.aegis.api.validation.Validation;

public abstract class AbstractValidator<T> implements Validator<T>
{
	private final Validation<T> validationSpecification;

	@Override
	public void validate(final T t)
	{
		validationSpecification.validate(t);
	}

	protected AbstractValidator(final Validation<T> validationSpecification)
	{
		this.validationSpecification = validationSpecification;
	}
}