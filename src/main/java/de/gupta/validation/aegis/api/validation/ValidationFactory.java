package de.gupta.validation.aegis.api.validation;

import de.gupta.validation.aegis.api.specification.Specification;

import java.util.function.Function;
import java.util.function.Supplier;

public final class ValidationFactory<T>
{
	public static <T, P, E extends RuntimeException> Validation<T> from(final Function<T, P> propertyExtractor,
																		final Specification<P> specification,
																		final Supplier<E> exceptionSupplier)
	{
		return new SpecificationValidation<>(propertyExtractor, specification, exceptionSupplier);
	}

	public static <T, P, E extends RuntimeException> Validation<T> from(final Function<T, P> propertyExtractor,
																		final Function<T, Specification<P>> specificationExtractor,
																		final Supplier<E> exceptionSupplier)
	{
		return new SpecificationExtractorValidation<>(propertyExtractor, specificationExtractor, exceptionSupplier);
	}
}