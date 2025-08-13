package de.gupta.validation.aegis.api.specification.validation.custom;

import de.gupta.validation.aegis.api.exception.ValidationFailedException;
import de.gupta.validation.aegis.api.specification.validation.AbstractValidationSpecification;
import de.gupta.validation.aegis.api.specification.validation.ValidationSpecification;

import java.util.function.Function;
import java.util.function.Supplier;

public final class CustomCheckSpecification<T, V extends ValidationFailedException>
		extends AbstractValidationSpecification<T, V>
		implements ValidationSpecification<T>
{
	private final Function<T, Boolean> customCheck;

	public static <T, V extends ValidationFailedException> CustomCheckSpecification<T, V> of(
			final Function<T, Boolean> customCheck,
			final Supplier<V> exceptionSupplier)
	{
		return new CustomCheckSpecification<>(customCheck, exceptionSupplier);
	}

	@Override
	public boolean isSatisfiedBy(final T t)
	{
		return customCheck.apply(t);
	}

	private CustomCheckSpecification(final Function<T, Boolean> customCheck, final Supplier<V> exceptionSupplier)
	{
		super(exceptionSupplier);
		this.customCheck = customCheck;
	}

}