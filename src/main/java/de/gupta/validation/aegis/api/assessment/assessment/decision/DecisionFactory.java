package de.gupta.validation.aegis.api.assessment.assessment.decision;

import de.gupta.aletheia.functional.Unfolding;
import de.gupta.commons.utility.exception.ExceptionHelper;
import de.gupta.validation.aegis.api.assessment.assessment.policy.DecisionPolicy;
import de.gupta.validation.aegis.api.assessment.assessment.result.AssessmentResult;
import de.gupta.validation.aegis.api.assessment.assessment.result.AssessmentResultAlgebra;

import java.util.Objects;

public final class DecisionFactory
{
	public static <M, D> Decision<M, D> decision(final M value, final D decision)
	{
		return DecisionImpl.of(value, AssessmentResultAlgebra.EMPTY_SET_BASED.zero(), decision);
	}

	public static <M, D> Decision<M, D> decided(final M value, final AssessmentResult assessmentResult,
	                                            final DecisionPolicy<D> decisionPolicy)
	{
		Objects.requireNonNull(decisionPolicy, "Decision policy may not be null");
		return decision(value, assessmentResult, decisionPolicy.decide(assessmentResult));
	}

	public static <M, D> Decision<M, D> decision(final M value, final AssessmentResult assessmentResult,
	                                             final D decision)
	{
		return DecisionImpl.of(value, assessmentResult, decision);
	}

	private DecisionFactory()
	{
	}
}

record DecisionImpl<M, D>(M value, AssessmentResult assessmentResult, D decision) implements Decision<M, D>
{
	static <M, D> Decision<M, D> of(final M value, final AssessmentResult assessmentResult, final D decision)
	{
		Objects.requireNonNull(assessmentResult, "Assessment result may not be null");
		return Unfolding.beckon(value)
		                .metamorphose(v -> new DecisionImpl<>(v, assessmentResult, decision))
		                .decree(ExceptionHelper.iaeFrom("Value may not be null"));
	}
}