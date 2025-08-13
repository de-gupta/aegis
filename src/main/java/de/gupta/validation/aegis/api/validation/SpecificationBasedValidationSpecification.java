package de.gupta.validation.aegis.api.validation;

import de.gupta.aletheia.functional.Unfolding;
import de.gupta.validation.aegis.api.exception.NullObjectException;
import de.gupta.validation.aegis.api.specification.Specification;

public final class SpecificationBasedValidationSpecification<T> implements ValidationSpecification<T>
{
	private final Specification<T> specification;

	public static <T> SpecificationBasedValidationSpecification<T> of(
			final Specification<T> specification)
	{
		return new SpecificationBasedValidationSpecification<>(specification);
	}

	@Override
	public void validate(final T t)
	{
		// TODO
		return Unfolding.beckon(t)
						.cleave(specification::isSatisfiedBy,
								_ -> ValidationSuccess.instance(),
								_ -> ValidationFailure.from(null))
						.decree(NullObjectException.fromMessage("Object to validate cannot be null."));
	}

	private SpecificationBasedValidationSpecification(final Specification<T> specification)
	{
		this.specification = specification;
	}
}