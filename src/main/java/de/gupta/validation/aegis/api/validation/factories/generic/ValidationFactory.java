package de.gupta.validation.aegis.api.validation.factories.generic;

import de.gupta.validation.aegis.api.specification.Specification;
import de.gupta.validation.aegis.api.validation.Validation;
import de.gupta.validation.aegis.api.violation.Violation;

import java.util.function.Function;
import java.util.function.Supplier;

public final class ValidationFactory
{
	public static <T, P, V extends Violation> Validation<T> from(final Function<T, P> propertyExtractor,
	                                                             final Specification<P> specification,
	                                                             final Supplier<V> violationSupplier)
	{
		return new SpecificationValidation<>(propertyExtractor, specification, violationSupplier);
	}

	public static <T, P, V extends Violation> Validation<T> fromExtractor(
			final Function<T, ? extends P> propertyExtractor,
			final Function<T, Specification<P>> specificationExtractor,
			final Supplier<V> violationSupplier)
	{
		return new SpecificationExtractorValidation<>(propertyExtractor, specificationExtractor, violationSupplier);
	}
}