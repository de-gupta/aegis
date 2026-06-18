package de.gupta.validation.aegis.api.validation.outcome;

import de.gupta.validation.aegis.api.validation.result.ValidationResult;

import java.util.Optional;

public sealed interface Outcome<M> permits FailureOutcome, PolicyBoundOutcome, SuccessfulOutcome
{
	ValidationResult validationResult();

	boolean isSuccessful();

	Optional<M> optionalValue();
}