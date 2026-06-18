package de.gupta.validation.aegis.api.validation.outcome;

import java.util.Optional;

public sealed interface FailureOutcome<M> extends Outcome<M> permits FailureOutcomeImpl
{
	@Override
	default boolean isSuccessful()
	{
		return false;
	}

	@Override
	default Optional<M> optionalValue()
	{
		return Optional.empty();
	}

}