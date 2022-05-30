package owmii.lib.network.packets;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
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
    public void encode(NextEnergyConfigPacket msg, PacketBuffer buffer) {
        buffer.writeInt(msg.mode);
        buffer.writeBlockPos(msg.pos);
    }

    @Override
    public NextEnergyConfigPacket decode(PacketBuffer buffer) {
        return new NextEnergyConfigPacket(buffer.readInt(), buffer.readBlockPos());
    }

    @Override
    public void handle(NextEnergyConfigPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null) {
                TileEntity tileEntity = player.world.getTileEntity(msg.pos);
                if (tileEntity instanceof AbstractEnergyStorage) {
                    AbstractEnergyStorage storage = ((AbstractEnergyStorage) tileEntity);
                    if (msg.mode > 5) storage.getSideConfig().nextTypeAll();
                    else storage.getSideConfig().nextType(Direction.byIndex(msg.mode));
                    storage.sync();
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
