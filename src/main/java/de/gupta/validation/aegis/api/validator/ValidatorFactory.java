package de.gupta.validation.aegis.api.validator;

import de.gupta.validation.aegis.api.validation.Validation;

import java.util.function.Supplier;

public final class ValidatorFactory
{
	public static <T> Validator<T> fromSpecificationSupplier(final Supplier<Validation<T>> specification)
	{
		return fromSpecification(specification.get());
	}

	public static <T> Validator<T> fromSpecification(final Validation<T> specification)
	{
		return SpecificationValidator.of(specification);
	}
}