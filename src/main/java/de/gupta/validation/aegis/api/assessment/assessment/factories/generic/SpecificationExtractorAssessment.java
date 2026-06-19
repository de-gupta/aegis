package de.gupta.validation.aegis.api.assessment.assessment.factories.generic;

import de.gupta.aletheia.functional.Unfolding;
import de.gupta.validation.aegis.api.assessment.assessment.Assessment;
import de.gupta.validation.aegis.api.assessment.assessment.result.AssessmentResult;
import de.gupta.validation.aegis.api.assessment.assessment.result.AssessmentResultFactory;
import de.gupta.validation.aegis.api.specification.Specification;
import de.gupta.validation.aegis.api.violation.Violation;

import java.util.function.Function;
import java.util.function.Supplier;

record SpecificationExtractorAssessment<T, P, V extends Violation>(Function<T, ? extends P> propertyExtractor,
                                                                   Function<T, Specification<P>> specificationExtractor,
                                                                   Supplier<V> violationSupplier) implements
		Assessment<T>
{
	@Override
	public AssessmentResult assess(final T t)
	{
		return Unfolding.beckon(propertyExtractor.apply(t))
		                .coronate(property -> specificationExtractor.apply(t).isSatisfiedBy(property),
								_ -> AssessmentResultFactory.empty(),
								_ -> AssessmentResultFactory.with(violationSupplier.get()));
	}
}
