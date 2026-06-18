package de.gupta.validation.aegis.api.violation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SeverityLattice")
final class SeverityLatticeTest
{
	private final SeverityLattice severityLattice = SeverityLattice.LEVEL_BASED;

	@Nested
	@DisplayName("as a bounded lattice")
	final class AsABoundedLattice
	{
		@Test
		@DisplayName("uses consideration as the infimum")
		void usesConsiderationAsTheInfimum()
		{
			assertThat(severityLattice.infimum()).as("infimum").isEqualTo(Severity.CONSIDERATION);
		}

		@Test
		@DisplayName("uses critical as the supremum")
		void usesCriticalAsTheSupremum()
		{
			assertThat(severityLattice.supremum())
					.as("supremum").isEqualTo(Severity.CRITICAL);
		}
	}

	@Nested
	@DisplayName("for join")
	final class ForJoin
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("returnsTheLowerSeverityCases")
		@DisplayName("returns the lower severity")
		void returnsTheLowerSeverity(final String as, final JoinCase tc)
		{
			var result = severityLattice.join(tc.left(), tc.right());

			assertThat(result).as("%s", as).isEqualTo(tc.expected());
		}

		private static Stream<Arguments> returnsTheLowerSeverityCases()
		{
			return Stream.of(
					JoinCase.of("consideration joined with critical yields consideration",
							Severity.CONSIDERATION, Severity.CRITICAL, Severity.CONSIDERATION),
					JoinCase.of("critical joined with consideration yields consideration",
							Severity.CRITICAL, Severity.CONSIDERATION, Severity.CONSIDERATION),
					JoinCase.of("medium joined with high yields medium",
							Severity.MEDIUM, Severity.HIGH, Severity.MEDIUM),
					JoinCase.of("very high joined with low yields low",
							Severity.VERY_HIGH, Severity.LOW, Severity.LOW),
					JoinCase.of("equal severities joined together keep that severity",
							Severity.LOW, Severity.LOW, Severity.LOW)
			).map(tc -> Arguments.of(tc.as(), tc));
		}

		private record JoinCase(String as, Severity left, Severity right, Severity expected)
		{
			private static JoinCase of(final String as, final Severity left, final Severity right,
			                           final Severity expected)
			{
				return new JoinCase(as, left, right, expected);
			}
		}
	}

	@Nested
	@DisplayName("for meet")
	final class ForMeet
	{
		@ParameterizedTest(name = "{0}")
		@MethodSource("returnsTheHigherSeverityCases")
		@DisplayName("returns the higher severity")
		void returnsTheHigherSeverity(final String as, final MeetCase tc)
		{
			var result = severityLattice.meet(tc.left(), tc.right());

			assertThat(result).as("%s", as).isEqualTo(tc.expected());
		}

		private static Stream<Arguments> returnsTheHigherSeverityCases()
		{
			return Stream.of(
					MeetCase.of("consideration met with critical yields critical",
							Severity.CONSIDERATION, Severity.CRITICAL, Severity.CRITICAL),
					MeetCase.of("critical met with consideration yields critical",
							Severity.CRITICAL, Severity.CONSIDERATION, Severity.CRITICAL),
					MeetCase.of("medium met with high yields high",
							Severity.MEDIUM, Severity.HIGH, Severity.HIGH),
					MeetCase.of("very high met with low yields very high",
							Severity.VERY_HIGH, Severity.LOW, Severity.VERY_HIGH),
					MeetCase.of("equal severities met together keep that severity",
							Severity.LOW, Severity.LOW, Severity.LOW)
			).map(tc -> Arguments.of(tc.as(), tc));
		}

		private record MeetCase(String as, Severity left, Severity right, Severity expected)
		{
			private static MeetCase of(final String as, final Severity left, final Severity right,
			                           final Severity expected)
			{
				return new MeetCase(as, left, right, expected);
			}
		}
	}
}