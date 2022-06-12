package owmii.powah.config.v2.values;

import me.shedaniel.autoconfig.annotation.ConfigEntry;
import owmii.powah.block.Tier;
import owmii.powah.lib.logistics.energy.Energy;

public class TieredEnergyValues {
	@ConfigEntry.BoundedDiscrete(max = Energy.MAX)
	public long starter;
	@ConfigEntry.BoundedDiscrete(max = Energy.MAX)
	public long basic;
	@ConfigEntry.BoundedDiscrete(max = Energy.MAX)
	public long hardened;
	@ConfigEntry.BoundedDiscrete(max = Energy.MAX)
	public long blazing;
	@ConfigEntry.BoundedDiscrete(max = Energy.MAX)
	public long niotic;
	@ConfigEntry.BoundedDiscrete(max = Energy.MAX)
	public long spirited;
	@ConfigEntry.BoundedDiscrete(max = Energy.MAX)
	public long nitro;

	public TieredEnergyValues(long starter, long basic, long hardened, long blazing, long niotic, long spirited, long nitro) {
		this.starter = starter;
		this.basic = basic;
		this.hardened = hardened;
		this.blazing = blazing;
		this.niotic = niotic;
		this.spirited = spirited;
		this.nitro = nitro;
	}

	public long get(Tier tier) {
		return switch (tier) {
			case STARTER -> starter;
			case BASIC -> basic;
			case HARDENED -> hardened;
			case BLAZING -> blazing;
			case NIOTIC -> niotic;
			case SPIRITED -> spirited;
			case NITRO -> nitro;
			case CREATIVE -> 0;
		};
	}
}
