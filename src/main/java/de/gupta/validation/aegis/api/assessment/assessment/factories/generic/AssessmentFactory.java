package de.gupta.validation.aegis.api.assessment.assessment.factories.generic;

import de.gupta.validation.aegis.api.assessment.assessment.Assessment;
import de.gupta.validation.aegis.api.specification.Specification;
import de.gupta.validation.aegis.api.violation.Violation;

import java.util.function.Function;
import java.util.function.Supplier;

public final class AssessmentFactory
{
	public static <T, P, V extends Violation> Assessment<T> from(final Function<T, P> propertyExtractor,
	                                                             final Specification<P> specification,
	                                                             final Supplier<V> violationSupplier)
	{
		return new SpecificationAssessment<>(propertyExtractor, specification, violationSupplier);
	}

	public static <T, P, V extends Violation> Assessment<T> fromExtractor(
			final Function<T, ? extends P> propertyExtractor,
			final Function<T, Specification<P>> specificationExtractor,
			final Supplier<V> violationSupplier)
	{
		return new SpecificationExtractorAssessment<>(propertyExtractor, specificationExtractor, violationSupplier);
	}

	private AssessmentFactory()
	{
	}
}
