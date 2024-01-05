package owmii.powah.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import owmii.powah.Powah;
import owmii.powah.block.ender.AbstractEnderTile;
import owmii.powah.network.ServerboundPacket;

public class SetChannelPacket implements ServerboundPacket {
    public static final ResourceLocation ID = Powah.id("set_channel");

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
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeInt(channel);
    }

    @Override
    public void handleOnServer(ServerPlayer player) {
        if (player.serverLevel().getBlockEntity(pos) instanceof AbstractEnderTile<?>cell) {
            cell.getChannel().set(channel);
            cell.sync();
        }
    }
}
