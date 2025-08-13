package de.gupta.validation.aegis.api.validation;

record BinaryCompositeValidationSpecification<T>(ValidationSpecification<T> left, ValidationSpecification<T> right,
												 CompositionType compositionType) implements ValidationSpecification<T>
{
	static <T> ValidationSpecification<T> negation(final ValidationSpecification<T> specification)
	{
		return BinaryCompositeValidationSpecification.from(specification, null, CompositionType.NOT);
	}

	static <T> ValidationSpecification<T> from(final ValidationSpecification<T> left,
											   final ValidationSpecification<T> right,
											   final CompositionType compositionType)
	{
		return new BinaryCompositeValidationSpecification<>(left, right, compositionType);
	}

	@Override
	public ValidationResult validate(final T t)
	{
		return switch (compositionType)
		{
			case AND -> left.validate(t).and(right.validate(t));
			case OR -> left.validate(t).or(right.validate(t));
			case XOR -> left.validate(t).xor(right.validate(t));
			case NOT -> left.validate(t).not();
		};
	}

}