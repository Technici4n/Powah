package owmii.powah.entity;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import owmii.lib.registry.Registry;
import owmii.powah.Powah;

public class Entities {
    public static final Registry<EntityType<?>> REG = new Registry<>(Powah.MOD_ID);
    public static final EntityType<ChargedSnowballEntity> CHARGED_SNOWBALL = REG.register("charged_snowball", ChargedSnowballEntity::new, EntityClassification.MISC, 0.25F, 0.25F, 10, 64, true);
}
