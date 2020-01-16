package owmii.powah.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.WorldSavedData;

public class WorldData extends WorldSavedData {
    public WorldData() {
        super("powah_generic_data");
    }

    @Override
    public void read(CompoundNBT nbt) {
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        return compound;
    }
}
