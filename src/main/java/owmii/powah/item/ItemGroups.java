package owmii.powah.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import owmii.powah.Powah;
import owmii.powah.block.Blcks;
import owmii.powah.block.Tier;

import java.util.stream.Collectors;

public class ItemGroups {
    public static ResourceKey<CreativeModeTab> MAIN_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Powah.id("tab"));

    public static final DeferredRegister<CreativeModeTab> DR = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Powah.MOD_ID);

    static {
        DR.register("tab", () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.powah.tab"))
                .icon(() -> new ItemStack(Blcks.ENERGY_CELL.get(Tier.BLAZING)))
                .displayItems((params, output) -> {
                    output.accept(Itms.BOOK.get());
                    output.accept(Itms.WRENCH.get());
                    output.accept(Itms.CAPACITOR_BASIC_TINY.get());
                    output.accept(Itms.CAPACITOR_BASIC.get());
                    output.accept(Itms.CAPACITOR_BASIC_LARGE.get());
                    output.accept(Itms.CAPACITOR_HARDENED.get());
                    output.accept(Itms.CAPACITOR_BLAZING.get());
                    output.accept(Itms.CAPACITOR_NIOTIC.get());
                    output.accept(Itms.CAPACITOR_SPIRITED.get());
                    output.accept(Itms.CAPACITOR_NITRO.get());
                    output.acceptAll(Itms.BATTERY.getAll().stream().map(Item::getDefaultInstance).collect(Collectors.toList()));
                    output.accept(Itms.AERIAL_PEARL.get());
                    output.accept(Itms.PLAYER_AERIAL_PEARL.get());
                    output.accept(Itms.BLANK_CARD.get());
                    output.accept(Itms.BINDING_CARD.get());
                    output.accept(Itms.BINDING_CARD_DIM.get());
                    output.accept(Itms.LENS_OF_ENDER.get());
                    output.accept(Itms.PHOTOELECTRIC_PANE.get());
                    output.accept(Itms.THERMOELECTRIC_PLATE.get());
                    output.accept(Itms.DIELECTRIC_PASTE.get());
                    output.accept(Itms.DIELECTRIC_ROD.get());
                    output.accept(Itms.DIELECTRIC_ROD_HORIZONTAL.get());
                    output.accept(Itms.DIELECTRIC_CASING.get());
                    output.accept(Itms.ENERGIZED_STEEL.get());
                    output.accept(Itms.BLAZING_CRYSTAL.get());
                    output.accept(Itms.NIOTIC_CRYSTAL.get());
                    output.accept(Itms.SPIRITED_CRYSTAL.get());
                    output.accept(Itms.NITRO_CRYSTAL.get());
                    output.accept(Itms.ENDER_CORE.get());
                    output.accept(Itms.CHARGED_SNOWBALL.get());
                    output.accept(Itms.URANINITE_RAW.get());
                    output.accept(Itms.URANINITE.get());
                })
                .build());
    }
}
