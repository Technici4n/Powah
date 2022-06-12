package owmii.powah.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import owmii.powah.lib.block.AbstractEnergyStorage;
import owmii.powah.network.IPacket;

public class NextEnergyConfigPacket implements IPacket {
    private final int mode;
    private final BlockPos pos;

    public NextEnergyConfigPacket(int mode, BlockPos pos) {
        this.mode = mode;
        this.pos = pos;
    }

    public NextEnergyConfigPacket (FriendlyByteBuf buffer) {
        this(buffer.readInt(), buffer.readBlockPos());
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(mode);
        buffer.writeBlockPos(pos);
    }

    @Override
    public void handle(Player player) {
        if (player instanceof ServerPlayer) {
            BlockEntity tileEntity = player.level.getBlockEntity(pos);
            if (tileEntity instanceof AbstractEnergyStorage storage) {
                if (mode > 5) storage.getSideConfig().nextTypeAll();
                else storage.getSideConfig().nextType(Direction.from3DDataValue(mode));
                storage.sync();
            }
        }
    }
}
