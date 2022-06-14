package owmii.powah.config.v2.values;

import owmii.powah.block.Tier;
import owmii.powah.config.v2.annotations.LongRange;

public class TieredChannelValues {
	@LongRange(min = 1, max = 12)
	public int starter;
	@LongRange(min = 1, max = 12)
	public int basic;
	@LongRange(min = 1, max = 12)
	public int hardened;
	@LongRange(min = 1, max = 12)
	public int blazing;
	@LongRange(min = 1, max = 12)
	public int niotic;
	@LongRange(min = 1, max = 12)
	public int spirited;
	@LongRange(min = 1, max = 12)
	public int nitro;

	public static TieredChannelValues getDefault() {
		return new TieredChannelValues(1, 2, 3, 5, 7, 9, 12);
	}

	public TieredChannelValues(int starter, int basic, int hardened, int blazing, int niotic, int spirited, int nitro) {
		this.starter = starter;
		this.basic = basic;
		this.hardened = hardened;
		this.blazing = blazing;
		this.niotic = niotic;
		this.spirited = spirited;
		this.nitro = nitro;
	}

	public int get(Tier tier) {
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
