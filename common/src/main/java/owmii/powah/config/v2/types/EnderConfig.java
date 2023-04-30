package owmii.powah.config.v2.types;

import owmii.powah.block.Tier;
import owmii.powah.config.IEnergyConfig;
import owmii.powah.config.v2.values.TieredChannelValues;
import owmii.powah.config.v2.values.TieredEnergyValues;

public class EnderConfig implements IEnergyConfig<Tier> {
    public TieredEnergyValues transfer_rates;
    public TieredChannelValues channels;

    private EnderConfig() {
    }

    public EnderConfig(TieredEnergyValues transfer_rates, TieredChannelValues channels) {
        this.transfer_rates = transfer_rates;
        this.channels = channels;
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
