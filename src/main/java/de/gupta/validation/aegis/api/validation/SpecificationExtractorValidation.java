package de.gupta.validation.aegis.api.validation;

import de.gupta.aletheia.functional.Unfolding;
import de.gupta.validation.aegis.api.specification.Specification;

import java.util.function.Function;
import java.util.function.Supplier;

record SpecificationExtractorValidation<T, P, E extends RuntimeException>(Function<T, ? extends P> propertyExtractor,
																		  Function<T, Specification<P>> specificationExtractor,
																		  Supplier<E> exceptionSupplier)
		implements Validation<T>
{
	@Override
	public void validate(final T t)
	{
		Unfolding.beckon(t)
				 .sanctify(propertyExtractor, (o, p) -> specificationExtractor.apply(o).isSatisfiedBy(p),
						 v -> v == true, exceptionSupplier);
	}
}