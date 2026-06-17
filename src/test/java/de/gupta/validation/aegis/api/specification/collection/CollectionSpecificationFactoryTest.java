package de.gupta.validation.aegis.api.specification.collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CollectionSpecificationFactory")
final class CollectionSpecificationFactoryTest
{
	@Nested
	@DisplayName("for nonEmpty()")
	final class ForNonEmpty
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("acceptsCollectionsWithAtLeastOneElementCases")
		@DisplayName("accepts collections with at least one element")
		void acceptsCollectionsWithAtLeastOneElement(final String as, final List<String> value,
		                                             final boolean expected)
		{
			var specification = CollectionSpecificationFactory.<String>nonEmpty();

			assertThat(specification.isSatisfiedBy(value))
					.as("%s", as)
					.isEqualTo(expected);
		}

		private static Stream<Arguments> acceptsCollectionsWithAtLeastOneElementCases()
		{
			return Stream.of(
					Arguments.of("empty collections are rejected", List.of(), false),
					Arguments.of("non-empty collections are accepted", List.of("alpha"), true)
			);
		}
	}

	@Nested
	@DisplayName("for membership factories")
	final class ForMembershipFactories
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("evaluatesMembershipCases")
		@DisplayName("evaluates membership against the supplied collection")
		void evaluatesMembershipAgainstTheSuppliedCollection(final String as, final String candidate,
		                                                     final boolean expectedInCollection,
		                                                     final boolean expectedNotInCollection)
		{
			var allowedValues = List.of("alpha", "beta");

			assertThat(CollectionSpecificationFactory.inCollection(allowedValues).isSatisfiedBy(candidate))
					.as("inCollection for %s", as)
					.isEqualTo(expectedInCollection);
			assertThat(CollectionSpecificationFactory.notInCollection(allowedValues).isSatisfiedBy(candidate))
					.as("notInCollection for %s", as)
					.isEqualTo(expectedNotInCollection);
		}

		private static Stream<Arguments> evaluatesMembershipCases()
		{
			return Stream.of(
					Arguments.of("listed value", "alpha", true, false),
					Arguments.of("missing value", "gamma", false, true)
			);
		}
	}

	@Nested
	@DisplayName("for bulk membership factories")
	final class ForBulkMembershipFactories
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("evaluatesCollectionsAgainstTheReferenceCollectionCases")
		@DisplayName("evaluates collections against the reference collection")
		void evaluatesCollectionsAgainstTheReferenceCollection(final String as, final List<String> candidate,
		                                                       final boolean expectedEachInCollection,
		                                                       final boolean expectedAnyInCollection)
		{
			var allowedValues = List.of("alpha", "beta");

			assertThat(CollectionSpecificationFactory.eachInCollection(allowedValues).isSatisfiedBy(candidate))
					.as("eachInCollection for %s", as)
					.isEqualTo(expectedEachInCollection);
			assertThat(CollectionSpecificationFactory.anyInCollection(allowedValues).isSatisfiedBy(candidate))
					.as("anyInCollection for %s", as)
					.isEqualTo(expectedAnyInCollection);
		}

		private static Stream<Arguments> evaluatesCollectionsAgainstTheReferenceCollectionCases()
		{
			return Stream.of(
					Arguments.of("every candidate value listed", List.of("alpha"), true, true),
					Arguments.of("some candidate values listed", List.of("alpha", "gamma"), false, true),
					Arguments.of("no candidate values listed", List.of("gamma", "delta"), false, false)
			);
		}
	}
}
