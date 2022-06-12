package owmii.powah.config.v2.types;

import owmii.powah.block.Tier;
import owmii.powah.config.IEnergyConfig;
import owmii.powah.config.v2.values.TieredEnergyValues;

public class CableConfig implements IEnergyConfig<Tier> {
	public final TieredEnergyValues transfer_rates;

	public CableConfig(TieredEnergyValues transfer_rates) {
		this.transfer_rates = transfer_rates;
	}

	@Override
	public long getCapacity(Tier variant) {
		return 0;
	}

	@Override
	public long getTransfer(Tier variant) {
		return transfer_rates.get(variant);
	}
}
