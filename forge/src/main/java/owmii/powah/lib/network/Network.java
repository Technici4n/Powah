package owmii.powah.lib.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class Network {
    private final ResourceLocation location;
    private final SimpleChannel channel;
    private int id;

    public Network(String id) {
        this.location = new ResourceLocation(id, "main");
        this.channel = NetworkRegistry.ChannelBuilder.named(this.location)
                .clientAcceptedVersions("1"::equals)
                .serverAcceptedVersions("1"::equals)
                .networkProtocolVersion(() -> "1")
                .simpleChannel();
    }

    @SuppressWarnings("unchecked")
    public <T> void register(IPacket<T> message) {
        this.channel.registerMessage(this.id++, (Class<T>) message.getClass(), message::encode, message::decode, message::handle);
    }

    @OnlyIn(Dist.CLIENT)
    public <T> void toServer(T msg) {
        this.channel.sendToServer(msg);
    }

    public <T> void toAll(T msg) {
        this.channel.send(PacketDistributor.ALL.noArg(), msg);
    }

    public <T> void toClient(T msg, Player player) {
        if (player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            this.channel.send(PacketDistributor.PLAYER.with(() -> serverPlayer), msg);
        }
    }

    public <T> void toTracking(T msg, Entity entity) {
        this.channel.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), msg);
    }

    public <T> void toTrackingAndSelf(T msg, Entity entity) {
        this.channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), msg);
    }
}
