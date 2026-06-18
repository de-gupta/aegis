package de.gupta.validation.aegis.api.violation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ViolationFactory")
final class ViolationFactoryTest
{
	@Nested
	@DisplayName("for severity-specific factories")
	final class ForSeveritySpecificFactories
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("createsViolationsWithTheExpectedSeverityCases")
		@DisplayName("creates violations with the expected severity")
		void createsViolationsWithTheExpectedSeverity(final String as, final Function<String, Violation> invocation,
		                                              final Severity expectedSeverity)
		{
			var violation = invocation.apply("problem");

			assertThat(violation)
					.as("%s", as)
					.isInstanceOf(ViolationImpl.class);
			assertThat(violation.message())
					.as("message for %s", as)
					.isEqualTo("problem");
			assertThat(violation.severity())
					.as("severity for %s", as)
					.isEqualTo(expectedSeverity);
		}

		private static Stream<Arguments> createsViolationsWithTheExpectedSeverityCases()
		{
			return Stream.of(
					Arguments.of("critical uses CRITICAL severity",
							(Function<String, Violation>) ViolationFactory::critical,
							Severity.CRITICAL),
					Arguments.of("high uses HIGH severity", (Function<String, Violation>) ViolationFactory::high,
							Severity.HIGH),
					Arguments.of("low uses LOW severity", (Function<String, Violation>) ViolationFactory::low,
							Severity.LOW),
					Arguments.of("consideration uses CONSIDERATION severity",
							(Function<String, Violation>) ViolationFactory::consideration, Severity.CONSIDERATION)
			);
		}
	}

	@Nested
	@DisplayName("for with()")
	final class ForWith
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("createsViolationsWithTheProvidedSeverityCases")
		@DisplayName("creates violations with the provided severity")
		void createsViolationsWithTheProvidedSeverity(final String as, final Severity severity)
		{
			var violation = ViolationFactory.with("problem", severity);

			assertThat(violation.message())
					.as("message for %s", as)
					.isEqualTo("problem");
			assertThat(violation.severity())
					.as("severity for %s", as)
					.isEqualTo(severity);
		}

		private static Stream<Arguments> createsViolationsWithTheProvidedSeverityCases()
		{
			return Stream.of(
					Arguments.of("with preserves LOW", Severity.LOW),
					Arguments.of("with preserves MEDIUM", Severity.MEDIUM)
			);
		}
	}
}