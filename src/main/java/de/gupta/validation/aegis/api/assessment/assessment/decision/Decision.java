package de.gupta.validation.aegis.api.assessment.assessment.decision;

import de.gupta.validation.aegis.api.assessment.assessment.result.AssessmentResult;

import java.util.function.Function;

public interface Decision<M, D>
{
	M value();

	AssessmentResult assessmentResult();

	D decision();

	default <N> Decision<N, D> map(final Function<M, N> mapper)
	{
		return DecisionOperations.map(this, mapper);
	}
}
