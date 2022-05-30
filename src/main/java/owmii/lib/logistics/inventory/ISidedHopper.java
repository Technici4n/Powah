package owmii.lib.logistics.inventory;

import owmii.lib.logistics.Transfer;

public interface ISidedHopper {
    Transfer getItemTransfer();

    SidedHopperConfig getHopperConfig();

    SidedHopper getSidedHopper();
}
