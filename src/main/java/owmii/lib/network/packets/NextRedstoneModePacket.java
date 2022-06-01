package owmii.lib.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import owmii.lib.block.AbstractTileEntity;
import owmii.lib.logistics.IRedstoneInteract;
import owmii.lib.network.IPacket;

import java.util.function.Supplier;

public class NextRedstoneModePacket implements IPacket<NextRedstoneModePacket> {
    private BlockPos pos;

    public NextRedstoneModePacket(BlockPos pos) {
        this.pos = pos;
    }

    public NextRedstoneModePacket() {
        this(BlockPos.ZERO);
    }

    @Override
    public void encode(NextRedstoneModePacket msg, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(msg.pos);
    }

    @Override
    public NextRedstoneModePacket decode(FriendlyByteBuf buffer) {
        return new NextRedstoneModePacket(buffer.readBlockPos());
    }

    @Override
    public void handle(NextRedstoneModePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                BlockEntity tileEntity = player.level.getBlockEntity(msg.pos);
                if (tileEntity instanceof AbstractTileEntity) {
                    if (tileEntity instanceof IRedstoneInteract) {
                        ((IRedstoneInteract) tileEntity).nextRedstoneMode();
                        ((AbstractTileEntity) tileEntity).sync();
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
