package de.gupta.validation.aegis.api.validation;

import de.gupta.validation.aegis.api.validation.result.ValidationResult;

@FunctionalInterface
public interface Validation<T>
//		extends BooleanAlgebra<Validation<T>>
{
	default Validation<T> and(Validation<T> other)
	{
		return t -> validate(t).and(other.validate(t));
	}

	//	@Override
//	default Validation<T> complement()
//	{
//		return t -> validate(t).complement();
//	}
//
//	@Override
//	Validation<T> top();
//
//	@Override
//	Validation<T> bottom();
//
//	@Override
//	Validation<T> join(Validation<T> tValidation);
//
//	@Override
//	Validation<T> meet(Validation<T> tValidation);
//
	ValidationResult validate(T t);
}