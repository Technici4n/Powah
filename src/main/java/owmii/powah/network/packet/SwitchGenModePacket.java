package owmii.powah.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import owmii.powah.Powah;
import owmii.powah.block.reactor.ReactorTile;
import owmii.powah.network.ServerboundPacket;

public class SwitchGenModePacket implements ServerboundPacket {
    public static ResourceLocation ID = Powah.id("switch_gen_mode");

    private final BlockPos pos;

    public SwitchGenModePacket(BlockPos pos) {
        this.pos = pos;
    }

    public SwitchGenModePacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos());
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
    }

    @Override
    public void handleOnServer(ServerPlayer player) {
        var be = player.serverLevel().getBlockEntity(pos);
        if (be instanceof ReactorTile reactor) {
            reactor.setGenModeOn(!reactor.isGenModeOn());
        }
    }
}
