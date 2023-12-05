package owmii.powah.entity;

import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import owmii.powah.Powah;

public class Entities {
    public static final DeferredRegister<EntityType<?>> DR = DeferredRegister.create(Registries.ENTITY_TYPE, Powah.MOD_ID);

    public static final Supplier<EntityType<ChargedSnowballEntity>> CHARGED_SNOWBALL = DR.register("charged_snowball", () -> {
        return EntityType.Builder.<ChargedSnowballEntity>of(ChargedSnowballEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).updateInterval(10)
                .clientTrackingRange(64).build("charged_snowball");
    });
}
