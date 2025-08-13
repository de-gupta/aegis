package de.gupta.validation.aegis.api.validator;


import de.gupta.validation.aegis.api.specification.validation.ValidationSpecification;

record SpecificationValidator<T>(ValidationSpecification<T> validationSpecification) implements Validator<T>
{
	@Override
	public void validate(final T t)
	{
		validationSpecification.validate(t);
	}

	static <T> SpecificationValidator<T> of(final ValidationSpecification<T> validationSpecification)
	{
		return new SpecificationValidator<>(validationSpecification);
	}
}