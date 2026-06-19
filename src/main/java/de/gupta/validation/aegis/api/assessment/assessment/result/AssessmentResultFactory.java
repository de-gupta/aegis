package de.gupta.validation.aegis.api.assessment.assessment.result;

import de.gupta.validation.aegis.api.violation.Violation;

import java.util.Set;
import java.util.function.Supplier;

public final class AssessmentResultFactory
{
	public static AssessmentResult empty()
	{
		return with(Set.of());
	}

	public static AssessmentResult with(final Set<Violation> violations)
	{
		return () -> violations;
	}

	public static AssessmentResult with(final Violation violation)
	{
		return with(Set.of(violation));
	}

	public static AssessmentResult with(final Supplier<Violation> violation)
	{
		return with(Set.of(violation.get()));
	}

	private AssessmentResultFactory()
	{
	}
}
