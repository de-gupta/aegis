package de.gupta.validation.aegis.api.validation.result;

import de.gupta.validation.aegis.api.violation.Violation;

import java.util.Collection;
import java.util.List;

final class FailedValidationResult implements ValidationResult
{
	static final FailedValidationResult INSTANCE = new FailedValidationResult();

	@Override
	public boolean isValid()
	{
		return false;
	}

	@Override
	public Collection<Violation> blockingViolations()
	{
		return List.of();
	}

	@Override
	public Collection<Violation> toleratedViolations()
	{
		return List.of();
	}

	@Override
	public ValidationResult complement()
	{
		return null;
	}

	@Override
	public ValidationResult supremum()
	{
		return null;
	}

	@Override
	public ValidationResult infimum()
	{
		return null;
	}

	@Override
	public ValidationResult join(final ValidationResult validationResult)
	{
		return null;
	}

	@Override
	public ValidationResult meet(final ValidationResult validationResult)
	{
		return null;
	}

	private FailedValidationResult()
	{
	}
}