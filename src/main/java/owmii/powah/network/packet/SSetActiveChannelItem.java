package owmii.powah.network.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkEvent;
import owmii.lib.Lollipop;
import owmii.lib.network.IPacket;
import owmii.lib.util.Data;

import java.util.function.Supplier;

public class SSetActiveChannelItem implements IPacket<SSetActiveChannelItem> {
    private int activeChannel;

    public SSetActiveChannelItem(int activeChannel) {
        this.activeChannel = activeChannel;
    }

    public SSetActiveChannelItem() {
        this(0);
    }

    public static void send(int activeChannel) {
        Lollipop.NET.toServer(new SSetActiveChannelItem(activeChannel));
    }

    @Override
    public void encode(SSetActiveChannelItem msg, PacketBuffer buffer) {
        buffer.writeInt(msg.activeChannel);
    }

    @Override
    public SSetActiveChannelItem decode(PacketBuffer buffer) {
        return new SSetActiveChannelItem(buffer.readInt());
    }

    @Override
    public void handle(SSetActiveChannelItem msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            if (player != null) {
                for (Hand hand : Hand.values()) {
                    ItemStack stack = player.getHeldItem(hand);
                    CompoundNBT tag = stack.getTag() != null ? stack.getTag() : new CompoundNBT();
                    if (tag.contains(Data.TAG_TE_STORABLE, Constants.NBT.TAG_COMPOUND)) {
                        CompoundNBT stackTag = tag.getCompound(Data.TAG_TE_STORABLE);
                        if (stackTag.contains("ActiveChannel", Constants.NBT.TAG_INT)) {
                            stackTag.putInt("ActiveChannel", msg.activeChannel);
                            tag.put(Data.TAG_TE_STORABLE, stackTag);
                            break;
                        }
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
