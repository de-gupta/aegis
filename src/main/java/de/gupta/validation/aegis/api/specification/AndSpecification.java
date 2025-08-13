package de.gupta.validation.aegis.api.specification;

public record AndSpecification<T, V extends Specification<T>>(V firstSpecification, V secondSpecification)
		implements Specification<T>
{
	public static <T, V extends Specification<T>> AndSpecification<T, V> from(final V firstSpecification,
																			  final V secondSpecification)
	{
		return new AndSpecification<>(firstSpecification, secondSpecification);
	}

	@Override
	public boolean isSatisfiedBy(final T t)
	{
		return firstSpecification.isSatisfiedBy(t) && secondSpecification.isSatisfiedBy(t);
	}
}