package de.gupta.validation.aegis.api.validation;

@FunctionalInterface
public interface ValidationSpecification<T>
{
	ValidationResult validate(T t);

	default ValidationSpecification<T> and(final ValidationSpecification<T> other)
	{
		return BinaryCompositeValidationSpecification.from(this, other, CompositionType.AND);
	}

	default ValidationSpecification<T> or(final ValidationSpecification<T> other)
	{
		return BinaryCompositeValidationSpecification.from(this, other, CompositionType.OR);
	}

	default ValidationSpecification<T> xor(final ValidationSpecification<T> other)
	{
		return BinaryCompositeValidationSpecification.from(this, other, CompositionType.XOR);
	}

	default ValidationSpecification<T> not()
	{
		return BinaryCompositeValidationSpecification.negation(this);
	}
}