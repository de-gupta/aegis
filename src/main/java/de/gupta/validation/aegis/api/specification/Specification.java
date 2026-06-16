package de.gupta.validation.aegis.api.specification;

import de.gupta.commons.utility.math.algebra.element.lattice.BooleanAlgebra;

@FunctionalInterface
public interface Specification<T> extends BooleanAlgebra<Specification<T>>
{
	@Override
	default Specification<T> meet(Specification<T> other)
	{
		return t -> this.isSatisfiedBy(t) && other.isSatisfiedBy(t);
	}

	boolean isSatisfiedBy(T t);

	@Override
	default Specification<T> join(Specification<T> other)
	{
		return t -> this.isSatisfiedBy(t) || other.isSatisfiedBy(t);
	}

	@Override
	default Specification<T> complement()
	{
		return t -> !this.isSatisfiedBy(t);
	}

	@Override
	default Specification<T> top()
	{
		return _ -> true;
	}

	@Override
	default Specification<T> bottom()
	{
		return _ -> false;
	}
}