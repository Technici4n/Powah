package owmii.powah.network.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkEvent;
import owmii.lib.network.IPacket;
import owmii.lib.util.Data;

import java.util.function.Supplier;

public class SetActiveChannelItem implements IPacket<SetActiveChannelItem> {
    private int activeChannel;

    public SetActiveChannelItem(int activeChannel) {
        this.activeChannel = activeChannel;
    }

    public SetActiveChannelItem() {
        this(0);
    }

    @Override
    public void encode(SetActiveChannelItem msg, PacketBuffer buffer) {
        buffer.writeInt(msg.activeChannel);
    }

    @Override
    public SetActiveChannelItem decode(PacketBuffer buffer) {
        return new SetActiveChannelItem(buffer.readInt());
    }

    @Override
    public void handle(SetActiveChannelItem msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            if (player != null) {
                for (Hand hand : Hand.values()) {
                    ItemStack stack = player.getHeldItem(hand);
                    CompoundNBT tag = stack.getTag() != null ? stack.getTag() : new CompoundNBT();
                    if (tag.contains(Data.TAG_STORABLE, Constants.NBT.TAG_COMPOUND)) {
                        CompoundNBT stackTag = tag.getCompound(Data.TAG_STORABLE);
                        if (stackTag.contains("ActiveChannel", Constants.NBT.TAG_INT)) {
                            stackTag.putInt("ActiveChannel", msg.activeChannel);
                            tag.put(Data.TAG_STORABLE, stackTag);
                            break;
                        }
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
