package de.gupta.validation.aegis.api.validation.outcome;

import de.gupta.aletheia.functional.Unfolding;
import de.gupta.commons.utility.exception.ExceptionHelper;
import de.gupta.validation.aegis.api.validation.result.ValidationResult;
import de.gupta.validation.aegis.api.validation.result.ValidationResultAlgebra;

public final class OutcomeFactory
{
	public static <M> SuccessfulOutcome<M> success(final M value)
	{
		return SuccessfulOutcomeImpl.of(value, ValidationResultAlgebra.EMPTY_SET_BASED.zero());
	}

	public static <M> PolicyBoundOutcome<M> outcome(final M value, final ValidationResult validationResult,
	                                                final ValidationPolicy policy)
	{
		return Unfolding.beckon(validationResult)
		                .coronate(policy::isValid, r -> validated(value, r, policy), r -> rejected(r, policy));
	}

	public static <M> ValidatedOutcome<M> validated(final M value, final ValidationResult validationResult,
	                                                final ValidationPolicy policy)
	{
		return ValidatedOutcomeImpl.of(value, validationResult, policy);
	}

	public static <M> RejectedOutcome<M> rejected(final ValidationResult validationResult,
	                                              final ValidationPolicy policy)
	{
		return RejectedOutcomeImpl.of(validationResult, policy);
	}

	public static <M> SuccessfulOutcome<M> success(final M value, final ValidationResult validationResult)
	{
		return SuccessfulOutcomeImpl.of(value, validationResult);
	}

	public static <M> FailureOutcome<M> failure(final ValidationResult validationResult)
	{
		return Unfolding.beckon(validationResult)
		                .interdict(s -> s.violations().isEmpty(),
								ExceptionHelper.iaeFrom("Violations set may not be empty"))
		                .coronate(FailureOutcomeImpl::new);
	}

	private OutcomeFactory()
	{
	}
}