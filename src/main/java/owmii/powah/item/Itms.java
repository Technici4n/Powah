package owmii.powah.item;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import owmii.powah.Powah;
import owmii.powah.block.Tier;
import owmii.powah.lib.item.ItemBase;
import owmii.powah.lib.registry.VarReg;

public class Itms {
    public static final DeferredRegister.Items DR = DeferredRegister.createItems(Powah.MOD_ID);

    public static final DeferredItem<PowahBookItem> BOOK = DR.register("book",
            () -> new PowahBookItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<WrenchItem> WRENCH = DR.register("wrench",
            () -> new WrenchItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<CapacitorItem> CAPACITOR_BASIC_TINY = DR.register("capacitor_basic_tiny",
            () -> new CapacitorItem(new Item.Properties()));
    public static final DeferredItem<CapacitorItem> CAPACITOR_BASIC = DR.register("capacitor_basic",
            () -> new CapacitorItem(new Item.Properties()));
    public static final DeferredItem<CapacitorItem> CAPACITOR_BASIC_LARGE = DR.register("capacitor_basic_large",
            () -> new CapacitorItem(new Item.Properties()));
    public static final DeferredItem<CapacitorItem> CAPACITOR_HARDENED = DR.register("capacitor_hardened",
            () -> new CapacitorItem(new Item.Properties()));
    public static final DeferredItem<CapacitorItem> CAPACITOR_BLAZING = DR.register("capacitor_blazing",
            () -> new CapacitorItem(new Item.Properties()));
    public static final DeferredItem<CapacitorItem> CAPACITOR_NIOTIC = DR.register("capacitor_niotic",
            () -> new CapacitorItem(new Item.Properties()));
    public static final DeferredItem<CapacitorItem> CAPACITOR_SPIRITED = DR.register("capacitor_spirited",
            () -> new CapacitorItem(new Item.Properties()));
    public static final DeferredItem<CapacitorItem> CAPACITOR_NITRO = DR.register("capacitor_nitro",
            () -> new CapacitorItem(new Item.Properties()));
    public static final VarReg<Tier, Item> BATTERY = new VarReg<>(DR, "battery",
            variant -> new BatteryItem(new Item.Properties().stacksTo(1), variant), Tier.getNormalVariants());
    public static final DeferredItem<AerialPearlItem> AERIAL_PEARL = DR.register("aerial_pearl",
            () -> new AerialPearlItem(new Item.Properties()));
    public static final DeferredItem<AerialPearlItem> PLAYER_AERIAL_PEARL = DR.register("player_aerial_pearl",
            () -> new AerialPearlItem(new Item.Properties()));
    public static final DeferredItem<ItemBase> BLANK_CARD = DR.register("blank_card",
            () -> new ItemBase(new Item.Properties()));
    public static final DeferredItem<BindingCardItem> BINDING_CARD = DR.register("binding_card",
            () -> new BindingCardItem(new Item.Properties().stacksTo(1), false));
    public static final DeferredItem<BindingCardItem> BINDING_CARD_DIM = DR.register("binding_card_dim",
            () -> new BindingCardItem(new Item.Properties().stacksTo(1), true));
    public static final DeferredItem<LensOfEnderItem> LENS_OF_ENDER = DR.register("lens_of_ender",
            () -> new LensOfEnderItem(new Item.Properties()));
    public static final DeferredItem<PhotoelectricPaneItem> PHOTOELECTRIC_PANE = DR.register("photoelectric_pane",
            () -> new PhotoelectricPaneItem(new Item.Properties()));
    public static final DeferredItem<ItemBase> THERMOELECTRIC_PLATE = DR.register("thermoelectric_plate",
            () -> new ItemBase(new Item.Properties()));
    public static final DeferredItem<ItemBase> DIELECTRIC_PASTE = DR.register("dielectric_paste",
            () -> new ItemBase(new Item.Properties()));
    public static final DeferredItem<ItemBase> DIELECTRIC_ROD = DR.register("dielectric_rod",
            () -> new ItemBase(new Item.Properties()));
    public static final DeferredItem<ItemBase> DIELECTRIC_ROD_HORIZONTAL = DR.register("dielectric_rod_horizontal",
            () -> new ItemBase(new Item.Properties()));
    public static final DeferredItem<ItemBase> DIELECTRIC_CASING = DR.register("dielectric_casing",
            () -> new ItemBase(new Item.Properties()));
    public static final DeferredItem<ItemBase> ENERGIZED_STEEL = DR.register("steel_energized",
            () -> new ItemBase(new Item.Properties()));
    public static final DeferredItem<ItemBase> BLAZING_CRYSTAL = DR.register("crystal_blazing",
            () -> new ItemBase(new Item.Properties()));
    public static final DeferredItem<ItemBase> NIOTIC_CRYSTAL = DR.register("crystal_niotic",
            () -> new ItemBase(new Item.Properties()));
    public static final DeferredItem<ItemBase> SPIRITED_CRYSTAL = DR.register("crystal_spirited",
            () -> new ItemBase(new Item.Properties()));
    public static final DeferredItem<ItemBase> NITRO_CRYSTAL = DR.register("crystal_nitro",
            () -> new ItemBase(new Item.Properties()));
    public static final DeferredItem<ItemBase> ENDER_CORE = DR.register("ender_core",
            () -> new ItemBase(new Item.Properties()));
    public static final DeferredItem<ChargedSnowballItem> CHARGED_SNOWBALL = DR.register("charged_snowball",
            () -> new ChargedSnowballItem(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<UraniniteItem> URANINITE_RAW = DR.register("uraninite_raw",
            () -> new UraniniteItem(new Item.Properties()));
    public static final DeferredItem<UraniniteItem> URANINITE = DR.register("uraninite",
            () -> new UraniniteItem(new Item.Properties()));
}
