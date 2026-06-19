package de.gupta.validation.aegis.api.validation.validation.outcome;

import de.gupta.validation.aegis.api.validation.validation.result.ValidationResult;

import java.util.Optional;
import java.util.function.Function;

public sealed interface ValidationOutcome<M> permits FailureValidationOutcome, PolicyBoundValidationOutcome,
		SuccessfulValidationOutcome
{
	ValidationResult validationResult();

	boolean isSuccessful();

	Optional<M> optionalValue();

	default <N> ValidationOutcome<N> map(final Function<M, N> mapper)
	{
		return OutcomeOperations.map(this, mapper);
	}
}