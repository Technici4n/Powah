package owmii.powah.network.packet;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import owmii.lib.Lollipop;
import owmii.lib.network.IPacket;
import owmii.lib.util.Packet;
import owmii.powah.block.endercell.EnderCellTile;

import java.util.function.Supplier;

public class SSetActiveChannel implements IPacket<SSetActiveChannel> {
    private int activeChannel;
    private BlockPos pos;

    public SSetActiveChannel(int activeChannel, BlockPos pos) {
        this.activeChannel = activeChannel;
        this.pos = pos;
    }

    public SSetActiveChannel() {
        this(0, BlockPos.ZERO);
    }

    public static void send(int activeChannel, BlockPos pos) {
        Lollipop.NET.toServer(new SSetActiveChannel(activeChannel, pos));
    }

    @Override
    public void encode(SSetActiveChannel msg, PacketBuffer buffer) {
        buffer.writeInt(msg.activeChannel);
        Packet.writePos(msg.pos, buffer);
    }

    @Override
    public SSetActiveChannel decode(PacketBuffer buffer) {
        return new SSetActiveChannel(buffer.readInt(), Packet.readPos(buffer));
    }

    @Override
    public void handle(SSetActiveChannel msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null) {
                TileEntity te = player.world.getTileEntity(msg.pos);
                if (te instanceof EnderCellTile) {
                    ((EnderCellTile) te).setActiveChannel(msg.activeChannel);
                    ((EnderCellTile) te).markDirtyAndSync();
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
