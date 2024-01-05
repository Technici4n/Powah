package owmii.powah.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import owmii.powah.Powah;
import owmii.powah.lib.block.AbstractEnergyStorage;
import owmii.powah.network.ServerboundPacket;

public class NextEnergyConfigPacket implements ServerboundPacket {
    public static final ResourceLocation ID = Powah.id("next_energy_config");

    private final int mode;
    private final BlockPos pos;

    public NextEnergyConfigPacket(int mode, BlockPos pos) {
        this.mode = mode;
        this.pos = pos;
    }

    public NextEnergyConfigPacket(FriendlyByteBuf buffer) {
        this(buffer.readInt(), buffer.readBlockPos());
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(mode);
        buffer.writeBlockPos(pos);
    }

    @Override
    public void handleOnServer(ServerPlayer player) {
        var tileEntity = player.level().getBlockEntity(pos);
        if (tileEntity instanceof AbstractEnergyStorage storage) {
            if (mode > 5)
                storage.getSideConfig().nextTypeAll();
            else
                storage.getSideConfig().nextType(Direction.from3DDataValue(mode));
            storage.sync();
        }
    }
}
