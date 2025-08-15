package de.gupta.validation.aegis.api.specification.string;

import de.gupta.validation.aegis.api.specification.Specification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

final class StringSpecificationFactoryTest
{
	private record StringTestCase(String description, String value, boolean expectedResult)
	{
		static StringTestCase of(final String description, final String value, final boolean expectedResult)
		{
			return new StringTestCase(description, value, expectedResult);
		}
	}

	private record FactoryMethodTestCase(String description, Specification<String> specification1,
										 Specification<String> specification2)
	{
		static FactoryMethodTestCase of(final String description, final Specification<String> specification1,
										final Specification<String> specification2)
		{
			return new FactoryMethodTestCase(description, specification1, specification2);
		}
	}

	@Nested
	@DisplayName("notBlank() specification tests")
	final class NotBlankTests
	{
		@DisplayName("should validate non-blank strings correctly")
		@ParameterizedTest(name = "{0}")
		@MethodSource("notBlankTestCases")
		void shouldValidateNotBlankStringsCorrectly(final String description, final String value,
													final boolean expectedResult)
		{
			final Specification<String> specification = StringSpecificationFactory.notBlank();

			assertThat(specification.isSatisfiedBy(value))
					.as("notBlank specification %s should return %s for value '%s'", description, expectedResult, value)
					.isEqualTo(expectedResult);
		}

		private static Stream<Arguments> notBlankTestCases()
		{
			return Stream.of(
					StringTestCase.of("with non-empty string", "hello", true),
					StringTestCase.of("with string containing only text", "world", true),
					StringTestCase.of("with string containing spaces and text", " hello world ", true),
					StringTestCase.of("with string containing numbers", "123", true),
					StringTestCase.of("with string containing special characters", "!@#$%", true),
					StringTestCase.of("with string containing mixed content", "hello123!@#", true),
					StringTestCase.of("with single character", "a", true),
					StringTestCase.of("with single space", " ", false),
					StringTestCase.of("with multiple spaces", "   ", false),
					StringTestCase.of("with tabs only", "\t\t", false),
					StringTestCase.of("with newlines only", "\n\n", false),
					StringTestCase.of("with mixed whitespace", " \t\n ", false),
					StringTestCase.of("with empty string", "", false),
					StringTestCase.of("with null value", null, false)
			).map(tc -> Arguments.of(tc.description, tc.value, tc.expectedResult));
		}
	}

	@Nested
	@DisplayName("trimmed() specification tests")
	final class TrimmedTests
	{
		@DisplayName("should validate trimmed strings correctly")
		@ParameterizedTest(name = "{0}")
		@MethodSource("trimmedTestCases")
		void shouldValidateTrimmedStringsCorrectly(final String description, final String value,
												   final boolean expectedResult)
		{
			final Specification<String> specification = StringSpecificationFactory.trimmed();

			assertThat(specification.isSatisfiedBy(value))
					.as("trimmed specification %s should return %s for value '%s'", description, expectedResult, value)
					.isEqualTo(expectedResult);
		}

		private static Stream<Arguments> trimmedTestCases()
		{
			return Stream.of(
					StringTestCase.of("with already trimmed string", "hello", true),
					StringTestCase.of("with single word", "world", true),
					StringTestCase.of("with multiple words no leading/trailing spaces", "hello world", true),
					StringTestCase.of("with numbers", "123", true),
					StringTestCase.of("with special characters", "!@#$%", true),
					StringTestCase.of("with empty string", "", true),
					StringTestCase.of("with single character", "a", true),
					StringTestCase.of("with string having leading space", " hello", false),
					StringTestCase.of("with string having trailing space", "hello ", false),
					StringTestCase.of("with string having both leading and trailing spaces", " hello ", false),
					StringTestCase.of("with string having leading tab", "\thello", false),
					StringTestCase.of("with string having trailing tab", "hello\t", false),
					StringTestCase.of("with string having leading newline", "\nhello", false),
					StringTestCase.of("with string having trailing newline", "hello\n", false),
					StringTestCase.of("with string having mixed leading whitespace", " \t\nhello", false),
					StringTestCase.of("with string having mixed trailing whitespace", "hello \t\n", false),
					StringTestCase.of("with string having both mixed whitespace", " \thello\n ", false),
					StringTestCase.of("with only spaces", "   ", false),
					StringTestCase.of("with only tabs", "\t\t", false),
					StringTestCase.of("with only newlines", "\n\n", false),
					StringTestCase.of("with mixed whitespace only", " \t\n ", false),
					StringTestCase.of("with null value", null, false)
			).map(tc -> Arguments.of(tc.description, tc.value, tc.expectedResult));
		}
	}

	@Nested
	@DisplayName("Factory method tests")
	final class FactoryMethodTests
	{
		@DisplayName("should create different specification instances")
		@ParameterizedTest(name = "{0}")
		@MethodSource("factoryMethodTestCases")
		void shouldCreateDifferentSpecificationInstances(final String description,
														 final Specification<String> specification1,
														 final Specification<String> specification2)
		{
			assertThat(specification1)
					.as("First specification instance %s should not be null", description)
					.isNotNull();

			assertThat(specification2)
					.as("Second specification instance %s should not be null", description)
					.isNotNull();

			assertThat(specification1)
					.as("Different calls to factory methods %s should create different instances", description)
					.isNotSameAs(specification2);
		}

		private static Stream<Arguments> factoryMethodTestCases()
		{
			return Stream.of(
					FactoryMethodTestCase.of("for notBlank()",
							StringSpecificationFactory.notBlank(),
							StringSpecificationFactory.notBlank()),
					FactoryMethodTestCase.of("for trimmed()",
							StringSpecificationFactory.trimmed(),
							StringSpecificationFactory.trimmed()),
					FactoryMethodTestCase.of("between different methods",
							StringSpecificationFactory.notBlank(),
							StringSpecificationFactory.trimmed())
			).map(tc -> Arguments.of(tc.description, tc.specification1, tc.specification2));
		}
	}
}