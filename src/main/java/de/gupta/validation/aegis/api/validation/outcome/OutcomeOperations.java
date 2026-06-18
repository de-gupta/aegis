package de.gupta.validation.aegis.api.validation.outcome;

import java.util.function.Function;

public final class OutcomeOperations
{
	public static <M, N> Outcome<N> map(final Outcome<M> outcome, final Function<M, N> mapper)
	{
		return switch (outcome)
		{
			case PolicyBoundOutcome<M> policyBoundOutcome -> switch (policyBoundOutcome)
			{
				case ValidatedOutcome<M> valid ->
						OutcomeFactory.validated(mapper.apply(valid.value()), valid.validationResult(), valid.policy());
				case RejectedOutcome<M> rejected ->
						OutcomeFactory.rejected(rejected.validationResult(), rejected.policy());
			};
			case SuccessfulOutcome<M> success ->
					OutcomeFactory.success(mapper.apply(success.value()), success.validationResult());
			case FailureOutcome<M> failure -> OutcomeFactory.failure(failure.validationResult());
		};
	}
}