package de.gupta.validation.aegis.api.validation.validation.outcome;

import java.util.function.Function;

public final class OutcomeOperations
{
	public static <M, N> ValidationOutcome<N> map(final ValidationOutcome<M> validationOutcome,
	                                              final Function<M, N> mapper)
	{
		return switch (validationOutcome)
		{
			case PolicyBoundValidationOutcome<M> policyBoundOutcome -> switch (policyBoundOutcome)
			{
				case ValidatedValidationOutcome<M> valid ->
						OutcomeFactory.validated(mapper.apply(valid.value()), valid.validationResult(), valid.policy());
				case RejectedValidationOutcome<M> rejected ->
						OutcomeFactory.rejected(rejected.validationResult(), rejected.policy());
			};
			case SuccessfulValidationOutcome<M> success ->
					OutcomeFactory.success(mapper.apply(success.value()), success.validationResult());
			case FailureValidationOutcome<M> failure -> OutcomeFactory.failure(failure.validationResult());
		};
	}
}