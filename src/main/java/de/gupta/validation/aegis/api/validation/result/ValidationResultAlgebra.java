package de.gupta.validation.aegis.api.validation.result;

import de.gupta.commons.utility.math.algebra.structure.binary.notation.additive.AdditiveMonoidStructure;
import de.gupta.validation.aegis.api.violation.Violation;

import java.util.Set;

public enum ValidationResultAlgebra implements AdditiveMonoidStructure<ValidationResult>
{
	EMPTY_SET_BASED(Set.of());

	private final Set<Violation> zero;

	@Override
	public ValidationResult add(final ValidationResult left, final ValidationResult right)
	{
		return left.add(right);
	}

	@Override
	public ValidationResult zero()
	{
		return () -> zero;
	}

	ValidationResultAlgebra(final Set<Violation> zero)
	{
		this.zero = zero;
	}
}