package de.gupta.validation.aegis.api.specification;

public record XorSpecification<T>(Specification<T> left, Specification<T> right) implements Specification<T>
{
	public static <T> Specification<T> from(final Specification<T> left, final Specification<T> right)
	{
		return new XorSpecification<>(left, right);
	}

	@Override
	public boolean isSatisfiedBy(final T t)
	{
		return left.isSatisfiedBy(t) ^ right.isSatisfiedBy(t);
	}
}