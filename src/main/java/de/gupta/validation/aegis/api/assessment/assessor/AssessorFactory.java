package de.gupta.validation.aegis.api.assessment.assessor;

import de.gupta.validation.aegis.api.assessment.assessment.Assessment;
import de.gupta.validation.aegis.api.assessment.assessment.result.AssessmentResult;
import de.gupta.validation.aegis.api.assessment.assessment.result.AssessmentResultFactory;

import java.util.Collection;

public final class AssessorFactory
{
	public static <T> Assessor<T> with(final Collection<Assessment<T>> assessments)
	{
		return t -> assessments.stream()
		                       .map(assessment -> assessment.assess(t))
		                       .reduce(AssessmentResult::add)
		                       .orElse(AssessmentResultFactory.empty());
	}

	private AssessorFactory()
	{
	}
}
