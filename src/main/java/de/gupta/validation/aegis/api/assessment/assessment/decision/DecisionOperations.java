package de.gupta.validation.aegis.api.assessment.assessment.decision;

import java.util.function.Function;

public final class DecisionOperations
{
	public static <M, N, D> Decision<N, D> map(final Decision<M, D> decision, final Function<M, N> mapper)
	{
		return DecisionFactory.decision(mapper.apply(decision.value()), decision.assessmentResult(),
				decision.decision());
	}

	private DecisionOperations()
	{
	}
}