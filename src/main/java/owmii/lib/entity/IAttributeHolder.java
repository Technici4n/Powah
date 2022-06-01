package owmii.lib.entity;

import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public interface IAttributeHolder {
    AttributeSupplier.Builder getAttribute();
}
