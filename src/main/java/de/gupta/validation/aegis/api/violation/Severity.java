package de.gupta.validation.aegis.api.violation;

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

	public int level()
	{
		return level;
	}

	Severity(int level)
	{
		this.level = level;
	}
}