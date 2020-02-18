package owmii.powah.item;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import owmii.lib.item.ItemBase;
import owmii.powah.block.IBlocks;
import owmii.powah.block.Tier;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class IItems {
    public static final List<Item> ITEMS = new ArrayList<>(IBlocks.BLOCK_ITEMS);
    public static final Item BOOK = register("book", new PowahBookItem(new Item.Properties().group(ItemGroups.MAIN).maxStackSize(1)));
    public static final Item WRENCH = register("wrench", new WrenchItem(new Item.Properties().group(ItemGroups.MAIN).maxStackSize(1)));
    public static final Item CAPACITOR_BASIC_TINY = register("capacitor_basic_tiny", new CapacitorItem(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item CAPACITOR_BASIC = register("capacitor_basic", new CapacitorItem(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item CAPACITOR_BASIC_LARGE = register("capacitor_basic_large", new CapacitorItem(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item CAPACITOR_HARDENED = register("capacitor_hardened", new CapacitorItem(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item CAPACITOR_BLAZING = register("capacitor_blazing", new CapacitorItem(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item CAPACITOR_NIOTIC = register("capacitor_niotic", new CapacitorItem(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item CAPACITOR_SPIRITED = register("capacitor_spirited", new CapacitorItem(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item CAPACITOR_NITRO = register("capacitor_nitro", new CapacitorItem(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item BATTERY_STARTER = register("battery_starter", new BatteryItem(new Item.Properties().group(ItemGroups.MAIN).maxStackSize(1), Tier.STARTER));
    public static final Item BATTERY_BASIC = register("battery_basic", new BatteryItem(new Item.Properties().group(ItemGroups.MAIN).maxStackSize(1), Tier.BASIC));
    public static final Item BATTERY_HARDENED = register("battery_hardened", new BatteryItem(new Item.Properties().group(ItemGroups.MAIN).maxStackSize(1), Tier.HARDENED));
    public static final Item BATTERY_BLAZING = register("battery_blazing", new BatteryItem(new Item.Properties().group(ItemGroups.MAIN).maxStackSize(1), Tier.BLAZING));
    public static final Item BATTERY_NIOTIC = register("battery_niotic", new BatteryItem(new Item.Properties().group(ItemGroups.MAIN).maxStackSize(1), Tier.NIOTIC));
    public static final Item BATTERY_SPIRITED = register("battery_spirited", new BatteryItem(new Item.Properties().group(ItemGroups.MAIN).maxStackSize(1), Tier.SPIRITED));
    public static final Item BATTERY_NITRO = register("battery_nitro", new BatteryItem(new Item.Properties().group(ItemGroups.MAIN).maxStackSize(1), Tier.NITRO));
    public static final Item AERIAL_PEARL = register("aerial_pearl", new AerialPearlItem(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item PLAYER_AERIAL_PEARL = register("player_aerial_pearl", new AerialPearlItem(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item BLANK_CARD = register("blank_card", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item BINDING_CARD = register("binding_card", new BindingCardItem(new Item.Properties().group(ItemGroups.MAIN).maxStackSize(1), false));
    public static final Item BINDING_CARD_DIM = register("binding_card_dim", new BindingCardItem(new Item.Properties().group(ItemGroups.MAIN).maxStackSize(1), true));
    public static final Item LENS_OF_ENDER = register("lens_of_ender", new LensOfEnderItem(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item PHOTOELECTRIC_PANE = register("photoelectric_pane", new PhotoelectricPaneItem(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item THERMOELECTRIC_PLATE = register("thermoelectric_plate", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item DIELECTRIC_PASTE = register("dielectric_paste", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item DIELECTRIC_ROD = register("dielectric_rod", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item DIELECTRIC_ROD_HORIZONTAL = register("dielectric_rod_horizontal", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item DIELECTRIC_CASING = register("dielectric_casing", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item ENERGIZED_STEEL = register("steel_energized", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item BLAZING_CRYSTAL = register("crystal_blazing", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item NIOTIC_CRYSTAL = register("crystal_niotic", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item SPIRITED_CRYSTAL = register("crystal_spirited", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item NITRO_CRYSTAL = register("crystal_nitro", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item ENDER_CORE = register("ender_core", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item CHARGED_SNOWBALL = register("charged_snowball", new ChargedSnowballItem(new Item.Properties().group(ItemGroups.MAIN).maxStackSize(16)));
    public static final Item URANINITE_RAW_POOR = register("uraninite_raw_poor", new UraniniteItem(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item URANINITE_RAW = register("uraninite_raw", new UraniniteItem(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item URANINITE_RAW_DENSE = register("uraninite_raw_dense", new UraniniteItem(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item URANINITE = register("uraninite", new UraniniteItem(new Item.Properties().group(ItemGroups.MAIN)));

    static <T extends net.minecraft.item.Item> T register(final String name, final T item) {
        item.setRegistryName(name);
        IItems.ITEMS.add(item);
        return item;
    }

    @SubscribeEvent
    public static void onRegistry(final RegistryEvent.Register<Item> event) {
        IItems.ITEMS.forEach(item -> event.getRegistry().register(item));
    }
}
