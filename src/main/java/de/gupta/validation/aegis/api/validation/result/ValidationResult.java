package de.gupta.validation.aegis.api.validation.result;

import de.gupta.aletheia.collection.cascade.Cascade;
import de.gupta.commons.utility.collection.SetUtility;
import de.gupta.commons.utility.comparison.ComparisonType;
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
	default boolean isLessThan(final Severity threshold)
	{
		return isValid(threshold, ComparisonType.LESS_THAN);
	}

	default boolean isValid(final Severity threshold, final ComparisonType comparisonType)
	{
		return highestSeverity()
				.map(severity -> comparisonType.compare(severity, threshold, Comparator.comparing(Severity::level)))
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

	default ValidationResult and(final ValidationResult other)
	{
		return add(other);
	}

	@Override
	default ValidationResult add(ValidationResult other)
	{
		return () -> SetUtility.unionOf(List.of(violations(), other.violations()));
	}
}