package zeroneye.powah.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class IEntities {
    public static final List<EntityType<?>> ENTITIES = new ArrayList<>();
    public static final EntityType<ChargedSnowballEntity> CHARGED_SNOWBALL = register("charged_snowball", ChargedSnowballEntity::new, EntityClassification.MISC, 0.25F, 0.25F, 10, 64, true);

    static <T extends Item> T register(String name, T item) {
        item.setRegistryName(name);
        return item;
    }

    private static <T extends Entity> EntityType<T> register(String key, EntityType.IFactory<T> factory, EntityClassification classification, float width, float height, int updateInterval, int rage, boolean sendVelocity) {
        EntityType<T> entityType = EntityType.Builder.create(factory, classification)
                .size(width, height)
                .setUpdateInterval(updateInterval)
                .setTrackingRange(rage)
                .setShouldReceiveVelocityUpdates(sendVelocity)
                .build(key);
        entityType.setRegistryName(key);
        ENTITIES.add(entityType);
        return entityType;
    }

    @SubscribeEvent
    public static void onRegistry(RegistryEvent.Register<EntityType<?>> event) {
        ENTITIES.forEach(item -> event.getRegistry().register(item));
    }
}
