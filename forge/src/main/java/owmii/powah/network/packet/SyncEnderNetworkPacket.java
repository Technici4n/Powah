package owmii.powah.network.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import owmii.powah.lib.client.util.MC;
import owmii.powah.lib.network.IPacket;
import owmii.powah.block.ender.EnderNetwork;

import java.util.UUID;
import java.util.function.Supplier;

public class SyncEnderNetworkPacket implements IPacket<SyncEnderNetworkPacket> {
    private UUID id;
    private final CompoundTag nbt;

    public SyncEnderNetworkPacket(UUID id, CompoundTag nbt) {
        this.id = id;
        this.nbt = nbt;
    }

    public SyncEnderNetworkPacket() {
        this(UUID.randomUUID(), new CompoundTag());
    }

    @Override
    public void encode(SyncEnderNetworkPacket msg, FriendlyByteBuf buffer) {
        buffer.writeUUID(this.id);
        buffer.writeNbt(this.nbt);
    }

    @Override
    public SyncEnderNetworkPacket decode(FriendlyByteBuf buffer) {
        return new SyncEnderNetworkPacket(buffer.readUUID(), buffer.readNbt());
    }

    @Override
    public void handle(SyncEnderNetworkPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            MC.world().ifPresent(world -> {
                EnderNetwork.INSTANCE.deserialize(msg.id, msg.nbt);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
