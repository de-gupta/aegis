package de.gupta.validation.aegis.api.specification;

@FunctionalInterface
public interface Specification<T>
{
	default Specification<T> and(Specification<T> other)
	{
		return CompositeSpecification.of(this, other, CompositeSpecification.CompositionType.AND);
	}

	default Specification<T> or(Specification<T> other)
	{
		return CompositeSpecification.of(this, other, CompositeSpecification.CompositionType.OR);
	}

	default Specification<T> xor(Specification<T> other)
	{
		return CompositeSpecification.of(this, other, CompositeSpecification.CompositionType.XOR);
	}

	default Specification<T> not()
	{
		return t -> !this.isSatisfiedBy(t);
	}

	boolean isSatisfiedBy(T t);
}