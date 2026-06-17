package de.gupta.validation.aegis.api.validation.result;

import de.gupta.aletheia.collection.cascade.Cascade;
import de.gupta.commons.utility.collection.SetUtility;
import de.gupta.commons.utility.math.algebra.element.binary.notation.additive.AdditiveSemigroup;
import de.gupta.validation.aegis.api.violation.Severity;
import de.gupta.validation.aegis.api.violation.Violation;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@FunctionalInterface
public interface ValidationResult extends AdditiveSemigroup<ValidationResult>
{
	default boolean isValid(final Severity threshold)
	{
		return highestSeverity()
				.map(severity -> severity.level() < threshold.level())
				.orElse(true);
	}

	default Optional<Severity> highestSeverity()
	{
		return Cascade.beckon(violations())
		              .metamorphose(Violation::severity)
		              .zenith(Comparator.comparing(Severity::level))
		              .optional();
	}

	Set<Violation> violations();

	@Override
	default ValidationResult add(ValidationResult other)
	{
		return () -> SetUtility.unionOf(List.of(violations(), other.violations()));
	}
}