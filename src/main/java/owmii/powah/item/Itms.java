package owmii.powah.item;

import net.minecraft.world.item.Item;
import owmii.lib.item.ItemBase;
import owmii.lib.registry.Registry;
import owmii.lib.registry.VarReg;
import owmii.powah.Powah;
import owmii.powah.block.Blcks;
import owmii.powah.block.Tier;

public class Itms {
    public static final Registry<Item> REG = new Registry<>(Item.class, Blcks.REG.getBlockItems(ItemGroups.MAIN), Powah.MOD_ID);
    public static final Item BOOK = REG.register("book", new PowahBookItem(new Item.Properties().tab(ItemGroups.MAIN).stacksTo(1)));
    public static final Item WRENCH = REG.register("wrench", new WrenchItem(new Item.Properties().tab(ItemGroups.MAIN).stacksTo(1)));
    public static final Item CAPACITOR_BASIC_TINY = REG.register("capacitor_basic_tiny", new CapacitorItem(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Item CAPACITOR_BASIC = REG.register("capacitor_basic", new CapacitorItem(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Item CAPACITOR_BASIC_LARGE = REG.register("capacitor_basic_large", new CapacitorItem(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Item CAPACITOR_HARDENED = REG.register("capacitor_hardened", new CapacitorItem(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Item CAPACITOR_BLAZING = REG.register("capacitor_blazing", new CapacitorItem(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Item CAPACITOR_NIOTIC = REG.register("capacitor_niotic", new CapacitorItem(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Item CAPACITOR_SPIRITED = REG.register("capacitor_spirited", new CapacitorItem(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Item CAPACITOR_NITRO = REG.register("capacitor_nitro", new CapacitorItem(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final VarReg<Tier, Item> BATTERY = REG.getVar("battery", variant -> new BatteryItem(new Item.Properties().tab(ItemGroups.MAIN).stacksTo(1), variant), Tier.getNormalVariants());
    public static final Item AERIAL_PEARL = REG.register("aerial_pearl", new AerialPearlItem(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Item PLAYER_AERIAL_PEARL = REG.register("player_aerial_pearl", new AerialPearlItem(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Item BLANK_CARD = REG.register("blank_card", new ItemBase(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Item BINDING_CARD = REG.register("binding_card", new BindingCardItem(new Item.Properties().tab(ItemGroups.MAIN).stacksTo(1), false));
    public static final Item BINDING_CARD_DIM = REG.register("binding_card_dim", new BindingCardItem(new Item.Properties().tab(ItemGroups.MAIN).stacksTo(1), true));
    public static final Item LENS_OF_ENDER = REG.register("lens_of_ender", new LensOfEnderItem(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Item PHOTOELECTRIC_PANE = REG.register("photoelectric_pane", new PhotoelectricPaneItem(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Item THERMOELECTRIC_PLATE = REG.register("thermoelectric_plate", new ItemBase(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Item DIELECTRIC_PASTE = REG.register("dielectric_paste", new ItemBase(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Item DIELECTRIC_ROD = REG.register("dielectric_rod", new ItemBase(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Item DIELECTRIC_ROD_HORIZONTAL = REG.register("dielectric_rod_horizontal", new ItemBase(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Item DIELECTRIC_CASING = REG.register("dielectric_casing", new ItemBase(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Item ENERGIZED_STEEL = REG.register("steel_energized", new ItemBase(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Item BLAZING_CRYSTAL = REG.register("crystal_blazing", new ItemBase(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Item NIOTIC_CRYSTAL = REG.register("crystal_niotic", new ItemBase(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Item SPIRITED_CRYSTAL = REG.register("crystal_spirited", new ItemBase(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Item NITRO_CRYSTAL = REG.register("crystal_nitro", new ItemBase(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Item ENDER_CORE = REG.register("ender_core", new ItemBase(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Item CHARGED_SNOWBALL = REG.register("charged_snowball", new ChargedSnowballItem(new Item.Properties().tab(ItemGroups.MAIN).stacksTo(16)));
    public static final Item URANINITE_RAW_POOR = REG.register("uraninite_raw_poor", new UraniniteItem(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Item URANINITE_RAW = REG.register("uraninite_raw", new UraniniteItem(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Item URANINITE_RAW_DENSE = REG.register("uraninite_raw_dense", new UraniniteItem(new Item.Properties().tab(ItemGroups.MAIN)));
    public static final Item URANINITE = REG.register("uraninite", new UraniniteItem(new Item.Properties().tab(ItemGroups.MAIN)));
}
