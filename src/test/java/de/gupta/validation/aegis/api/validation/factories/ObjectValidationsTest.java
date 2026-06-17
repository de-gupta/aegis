package de.gupta.validation.aegis.api.validation.factories;

import de.gupta.validation.aegis.api.violation.Severity;
import de.gupta.validation.aegis.api.violation.Violation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ObjectValidations")
final class ObjectValidationsTest
{
	private static final TestViolation VIOLATION = new TestViolation("must not be null", Severity.HIGH);

	private record Sample(String value)
	{
	}

	private record TestViolation(String message, Severity severity) implements Violation
	{
	}

	@Nested
	@DisplayName("for notNullSpecification()")
	final class ForNotNullSpecification
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("acceptsPresentExtractedValuesCases")
		@DisplayName("validates whether the extracted value is present")
		void validatesWhetherTheExtractedValueIsPresent(final String as, final Sample sample)
		{
			var validation = ObjectValidations.notNullSpecification(Sample::value, () -> VIOLATION);

			var result = validation.validate(sample);

			assertThat(result.violations())
					.as("violations for %s", as)
					.isEmpty();
		}

		@Test
		@DisplayName("propagates the empty unfolding error when the extracted value is null")
		void propagatesTheEmptyUnfoldingErrorWhenTheExtractedValueIsNull()
		{
			var validation = ObjectValidations.notNullSpecification(Sample::value, () -> VIOLATION);

			assertThatThrownBy(() -> validation.validate(new Sample(null)))
					.as("validation result for null extracted value")
					.isInstanceOf(RuntimeException.class)
					.hasMessage("An empty vessel cannot pour forth wisdom");
		}

		private static Stream<Arguments> acceptsPresentExtractedValuesCases()
		{
			return Stream.of(Arguments.of("present values are accepted", new Sample("value")));
		}
	}
}
