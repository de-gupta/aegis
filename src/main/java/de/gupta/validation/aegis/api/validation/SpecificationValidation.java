package de.gupta.validation.aegis.api.validation;

import de.gupta.aletheia.functional.Unfolding;
import de.gupta.validation.aegis.api.specification.Specification;

import java.util.function.Function;
import java.util.function.Supplier;

record SpecificationValidation<T, P, E extends RuntimeException>(Function<T, P> propertyExtractor,
                                                                 Specification<P> specification,
                                                                 Supplier<E> exceptionSupplier)
		implements Validation<T>
{
	@Override
	public ValidationResult validate(final T t)
	{
		// TODO
		Unfolding.beckon(t)
		         .metamorphose(propertyExtractor)
		         .discern(specification::isSatisfiedBy, exceptionSupplier);
		return null;
	}
}