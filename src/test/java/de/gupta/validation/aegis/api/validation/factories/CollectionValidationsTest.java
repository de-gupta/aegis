package de.gupta.validation.aegis.api.validation.factories;

import de.gupta.validation.aegis.api.validation.validation.factories.CollectionValidations;
import de.gupta.validation.aegis.api.violation.Severity;
import de.gupta.validation.aegis.api.violation.Violation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CollectionValidations")
final class CollectionValidationsTest
{
	private static final TestViolation VIOLATION = new TestViolation("must be allowed", Severity.MEDIUM);

	private record StaticCollectionSample(String value)
	{
	}

	private record DynamicCollectionSample(String value, List<String> allowedValues)
	{
	}

	private record TestViolation(String message, Severity severity) implements Violation
	{
	}

	@Nested
	@DisplayName("for inCollectionSpecification() with a constant collection")
	final class ForConstantCollection
	{
		@Test
		@DisplayName("uses the supplied collection as the membership source")
		void usesTheSuppliedCollectionAsTheMembershipSource()
		{
			var validation = CollectionValidations.inCollectionSpecification(
					StaticCollectionSample::value,
					List.of("alpha", "beta"),
					() -> VIOLATION);

			var acceptedResult = validation.validate(new StaticCollectionSample("alpha"));
			var rejectedResult = validation.validate(new StaticCollectionSample("gamma"));

			assertThat(acceptedResult.violations())
					.as("violations for accepted value")
					.isEmpty();
			assertThat(rejectedResult.violations())
					.as("violations for rejected value")
					.isEqualTo(Set.of(VIOLATION));
		}
	}

	@Nested
	@DisplayName("for inCollectionSpecification() with an extracted collection")
	final class ForExtractedCollection
	{
		@Test
		@DisplayName("uses the extracted collection as the membership source")
		void usesTheExtractedCollectionAsTheMembershipSource()
		{
			var validation = CollectionValidations.inCollectionSpecification(
					DynamicCollectionSample::value,
					DynamicCollectionSample::allowedValues,
					() -> VIOLATION);

			var acceptedResult = validation.validate(new DynamicCollectionSample("alpha", List.of("alpha", "beta")));
			var rejectedResult = validation.validate(new DynamicCollectionSample("gamma", List.of("alpha", "beta")));

			assertThat(acceptedResult.violations())
					.as("violations for accepted value")
					.isEmpty();
			assertThat(rejectedResult.violations())
					.as("violations for rejected value")
					.isEqualTo(Set.of(VIOLATION));
		}
	}
}