package de.gupta.validation.aegis.api.validation;

import de.gupta.aletheia.functional.Unfolding;
import de.gupta.validation.aegis.api.specification.Specification;
import de.gupta.validation.aegis.api.validation.result.ValidationResult;
import de.gupta.validation.aegis.api.validation.result.ValidationResultFactory;
import de.gupta.validation.aegis.api.violation.Violation;

import java.util.function.Function;
import java.util.function.Supplier;

record SpecificationExtractorValidation<T, P, V extends Violation>(Function<T, ? extends P> propertyExtractor,
                                                                   Function<T, Specification<P>> specificationExtractor,
                                                                   Supplier<V> violationSupplier)
		implements Validation<T>
{
	@Override
	public ValidationResult validate(final T t)
	{
		return Unfolding.beckon(t)
		                .metamorphose(propertyExtractor)
		                .coronate(p -> specificationExtractor.apply(t).isSatisfiedBy(p),
								_ -> ValidationResultFactory.empty(),
								_ -> ValidationResultFactory.with(violationSupplier.get()));
	}
}