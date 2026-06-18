package de.gupta.validation.aegis.api.validation;

import de.gupta.aletheia.functional.Unfolding;
import de.gupta.validation.aegis.api.specification.Specification;
import de.gupta.validation.aegis.api.validation.result.ValidationResult;
import de.gupta.validation.aegis.api.validation.result.ValidationResultFactory;
import de.gupta.validation.aegis.api.violation.Violation;

import java.util.function.Function;
import java.util.function.Supplier;

record SpecificationValidation<T, P, V extends Violation>(Function<T, P> propertyExtractor,
                                                          Specification<P> specification,
                                                          Supplier<V> violationSupplier)
		implements Validation<T>
{
	@Override
	public ValidationResult validate(final T t)
	{
		return Unfolding.beckon(t)
		                .metamorphose(propertyExtractor)
		                .coronate(specification::isSatisfiedBy, _ -> ValidationResultFactory.empty(),
								_ -> ValidationResultFactory.with(violationSupplier.get()));
	}
}