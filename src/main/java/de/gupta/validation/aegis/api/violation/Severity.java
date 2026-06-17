package de.gupta.validation.aegis.api.violation;

import de.gupta.commons.utility.math.algebra.element.lattice.DistributiveLattice;

import java.util.Arrays;
import java.util.Comparator;

public enum Severity implements DistributiveLattice<Severity>
{
	CONSIDERATION(1),
	VERY_LOW(2),
	LOW(3),
	MEDIUM(4),
	HIGH(5),
	VERY_HIGH(6),
	CRITICAL(7);

	private final int level;

	@Override
	public Severity join(final Severity other)
	{
		return level() < other.level() ? this : other;
	}

	public int level()
	{
		return level;
	}

	@Override
	public Severity meet(final Severity other)
	{
		return level() > other.level() ? this : other;
	}

	@Override
	public Severity supremum()
	{
		return Arrays.stream(values())
		             .max(Comparator.comparing(Severity::level))
		             .orElseThrow();
	}

	@Override
	public Severity infimum()
	{
		return Arrays.stream(values())
		             .min(Comparator.comparing(Severity::level))
		             .orElseThrow();
	}

	Severity(int level)
	{
		this.level = level;
	}
}