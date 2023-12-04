package owmii.powah.lib.item;

import org.jetbrains.annotations.Nullable;

public interface IEnergyContainingItem {
    @Nullable
    Info getEnergyInfo();

    record Info(long capacity, long maxInsert, long maxExtract) {
    }
}
