package de.gupta.validation.aegis.api.validation.outcome;

import de.gupta.aletheia.functional.Unfolding;
import de.gupta.validation.aegis.api.validation.policy.ValidationPolicy;
import de.gupta.validation.aegis.api.validation.result.ValidationResult;
import de.gupta.validation.aegis.api.validation.result.ValidationResultAlgebra;

import java.util.Objects;

public final class OutcomeFactory
{
	public static <M> SuccessfulOutcome<M> success(final M value)
	{
		return SuccessfulOutcomeImpl.of(value, ValidationResultAlgebra.EMPTY_SET_BASED.zero());
	}

	public static <M> PolicyBoundOutcome<M> outcome(final M value, final ValidationResult validationResult,
	                                                final ValidationPolicy policy)
	{
		Objects.requireNonNull(validationResult, "Validation result may not be null");
		Objects.requireNonNull(policy, "Validation policy may not be null");

		return Unfolding.beckon(validationResult)
		                .coronate(policy::isValid, r -> validated(value, r, policy), r -> rejected(r, policy));
	}

	public static <M> ValidatedOutcome<M> validated(final M value, final ValidationResult validationResult,
	                                                final ValidationPolicy policy)
	{
		Objects.requireNonNull(value, "Value may not be null");
		Objects.requireNonNull(validationResult, "Validation result may not be null");
		Objects.requireNonNull(policy, "Validation policy may not be null");

		return ValidatedOutcomeImpl.of(value, validationResult, policy);
	}

	public static <M> RejectedOutcome<M> rejected(final ValidationResult validationResult,
	                                              final ValidationPolicy policy)
	{
		Objects.requireNonNull(validationResult, "Validation result may not be null");
		Objects.requireNonNull(policy, "Validation policy may not be null");

		return RejectedOutcomeImpl.of(validationResult, policy);
	}

	public static <M> SuccessfulOutcome<M> success(final M value, final ValidationResult validationResult)
	{
		Objects.requireNonNull(validationResult, "Validation result may not be null");

		return SuccessfulOutcomeImpl.of(value, validationResult);
	}

	public static <M> FailureOutcome<M> failure(final ValidationResult validationResult)
	{
		return FailureOutcomeImpl.of(validationResult);
	}

	private OutcomeFactory()
	{
	}
}