package owmii.powah.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import owmii.powah.Powah;
import owmii.powah.lib.block.AbstractTileEntity;
import owmii.powah.network.ServerboundPacket;

public class NextRedstoneModePacket implements ServerboundPacket {
    public static final ResourceLocation ID = Powah.id("next_redstone_mode");

    private final BlockPos pos;

    public NextRedstoneModePacket(BlockPos pos) {
        this.pos = pos;
    }

    public NextRedstoneModePacket(FriendlyByteBuf buffer) {
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
        BlockEntity tileEntity = player.serverLevel().getBlockEntity(pos);
        if (tileEntity instanceof AbstractTileEntity<?, ?>ate) {
            ate.nextRedstoneMode();
            ate.sync();
        }
    }
}
