package owmii.lib.network.packets;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import owmii.lib.Lollipop;
import owmii.lib.block.AbstractTileEntity;
import owmii.lib.logistics.inventory.ISidedHopper;
import owmii.lib.network.IPacket;

import java.util.function.Supplier;

public class NextTransferConfigPacket implements IPacket<NextTransferConfigPacket> {
    private int i;
    private BlockPos pos;

    public NextTransferConfigPacket(int i, BlockPos pos) {
        this.i = i;
        this.pos = pos;
    }

    public NextTransferConfigPacket() {
        this(0, BlockPos.ZERO);
    }

    public static void send(int i, BlockPos pos) {
        Lollipop.NET.toServer(new NextTransferConfigPacket(i, pos));
    }

    @Override
    public void encode(NextTransferConfigPacket msg, PacketBuffer buffer) {
        buffer.writeInt(msg.i);
        buffer.writeBlockPos(msg.pos);
    }

    @Override
    public NextTransferConfigPacket decode(PacketBuffer buffer) {
        return new NextTransferConfigPacket(buffer.readInt(), buffer.readBlockPos());
    }

    @Override
    public void handle(NextTransferConfigPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null) {
                TileEntity te = player.world.getTileEntity(msg.pos);
                if (te instanceof ISidedHopper && te instanceof AbstractTileEntity) {
                    ISidedHopper handler = ((ISidedHopper) te);
                    if (msg.i > 5) handler.getHopperConfig().nextTypeAll();
                    else handler.getHopperConfig().nextType(Direction.byIndex(msg.i));
                    ((AbstractTileEntity) te).sync();
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
