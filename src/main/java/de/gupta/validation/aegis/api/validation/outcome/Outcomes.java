package de.gupta.validation.aegis.api.validation.outcome;

import de.gupta.aletheia.functional.Unfolding;
import de.gupta.commons.utility.exception.ExceptionHelper;
import de.gupta.validation.aegis.api.validation.result.ValidationResult;
import de.gupta.validation.aegis.api.validation.result.ValidationResultAlgebra;

public final class Outcomes
{
	public static <M> SuccessfulOutcome<M> success(final M value)
	{
		return SuccessfulOutcomeImpl.of(value, ValidationResultAlgebra.EMPTY_SET_BASED.zero());
	}

	public static <M> Outcome<M> outcome(final M value, final ValidationResult validationResult,
	                                     final ValidationPolicy policy)
	{
		return Unfolding.beckon(validationResult)
		                .coronate(policy::isValid, r -> success(value, r), Outcomes::failure);
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

	private Outcomes()
	{
	}
}