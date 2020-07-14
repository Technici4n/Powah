package owmii.powah.network.packet;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import owmii.lib.client.util.Client;
import owmii.lib.network.IPacket;
import owmii.powah.block.ender.EnderNetwork;

import java.util.UUID;
import java.util.function.Supplier;

public class SyncEnderNetworkPacket implements IPacket<SyncEnderNetworkPacket> {
    private UUID id;
    private final CompoundNBT nbt;

    public SyncEnderNetworkPacket(UUID id, CompoundNBT nbt) {
        this.id = id;
        this.nbt = nbt;
    }

    public SyncEnderNetworkPacket() {
        this(UUID.randomUUID(), new CompoundNBT());
    }

    @Override
    public void encode(SyncEnderNetworkPacket msg, PacketBuffer buffer) {
        buffer.writeUniqueId(this.id);
        buffer.writeCompoundTag(this.nbt);
    }

    @Override
    public SyncEnderNetworkPacket decode(PacketBuffer buffer) {
        return new SyncEnderNetworkPacket(buffer.readUniqueId(), buffer.readCompoundTag());
    }

    @Override
    public void handle(SyncEnderNetworkPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Client.world().ifPresent(world -> {
                EnderNetwork.INSTANCE.deserialize(msg.id, msg.nbt);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
