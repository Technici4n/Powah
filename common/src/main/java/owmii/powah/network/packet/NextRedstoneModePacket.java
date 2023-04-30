package owmii.powah.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import owmii.powah.lib.block.AbstractTileEntity;
import owmii.powah.lib.logistics.IRedstoneInteract;
import owmii.powah.network.IPacket;

public class NextRedstoneModePacket implements IPacket {
    private final BlockPos pos;

    public NextRedstoneModePacket(BlockPos pos) {
        this.pos = pos;
    }

    public NextRedstoneModePacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos());
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
    }

    @Override
    public void handle(Player player) {
        if (player instanceof ServerPlayer) {
            BlockEntity tileEntity = player.level.getBlockEntity(pos);
            if (tileEntity instanceof AbstractTileEntity ate) {
                if (tileEntity instanceof IRedstoneInteract ri) {
                    ri.nextRedstoneMode();
                    ate.sync();
                }
            }
        }
    }
}
