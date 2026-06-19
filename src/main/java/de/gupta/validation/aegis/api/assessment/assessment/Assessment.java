package de.gupta.validation.aegis.api.assessment.assessment;

import de.gupta.commons.utility.math.algebra.element.binary.notation.additive.AdditiveSemigroup;
import de.gupta.validation.aegis.api.assessment.assessment.result.AssessmentResult;

@FunctionalInterface
public interface Assessment<T> extends AdditiveSemigroup<Assessment<T>>
{
	@Override
	default Assessment<T> add(final Assessment<T> other)
	{
		return t -> assess(t).add(other.assess(t));
	}

	AssessmentResult assess(T t);
}
