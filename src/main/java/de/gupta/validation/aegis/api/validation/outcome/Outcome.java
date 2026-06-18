package de.gupta.validation.aegis.api.validation.outcome;

import de.gupta.validation.aegis.api.validation.result.ValidationResult;

import java.util.Optional;
import java.util.function.Function;

public sealed interface Outcome<M> permits FailureOutcome, PolicyBoundOutcome, SuccessfulOutcome
{
	ValidationResult validationResult();

	boolean isSuccessful();

	Optional<M> optionalValue();

	default <N> Outcome<N> map(final Function<M, N> mapper)
	{
		return OutcomeOperations.map(this, mapper);
	}
}