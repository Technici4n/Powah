package owmii.powah.network.packet;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import owmii.lib.Lollipop;
import owmii.lib.network.IPacket;
import owmii.lib.util.Dim;
import owmii.lib.util.Server;
import owmii.powah.block.endercell.EnderCellTile;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public class SSetActiveChannel implements IPacket<SSetActiveChannel> {
    private int activeChannel;
    private int dim;
    private BlockPos pos;

    public SSetActiveChannel(int activeChannel, int dim, BlockPos pos) {
        this.activeChannel = activeChannel;
        this.dim = dim;
        this.pos = pos;
    }

    public SSetActiveChannel() {
        this(0, 0, BlockPos.ZERO);
    }

    public static void send(int activeChannel, @Nullable World world, BlockPos pos) {
        if (world != null) {
            Lollipop.NET.toServer(new SSetActiveChannel(activeChannel, Dim.id(world), pos));
        }
    }

    @Override
    public void encode(SSetActiveChannel msg, PacketBuffer buffer) {
        buffer.writeInt(msg.activeChannel);
        buffer.writeInt(msg.dim);
        buffer.writeBlockPos(msg.pos);
    }

    @Override
    public SSetActiveChannel decode(PacketBuffer buffer) {
        return new SSetActiveChannel(buffer.readInt(), buffer.readInt(), buffer.readBlockPos());
    }

    @Override
    public void handle(SSetActiveChannel msg, Supplier<NetworkEvent.Context> ctx) {
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
