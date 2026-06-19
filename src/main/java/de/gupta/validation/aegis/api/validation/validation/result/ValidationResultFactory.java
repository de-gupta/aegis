package de.gupta.validation.aegis.api.validation.validation.result;

import de.gupta.validation.aegis.api.violation.Violation;

import java.util.Set;
import java.util.function.Supplier;

public final class ValidationResultFactory
{
	public static ValidationResult empty()
	{
		return with(Set.of());
	}

	public static ValidationResult with(final Set<Violation> violations)
	{
		return () -> violations;
	}

	public static ValidationResult with(final Violation violation)
	{
		return with(Set.of(violation));
	}

	public static ValidationResult with(final Supplier<Violation> violation)
	{
		return with(Set.of(violation.get()));
	}

	private ValidationResultFactory()
	{
	}
}