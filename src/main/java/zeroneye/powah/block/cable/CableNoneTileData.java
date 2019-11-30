package zeroneye.powah.block.cable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.WorldSavedData;
import zeroneye.lib.util.Location;

import java.util.HashMap;
import java.util.Map;

public class CableNoneTileData extends WorldSavedData {
    private final Map<Location, CompoundNBT> map = new HashMap<>();

    public CableNoneTileData(String name) {
        super(name);
    }

    @Override
    public void read(CompoundNBT nbt) {

    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        return compound;
    }
}
