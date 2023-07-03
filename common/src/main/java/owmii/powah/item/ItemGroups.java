package owmii.powah.item;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import owmii.powah.Powah;
import owmii.powah.block.Blcks;
import owmii.powah.block.Tier;

public class ItemGroups {
    public static ResourceKey<CreativeModeTab> MAIN_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Powah.id("tab"));

    public static final DeferredRegister<CreativeModeTab> DR = DeferredRegister.create(Powah.MOD_ID, Registries.CREATIVE_MODE_TAB);

    static {
        DR.register("tab", () -> CreativeTabRegistry.create(
                Component.translatable("itemGroup.powah.tab"),
                () -> new ItemStack(Blcks.ENERGY_CELL.get(Tier.BLAZING))));
    }
}
