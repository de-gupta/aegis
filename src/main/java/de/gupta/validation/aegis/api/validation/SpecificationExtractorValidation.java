package de.gupta.validation.aegis.api.validation;

import de.gupta.aletheia.functional.Unfolding;
import de.gupta.validation.aegis.api.specification.Specification;
import de.gupta.validation.aegis.api.validation.result.ValidationResult;

import java.util.function.Function;
import java.util.function.Supplier;

record SpecificationExtractorValidation<T, P, E extends RuntimeException>(Function<T, ? extends P> propertyExtractor,
                                                                          Function<T, Specification<P>> specificationExtractor,
                                                                          Supplier<E> exceptionSupplier)
		implements Validation<T>
{
	@Override
	public ValidationResult validate(final T t)
	{
		// TODO
		Unfolding.beckon(t)
		         .sanctify(propertyExtractor, (o, p) -> specificationExtractor.apply(o).isSatisfiedBy(p),
						 v -> v == true, exceptionSupplier);
		return null;
	}
}