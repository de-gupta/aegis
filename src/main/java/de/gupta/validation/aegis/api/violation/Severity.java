package de.gupta.validation.aegis.api.violation;

import de.gupta.aletheia.functional.Unfolding;
import de.gupta.commons.utility.exception.ExceptionHelper;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Severity
{
	CONSIDERATION(1),
	VERY_LOW(2),
	LOW(3),
	MEDIUM(4),
	HIGH(5),
	VERY_HIGH(6),
	CRITICAL(7);

	private final int level;

	public static Severity fromLevel(final int level)
	{
		return Unfolding.beckon(level)
		                .discern(l -> Arrays.stream(Severity.values()).map(Severity::level)
		                                    .collect(Collectors.toUnmodifiableSet()).contains(l),
								ExceptionHelper.iaeFrom("The given level is not valid"))
		                .coronate(l -> Severity.values()[l - 1]);
	}

	public int level()
	{
		return level;
	}

	Severity(int level)
	{
		this.level = level;
	}
}