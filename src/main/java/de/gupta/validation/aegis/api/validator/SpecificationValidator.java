package de.gupta.validation.aegis.api.validator;


import de.gupta.validation.aegis.api.validation.Validation;

record SpecificationValidator<T>(Validation<T> validationSpecification) implements Validator<T>
{
	static <T> SpecificationValidator<T> of(final Validation<T> validationSpecification)
	{
		return new SpecificationValidator<>(validationSpecification);
	}

	@Override
	public void validate(final T t)
	{
		validationSpecification.validate(t);
	}
}