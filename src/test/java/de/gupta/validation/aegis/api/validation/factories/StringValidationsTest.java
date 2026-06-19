package de.gupta.validation.aegis.api.validation.factories;

import de.gupta.validation.aegis.api.validation.validation.factories.StringValidations;
import de.gupta.validation.aegis.api.violation.Severity;
import de.gupta.validation.aegis.api.violation.Violation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("StringValidations")
final class StringValidationsTest
{
	private static final TestViolation VIOLATION = new TestViolation("must be trimmed", Severity.MEDIUM);

	private record Sample(String value)
	{
	}

	private record TestViolation(String message, Severity severity) implements Violation
	{
	}

	@Nested
	@DisplayName("for trimmedStringSpecification()")
	final class ForTrimmedStringSpecification
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("validatesWhetherTheExtractedStringIsTrimmedCases")
		@DisplayName("validates whether the extracted string is trimmed")
		void validatesWhetherTheExtractedStringIsTrimmed(final String as, final Sample sample,
		                                                 final Set<Violation> expectedViolations)
		{
			var validation = StringValidations.trimmedStringSpecification(Sample::value, () -> VIOLATION);

			var result = validation.validate(sample);

			assertThat(result.violations())
					.as("violations for %s", as)
					.isEqualTo(expectedViolations);
		}

		private static Stream<Arguments> validatesWhetherTheExtractedStringIsTrimmedCases()
		{
			return Stream.of(
					Arguments.of("trimmed strings are accepted", new Sample("trimmed"), Set.of()),
					Arguments.of("untrimmed strings produce the supplied violation", new Sample(" trimmed "),
							Set.of(VIOLATION))
			);
		}
	}
}