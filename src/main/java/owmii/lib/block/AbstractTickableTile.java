package owmii.lib.block;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;
import owmii.lib.registry.IVariant;
import owmii.lib.util.Server;

public class AbstractTickableTile<V extends IVariant, B extends AbstractBlock<V, B>> extends AbstractTileEntity<V, B> implements ITickableTileEntity {
    private int syncTicks;
    public int ticks;

    public AbstractTickableTile(TileEntityType<?> type) {
        super(type);
    }

    public AbstractTickableTile(TileEntityType<?> type, V variant) {
        super(type, variant);
    }

    @Override
    public void tick() {
        final World world = this.world;
        if (world != null) {
            if (this.ticks == 0) {
                onFirstTick(world);
            }
            if (doPostTicks(world)) {
                int i = postTick(world);
                if (i > -1 && !isRemote()) {
                    sync(i);
                }
            }
            this.ticks++;
            if (!isRemote()) {
                if (this.syncTicks > -1)
                    this.syncTicks--;
                if (this.syncTicks == 0)
                    sync();
            } else {
                clientTick(world);
            }
        }
    }

    protected void onFirstTick(World world) {
    }

    protected boolean doPostTicks(World world) {
        return true;
    }

    protected int postTick(World world) {
        return -1;
    }

    protected void clientTick(World world) {
    }

    public void sync(int delay) {
        if (!isRemote()) {
            if (this.syncTicks <= 0 || delay < this.syncTicks) {
                this.syncTicks = Server.isSinglePlayer() ? 2 : delay;
            }
        }
    }
}
