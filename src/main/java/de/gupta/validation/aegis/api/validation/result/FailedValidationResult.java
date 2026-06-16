package de.gupta.validation.aegis.api.validation.result;

final class FailedValidationResult implements ValidationResult
{
	static final FailedValidationResult INSTANCE = new FailedValidationResult();

	@Override
	public boolean isValid()
	{
		return false;
	}

	@Override
	public ValidationResult and(final ValidationResult other)
	{
		// TODO
		return other;
	}

	private FailedValidationResult()
	{
	}
}