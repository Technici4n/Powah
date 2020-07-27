package owmii.powah.network.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import owmii.lib.network.IPacket;
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
    public void encode(SwitchGenModePacket msg, PacketBuffer buffer) {
        buffer.writeBlockPos(msg.pos);
    }

    @Override
    public SwitchGenModePacket decode(PacketBuffer buffer) {
        return new SwitchGenModePacket(buffer.readBlockPos());
    }

    @Override
    public void handle(SwitchGenModePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            if (player != null) {
                World world = player.getEntityWorld();
                TileEntity te = world.getTileEntity(msg.pos);
                if (te instanceof ReactorTile) {
                    ReactorTile reactor = (ReactorTile) te;
                    reactor.setGenModeOn(!reactor.isGenModeOn());
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
