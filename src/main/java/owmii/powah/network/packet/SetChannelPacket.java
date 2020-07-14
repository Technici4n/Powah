package owmii.powah.network.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import owmii.lib.network.IPacket;
import owmii.powah.block.ender.AbstractEnderTile;

import java.util.function.Supplier;

public class SetChannelPacket implements IPacket<SetChannelPacket> {
    private BlockPos pos;
    private int channel;

    public SetChannelPacket(BlockPos pos, int channel) {
        this.pos = pos;
        this.channel = channel;
    }

    public SetChannelPacket() {
        this(BlockPos.ZERO, 0);
    }

    @Override
    public void encode(SetChannelPacket msg, PacketBuffer buffer) {
        buffer.writeBlockPos(msg.pos);
        buffer.writeInt(msg.channel);
    }

    @Override
    public SetChannelPacket decode(PacketBuffer buffer) {
        return new SetChannelPacket(buffer.readBlockPos(), buffer.readInt());
    }

    @Override
    public void handle(SetChannelPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            if (player != null) {
                World world = player.getEntityWorld();
                TileEntity te = world.getTileEntity(msg.pos);
                if (te instanceof AbstractEnderTile) {
                    AbstractEnderTile cell = (AbstractEnderTile) te;
                    cell.getChannel().set(msg.channel);
                    cell.sync();
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
