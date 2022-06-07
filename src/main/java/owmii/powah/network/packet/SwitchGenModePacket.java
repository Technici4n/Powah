package owmii.powah.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import owmii.powah.lib.network.IPacket;
import owmii.powah.block.reactor.ReactorTile;

import java.util.function.Supplier;

public class SwitchGenModePacket implements IPacket<SwitchGenModePacket> {
    private BlockPos pos;

    public SwitchGenModePacket(BlockPos pos) {
        this.pos = pos;
    }

    public SwitchGenModePacket() {
        this(BlockPos.ZERO);
    }

    @Override
    public void encode(SwitchGenModePacket msg, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(msg.pos);
    }

    @Override
    public SwitchGenModePacket decode(FriendlyByteBuf buffer) {
        return new SwitchGenModePacket(buffer.readBlockPos());
    }

    @Override
    public void handle(SwitchGenModePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            if (player != null) {
                Level world = player.getCommandSenderWorld();
                BlockEntity te = world.getBlockEntity(msg.pos);
                if (te instanceof ReactorTile) {
                    ReactorTile reactor = (ReactorTile) te;
                    reactor.setGenModeOn(!reactor.isGenModeOn());
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
