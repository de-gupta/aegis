package de.gupta.validation.aegis.api.violation;

import de.gupta.aletheia.collection.cascade.Cascade;
import de.gupta.commons.utility.math.algebra.structure.lattice.DistributiveLatticeStructure;

import java.util.function.ToIntFunction;

public enum SeverityLattice implements DistributiveLatticeStructure<Severity>
{
	LEVEL_BASED(Severity::level);

	private final ToIntFunction<Severity> extractor;

	@Override
	public Severity supremum()
	{
		return Cascade.beckon(Severity.values())
		              .smelt(this::meet)
		              .summon();
	}

	@Override
	public Severity meet(final Severity left, final Severity right)
	{
		return extractor.applyAsInt(left) > extractor.applyAsInt(right) ? left : right;
	}

	@Override
	public Severity infimum()
	{
		return Cascade.beckon(Severity.values())
		              .smelt(this::join)
		              .summon();
	}

	@Override
	public Severity join(final Severity left, final Severity right)
	{
		return extractor.applyAsInt(left) < extractor.applyAsInt(right) ? left : right;
	}

	SeverityLattice(final ToIntFunction<Severity> extractor)
	{
		this.extractor = extractor;
	}
}