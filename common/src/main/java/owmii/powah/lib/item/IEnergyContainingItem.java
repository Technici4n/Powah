package owmii.powah.lib.item;

import javax.annotation.Nullable;

public interface IEnergyContainingItem {
    @Nullable
    Info getEnergyInfo();

    record Info(long capacity, long maxInsert, long maxExtract) {
    }
}
