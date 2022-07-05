package owmii.powah.config.v2.values;

import owmii.powah.block.Tier;
import owmii.powah.config.v2.annotations.LongRange;
import owmii.powah.lib.logistics.energy.Energy;

public class TieredEnergyValues {
	@LongRange(min = 1, max = Energy.MAX)
	public long starter;
	@LongRange(min = 1, max = Energy.MAX)
	public long basic;
	@LongRange(min = 1, max = Energy.MAX)
	public long hardened;
	@LongRange(min = 1, max = Energy.MAX)
	public long blazing;
	@LongRange(min = 1, max = Energy.MAX)
	public long niotic;
	@LongRange(min = 1, max = Energy.MAX)
	public long spirited;
	@LongRange(min = 1, max = Energy.MAX)
	public long nitro;

	private TieredEnergyValues() {
	}

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

	public TieredEnergyValues copy(long factor) {
		return new TieredEnergyValues(starter * factor, basic * factor, hardened * factor, blazing * factor, niotic * factor, spirited * factor, nitro * factor);
	}
}
