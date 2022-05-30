package owmii.lib.network.packets;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
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
    public void encode(NextRedstoneModePacket msg, PacketBuffer buffer) {
        buffer.writeBlockPos(msg.pos);
    }

    @Override
    public NextRedstoneModePacket decode(PacketBuffer buffer) {
        return new NextRedstoneModePacket(buffer.readBlockPos());
    }

    @Override
    public void handle(NextRedstoneModePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null) {
                TileEntity tileEntity = player.world.getTileEntity(msg.pos);
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
