package owmii.powah.lib.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface IVariantEntry<V extends IVariant, R extends IVariantEntry<V, R>> {
    V getVariant();

    default ResourceLocation getSiblingsKey(R o) {
        if (o instanceof IForgeRegistryEntry) {
            ResourceLocation rl = ((IForgeRegistryEntry) o).getRegistryName();
            if (rl != null) {
                return new ResourceLocation(rl.getNamespace(), rl.getPath().replace("_" + getVariant().getName(), ""));
            }
        }
        return new ResourceLocation("");
    }
}
