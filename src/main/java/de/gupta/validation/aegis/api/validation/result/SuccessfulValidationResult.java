package de.gupta.validation.aegis.api.validation.result;

final class SuccessfulValidationResult implements ValidationResult
{
	static final SuccessfulValidationResult INSTANCE = new SuccessfulValidationResult();

	@Override
	public boolean isValid()
	{
		return true;
	}

	@Override
	public ValidationResult and(final ValidationResult other)
	{
		// TODO
		return other;
	}

	private SuccessfulValidationResult()
	{
	}
}