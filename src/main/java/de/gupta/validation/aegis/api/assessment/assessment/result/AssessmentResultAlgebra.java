package de.gupta.validation.aegis.api.assessment.assessment.result;

import de.gupta.commons.utility.math.algebra.structure.binary.notation.additive.AdditiveMonoidStructure;
import de.gupta.validation.aegis.api.violation.Violation;

import java.util.Set;

public enum AssessmentResultAlgebra implements AdditiveMonoidStructure<AssessmentResult>
{
	EMPTY_SET_BASED(Set.of());

	private final Set<Violation> zero;

	@Override
	public AssessmentResult add(final AssessmentResult left, final AssessmentResult right)
	{
		return left.add(right);
	}

	@Override
	public AssessmentResult zero()
	{
		return () -> zero;
	}

	AssessmentResultAlgebra(final Set<Violation> zero)
	{
		this.zero = zero;
	}
}
