package owmii.lib.block;

import owmii.lib.logistics.fluid.Tank;

public interface ITankHolder {
    Tank getTank();

    boolean keepFluid();
}
