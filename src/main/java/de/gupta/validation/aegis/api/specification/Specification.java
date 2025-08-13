package de.gupta.validation.aegis.api.specification;

@FunctionalInterface
public interface Specification<T>
{
	default Specification<T> and(Specification<T> other)
	{
		return AndSpecification.from(this, other);
	}

	default Specification<T> or(Specification<T> other)
	{
		return OrSpecification.from(this, other);
	}

	default Specification<T> xor(Specification<T> other)
	{
		return XorSpecification.from(this, other);
	}

	default Specification<T> not()
	{
		Specification<T> original = this;
		return t -> !original.isSatisfiedBy(t);
	}

	boolean isSatisfiedBy(T t);
}