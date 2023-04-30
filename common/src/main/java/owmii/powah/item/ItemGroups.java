package owmii.powah.item;

import dev.architectury.registry.CreativeTabRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import owmii.powah.Powah;
import owmii.powah.block.Blcks;
import owmii.powah.block.Tier;

public class ItemGroups {
    public static final CreativeModeTab MAIN = CreativeTabRegistry.create(
            Powah.id("tab"),
            () -> new ItemStack(Blcks.ENERGY_CELL.get(Tier.BLAZING)));
}
