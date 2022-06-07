package owmii.powah.lib.block;

import owmii.powah.lib.logistics.fluid.Tank;

public interface ITankHolder {
    Tank getTank();

    boolean keepFluid();
}
