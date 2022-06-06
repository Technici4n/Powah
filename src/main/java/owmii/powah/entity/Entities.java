package owmii.powah.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import owmii.lib.registry.Registry;
import owmii.powah.Powah;

import java.util.function.Supplier;

public class Entities {
    public static final DeferredRegister<EntityType<?>> DR = DeferredRegister.create(ForgeRegistries.ENTITIES, Powah.MOD_ID);

    public static final Supplier<EntityType<ChargedSnowballEntity>> CHARGED_SNOWBALL = DR.register("charged_snowball", () -> {
        return EntityType.Builder.<ChargedSnowballEntity>of(ChargedSnowballEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).setUpdateInterval(10).setTrackingRange(64).setShouldReceiveVelocityUpdates(true).build("charged_snowball");
    });
}
