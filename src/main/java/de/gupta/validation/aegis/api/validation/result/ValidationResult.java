package de.gupta.validation.aegis.api.validation.result;

import de.gupta.commons.utility.math.algebra.element.lattice.BooleanAlgebra;
import de.gupta.validation.aegis.api.violation.Violation;

import java.util.Collection;

public interface ValidationResult extends BooleanAlgebra<ValidationResult>
{
	boolean isValid();

	Collection<Violation> blockingViolations();

	Collection<Violation> toleratedViolations();

	@Override
	ValidationResult complement();

	@Override
	ValidationResult supremum();

	@Override
	ValidationResult infimum();

	@Override
	ValidationResult join(ValidationResult validationResult);

	@Override
	ValidationResult meet(ValidationResult validationResult);
}