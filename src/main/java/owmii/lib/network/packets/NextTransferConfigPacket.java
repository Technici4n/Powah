package owmii.lib.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
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
    public void encode(NextTransferConfigPacket msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.i);
        buffer.writeBlockPos(msg.pos);
    }

    @Override
    public NextTransferConfigPacket decode(FriendlyByteBuf buffer) {
        return new NextTransferConfigPacket(buffer.readInt(), buffer.readBlockPos());
    }

    @Override
    public void handle(NextTransferConfigPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                BlockEntity te = player.level.getBlockEntity(msg.pos);
                if (te instanceof ISidedHopper && te instanceof AbstractTileEntity) {
                    ISidedHopper handler = ((ISidedHopper) te);
                    if (msg.i > 5) handler.getHopperConfig().nextTypeAll();
                    else handler.getHopperConfig().nextType(Direction.from3DDataValue(msg.i));
                    ((AbstractTileEntity) te).sync();
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
