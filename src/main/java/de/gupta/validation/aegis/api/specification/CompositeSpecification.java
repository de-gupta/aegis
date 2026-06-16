package de.gupta.validation.aegis.api.specification;

import java.util.function.Predicate;

@Deprecated
record CompositeSpecification<T>(Specification<T> left, Specification<T> right, CompositionType compositionType)
		implements Specification<T>
{
	static <T> CompositeSpecification<T> of(final Specification<T> left, final Specification<T> right,
	                                        final CompositionType compositionType)
	{
		return new CompositeSpecification<>(left, right, compositionType);
	}

	@Override
	public boolean isSatisfiedBy(final T t)
	{
		return compositionType.compose(t, left::isSatisfiedBy, right::isSatisfiedBy);
	}

	enum CompositionType
	{
		AND,
		OR,
		XOR;

		public <T> boolean compose(final T value, final Predicate<T> left, final Predicate<T> right)
		{
			return switch (this)
			{
				case AND -> left.test(value) && right.test(value);
				case OR -> left.test(value) || right.test(value);
				case XOR -> left.test(value) ^ right.test(value);
			};
		}
	}
}