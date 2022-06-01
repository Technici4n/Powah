package owmii.lib.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import owmii.lib.block.AbstractEnergyStorage;
import owmii.lib.network.IPacket;

import java.util.function.Supplier;

public class NextEnergyConfigPacket implements IPacket<NextEnergyConfigPacket> {
    private int mode;
    private BlockPos pos;

    public NextEnergyConfigPacket(int mode, BlockPos pos) {
        this.mode = mode;
        this.pos = pos;
    }

    public NextEnergyConfigPacket() {
        this(0, BlockPos.ZERO);
    }

    @Override
    public void encode(NextEnergyConfigPacket msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.mode);
        buffer.writeBlockPos(msg.pos);
    }

    @Override
    public NextEnergyConfigPacket decode(FriendlyByteBuf buffer) {
        return new NextEnergyConfigPacket(buffer.readInt(), buffer.readBlockPos());
    }

    @Override
    public void handle(NextEnergyConfigPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                BlockEntity tileEntity = player.level.getBlockEntity(msg.pos);
                if (tileEntity instanceof AbstractEnergyStorage) {
                    AbstractEnergyStorage storage = ((AbstractEnergyStorage) tileEntity);
                    if (msg.mode > 5) storage.getSideConfig().nextTypeAll();
                    else storage.getSideConfig().nextType(Direction.from3DDataValue(msg.mode));
                    storage.sync();
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
