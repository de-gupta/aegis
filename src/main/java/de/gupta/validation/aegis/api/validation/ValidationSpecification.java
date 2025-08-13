package de.gupta.validation.aegis.api.validation;

import de.gupta.validation.aegis.api.specification.Specification;

public interface ValidationSpecification<T> extends Specification<T>
{
	void validate(T t);

	default ValidationSpecification<T> and(final ValidationSpecification<T> other)
	{
		return AndValidationSpecification.from(this, other);
	}

	default ValidationSpecification<T> or(final ValidationSpecification<T> other)
	{
		return OrValidationSpecification.from(this, other);
	}

	default ValidationSpecification<T> xor(final ValidationSpecification<T> other)
	{
		return XorValidationSpecification.from(this, other);
	}

	@Override
	default ValidationSpecification<T> not()
	{
		return NotValidationSpecification.from(this);
	}
}