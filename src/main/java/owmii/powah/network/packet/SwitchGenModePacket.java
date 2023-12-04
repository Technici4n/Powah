package owmii.powah.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import owmii.powah.block.reactor.ReactorTile;
import owmii.powah.network.IPacket;

public class SwitchGenModePacket implements IPacket {
    private final BlockPos pos;

    public SwitchGenModePacket(BlockPos pos) {
        this.pos = pos;
    }

    public SwitchGenModePacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos());
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
    }

    @Override
    public void handle(Player player) {
        Level world = player.getCommandSenderWorld();
        BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof ReactorTile reactor) {
            reactor.setGenModeOn(!reactor.isGenModeOn());
        }
    }
}
