package owmii.powah.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import owmii.lib.registry.Registry;
import owmii.powah.Powah;

public class Entities {
    @SuppressWarnings("unchecked")
    public static final Registry<EntityType<?>> REG = new Registry(EntityType.class, Powah.MOD_ID);
    public static final EntityType<ChargedSnowballEntity> CHARGED_SNOWBALL = REG.register("charged_snowball", ChargedSnowballEntity::new, MobCategory.MISC, 0.25F, 0.25F, 10, 64, true);
}
