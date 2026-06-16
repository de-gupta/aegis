package de.gupta.validation.aegis.api.violation;

import de.gupta.commons.utility.math.algebra.element.lattice.JoinSemilattice;
import de.gupta.commons.utility.math.algebra.element.lattice.MeetSemilattice;

public enum Severity implements JoinSemilattice<Severity>, MeetSemilattice<Severity>
{
	VERY_LOW(1),
	LOW(2),
	MEDIUM(3),
	HIGH(4),
	VERY_HIGH(5),
	CRITICAL(6);

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

	Severity(int level)
	{
		this.level = level;
	}
}