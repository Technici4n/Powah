package owmii.powah.block;

import net.minecraft.tileentity.TileEntityType;
import owmii.lib.block.TileBase;

public abstract class PowahTile extends TileBase.Tickable {
    public PowahTile(TileEntityType<?> type) {
        super(type);
    }
}
