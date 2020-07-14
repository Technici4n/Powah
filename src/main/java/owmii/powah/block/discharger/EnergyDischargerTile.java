package owmii.powah.block.discharger;

import owmii.lib.block.AbstractEnergyStorage;
import owmii.powah.block.ITiles;
import owmii.powah.block.Tier;
import owmii.powah.config.EnergyDischargerConfig;

public class EnergyDischargerTile extends AbstractEnergyStorage<Tier, EnergyDischargerConfig, EnergyDischargerBlock> {
    public EnergyDischargerTile(Tier variant) {
        super(ITiles.ENERGY_DISCHARGER, variant);
    }

    public EnergyDischargerTile() {
        this(Tier.STARTER);
    }

    @Override
    public boolean keepEnergy() {
        return true;
    }
}
