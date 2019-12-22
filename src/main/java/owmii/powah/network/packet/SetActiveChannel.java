package owmii.powah.network.packet;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import owmii.lib.util.Server;
import owmii.powah.block.storage.endercell.EnderCellTile;

import java.util.Optional;
import java.util.function.Supplier;

public class SetActiveChannel {
    private int activeChannel;
    private int dim;
    private BlockPos pos;

    public SetActiveChannel(int activeChannel, int dim, BlockPos pos) {
        this.activeChannel = activeChannel;
        this.dim = dim;
        this.pos = pos;
    }

    public static void encode(SetActiveChannel msg, PacketBuffer buffer) {
        buffer.writeInt(msg.activeChannel);
        buffer.writeInt(msg.dim);
        buffer.writeBlockPos(msg.pos);
    }

    public static SetActiveChannel decode(PacketBuffer buffer) {
        return new SetActiveChannel(buffer.readInt(), buffer.readInt(), buffer.readBlockPos());
    }

    public static void handle(SetActiveChannel msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Optional<ServerWorld> world = Server.getWorld(msg.dim);
            world.ifPresent(serverWorld -> {
                TileEntity tileEntity = serverWorld.getTileEntity(msg.pos);
                if (tileEntity instanceof EnderCellTile) {
                    ((EnderCellTile) tileEntity).setActiveChannel(msg.activeChannel);
                    ((EnderCellTile) tileEntity).markDirtyAndSync();
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
