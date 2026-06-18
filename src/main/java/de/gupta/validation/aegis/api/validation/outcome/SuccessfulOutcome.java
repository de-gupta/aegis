package de.gupta.validation.aegis.api.validation.outcome;

public sealed interface SuccessfulOutcome<M> extends Outcome<M> permits SuccessfulOutcomeImpl
{
	M value();

	@Override
	default boolean isSuccessful()
	{
		return true;
	}
}