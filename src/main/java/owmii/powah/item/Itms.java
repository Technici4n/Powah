package owmii.powah.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import owmii.powah.lib.item.ItemBase;
import owmii.powah.lib.registry.VarReg;
import owmii.powah.Powah;
import owmii.powah.block.Tier;

import java.util.function.Supplier;

public class Itms {
    public static final DeferredRegister<Item> DR = DeferredRegister.create(ForgeRegistries.ITEMS, Powah.MOD_ID);

    public static final Supplier<Item> BOOK = DR.register("book", () -> new PowahBookItem(new Item.Properties().tab(ItemGroups.MAIN).stacksTo(1)));
    public static final Supplier<Item> WRENCH = DR.register("wrench", () -> new WrenchItem(new Item.Properties().tab(ItemGroups.MAIN).stacksTo(1)));
    public static final Supplier<Item> CAPACITOR_BASIC_TINY = DR.register("capacitor_basic_tiny", () -> new CapacitorItem(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Supplier<Item> CAPACITOR_BASIC = DR.register("capacitor_basic", () -> new CapacitorItem(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Supplier<Item> CAPACITOR_BASIC_LARGE = DR.register("capacitor_basic_large", () -> new CapacitorItem(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Supplier<Item> CAPACITOR_HARDENED = DR.register("capacitor_hardened", () -> new CapacitorItem(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Supplier<Item> CAPACITOR_BLAZING = DR.register("capacitor_blazing", () -> new CapacitorItem(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Supplier<Item> CAPACITOR_NIOTIC = DR.register("capacitor_niotic", () -> new CapacitorItem(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Supplier<Item> CAPACITOR_SPIRITED = DR.register("capacitor_spirited", () -> new CapacitorItem(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Supplier<Item> CAPACITOR_NITRO = DR.register("capacitor_nitro", () -> new CapacitorItem(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final VarReg<Tier, Item> BATTERY = new VarReg<>(DR, "battery", variant -> new BatteryItem(new Item.Properties().tab(ItemGroups.MAIN).stacksTo(1), variant), Tier.getNormalVariants());
    public static final Supplier<Item> AERIAL_PEARL = DR.register("aerial_pearl", () -> new AerialPearlItem(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Supplier<Item> PLAYER_AERIAL_PEARL = DR.register("player_aerial_pearl", () -> new AerialPearlItem(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Supplier<Item> BLANK_CARD = DR.register("blank_card", () -> new ItemBase(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Supplier<Item> BINDING_CARD = DR.register("binding_card", () -> new BindingCardItem(new Item.Properties().tab(ItemGroups.MAIN).stacksTo(1), false));
    public static final Supplier<Item> BINDING_CARD_DIM = DR.register("binding_card_dim", () -> new BindingCardItem(new Item.Properties().tab(ItemGroups.MAIN).stacksTo(1), true));
    public static final Supplier<Item> LENS_OF_ENDER = DR.register("lens_of_ender", () -> new LensOfEnderItem(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Supplier<Item> PHOTOELECTRIC_PANE = DR.register("photoelectric_pane", () -> new PhotoelectricPaneItem(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Supplier<Item> THERMOELECTRIC_PLATE = DR.register("thermoelectric_plate", () -> new ItemBase(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Supplier<Item> DIELECTRIC_PASTE = DR.register("dielectric_paste", () -> new ItemBase(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Supplier<Item> DIELECTRIC_ROD = DR.register("dielectric_rod", () -> new ItemBase(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Supplier<Item> DIELECTRIC_ROD_HORIZONTAL = DR.register("dielectric_rod_horizontal", () -> new ItemBase(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Supplier<Item> DIELECTRIC_CASING = DR.register("dielectric_casing", () -> new ItemBase(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Supplier<Item> ENERGIZED_STEEL = DR.register("steel_energized", () -> new ItemBase(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Supplier<Item> BLAZING_CRYSTAL = DR.register("crystal_blazing", () -> new ItemBase(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Supplier<Item> NIOTIC_CRYSTAL = DR.register("crystal_niotic", () -> new ItemBase(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Supplier<Item> SPIRITED_CRYSTAL = DR.register("crystal_spirited", () -> new ItemBase(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Supplier<Item> NITRO_CRYSTAL = DR.register("crystal_nitro", () -> new ItemBase(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Supplier<Item> ENDER_CORE = DR.register("ender_core", () -> new ItemBase(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Supplier<Item> CHARGED_SNOWBALL = DR.register("charged_snowball", () -> new ChargedSnowballItem(new Item.Properties().tab(ItemGroups.MAIN).stacksTo(16)));
    public static final Supplier<Item> URANINITE_RAW_POOR = DR.register("uraninite_raw_poor", () -> new UraniniteItem(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Supplier<Item> URANINITE_RAW = DR.register("uraninite_raw", () -> new UraniniteItem(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Supplier<Item> URANINITE_RAW_DENSE = DR.register("uraninite_raw_dense", () -> new UraniniteItem(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Supplier<Item> URANINITE = DR.register("uraninite", () -> new UraniniteItem(new Item.Properties().tab(ItemGroups.MAIN)));
}
