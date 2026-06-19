package de.gupta.validation.aegis.api.assessment.assessment.policy;

import de.gupta.validation.aegis.api.assessment.assessment.result.AssessmentResult;
import de.gupta.validation.aegis.api.policy.Policy;

@FunctionalInterface
public interface DecisionPolicy<D> extends Policy<AssessmentResult, D>
{
	@Override
	default D apply(final AssessmentResult input)
	{
		return decide(input);
	}

	D decide(AssessmentResult assessmentResult);
}