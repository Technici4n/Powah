package owmii.powah.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import owmii.powah.network.IPacket;
import owmii.powah.block.ender.AbstractEnderTile;

public class SetChannelPacket implements IPacket {
    private final BlockPos pos;
    private final int channel;

    public SetChannelPacket(BlockPos pos, int channel) {
        this.pos = pos;
        this.channel = channel;
    }

    public SetChannelPacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos(), buffer.readInt());
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeInt(channel);
    }

    @Override
    public void handle(Player player) {
        Level world = player.getCommandSenderWorld();
        BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof AbstractEnderTile cell) {
            cell.getChannel().set(channel);
            cell.sync();
        }
    }
}
