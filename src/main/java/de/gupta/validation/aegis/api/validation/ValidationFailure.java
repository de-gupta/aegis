package de.gupta.validation.aegis.api.validation;

record ValidationFailure(String message) implements ValidationResult
{
	private static final ValidationResult INSTANCE = new ValidationFailure("Validation Failed");

	static ValidationResult genericFailure()
	{
		return INSTANCE;
	}

	static ValidationResult withMessage(String message)
	{
		return new ValidationFailure(message);
	}

	@Override
	public boolean isSuccess()
	{
		return false;
	}
}