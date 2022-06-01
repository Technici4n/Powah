package owmii.powah.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
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
    public void encode(SetChannelPacket msg, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(msg.pos);
        buffer.writeInt(msg.channel);
    }

    @Override
    public SetChannelPacket decode(FriendlyByteBuf buffer) {
        return new SetChannelPacket(buffer.readBlockPos(), buffer.readInt());
    }

    @Override
    public void handle(SetChannelPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            if (player != null) {
                Level world = player.getCommandSenderWorld();
                BlockEntity te = world.getBlockEntity(msg.pos);
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
