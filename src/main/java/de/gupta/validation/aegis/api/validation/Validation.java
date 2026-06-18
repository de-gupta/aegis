package de.gupta.validation.aegis.api.validation;

import de.gupta.commons.utility.math.algebra.element.binary.notation.additive.AdditiveSemigroup;
import de.gupta.validation.aegis.api.validation.result.ValidationResult;

@FunctionalInterface
public interface Validation<T> extends AdditiveSemigroup<Validation<T>>
{
	@Override
	default Validation<T> add(Validation<T> other)
	{
		return t -> validate(t).add(other.validate(t));
	}

	ValidationResult validate(T t);
}