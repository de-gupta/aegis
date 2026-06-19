package de.gupta.validation.aegis.api.assessment.assessment.factories.generic;

import de.gupta.aletheia.functional.Unfolding;
import de.gupta.validation.aegis.api.assessment.assessment.Assessment;
import de.gupta.validation.aegis.api.assessment.assessment.result.AssessmentResult;
import de.gupta.validation.aegis.api.assessment.assessment.result.AssessmentResultFactory;
import de.gupta.validation.aegis.api.specification.Specification;
import de.gupta.validation.aegis.api.violation.Violation;

import java.util.function.Function;
import java.util.function.Supplier;

record SpecificationAssessment<T, P, V extends Violation>(Function<T, P> propertyExtractor,
                                                          Specification<P> specification,
                                                          Supplier<V> violationSupplier) implements Assessment<T>
{
	@Override
	public AssessmentResult assess(final T t)
	{
		return Unfolding.beckon(propertyExtractor.apply(t))
		                .coronate(specification::isSatisfiedBy, _ -> AssessmentResultFactory.empty(),
								_ -> AssessmentResultFactory.with(violationSupplier.get()));
	}
}