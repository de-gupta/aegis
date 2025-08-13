package de.gupta.validation.aegis.api.specification;

public record OrSpecification<T, V extends Specification<T>>(V firstSpecification, V secondSpecification)
		implements Specification<T>
{
	public static <T, V extends Specification<T>> OrSpecification<T, V> from(final V firstSpecification,
																			 final V secondSpecification)
	{
		return new OrSpecification<>(firstSpecification, secondSpecification);
	}

	@Override
	public boolean isSatisfiedBy(final T t)
	{
		return firstSpecification.isSatisfiedBy(t) || secondSpecification.isSatisfiedBy(t);
	}
}