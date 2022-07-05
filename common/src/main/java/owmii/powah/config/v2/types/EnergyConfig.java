package owmii.powah.config.v2.types;

import owmii.powah.block.Tier;
import owmii.powah.config.IEnergyConfig;
import owmii.powah.config.v2.values.TieredEnergyValues;

public class EnergyConfig implements IEnergyConfig<Tier> {
	public TieredEnergyValues capacities;
	public TieredEnergyValues transfer_rates;

	private EnergyConfig() {
	}

	public EnergyConfig(TieredEnergyValues capacities, TieredEnergyValues transfer_rates) {
		this.capacities = capacities;
		this.transfer_rates = transfer_rates;
	}

	@Override
	public long getCapacity(Tier variant) {
		return capacities.get(variant);
	}

	@Override
	public long getTransfer(Tier variant) {
		return transfer_rates.get(variant);
	}
}
