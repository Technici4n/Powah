package owmii.powah.item;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import owmii.lib.item.IItemBase;
import owmii.lib.item.ItemBase;
import owmii.powah.block.IBlocks;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class IItems {
    public static final List<Item> ITEMS = new ArrayList<>(IBlocks.BLOCK_ITEMS);
    public static final Item BOOK;
    public static final Item WRENCH;
    public static final Item DIELECTRIC_PASTE;
    public static final Item DIELECTRIC_ROD;
    public static final Item DIELECTRIC_ROD_HORIZONTAL;
    public static final Item DIELECTRIC_CASING;
    public static final Item CAPACITOR_BASIC_TINY;
    public static final Item CAPACITOR_BASIC;
    public static final Item CAPACITOR_BASIC_LARGE;
    public static final Item CAPACITOR_HARDENED;
    public static final Item CAPACITOR_BLAZING;
    public static final Item CAPACITOR_NIOTIC;
    public static final Item CAPACITOR_SPIRITED;
    public static final Item AERIAL_PEARL;
    public static final Item PLAYER_AERIAL_PEARL;
    public static final Item BLANK_CARD;
    public static final Item BINDING_CARD;
    public static final Item PHOTOELECTRIC_PANE;
    public static final Item THERMOELECTRIC_PLATE;
    public static final Item ENERGISED_STEEL;
    public static final Item BLAZING_CRYSTAL;
    public static final Item NIOTIC_CRYSTAL;
    public static final Item SPIRITED_CRYSTAL;
    public static final Item CHARGED_SNOWBALL;
    public static final Item ENDER_CORE;
    public static final Item URANINITE_RAW_POOR;
    public static final Item URANINITE_RAW;
    public static final Item URANINITE_RAW_DENSE;
    public static final Item URANINITE;

    static {
        BOOK = register("book", new PowahBookItem(new Item.Properties().group(ItemGroups.MAIN).maxStackSize(1)));
        WRENCH = register("wrench", new WrenchItem(new Item.Properties().group(ItemGroups.MAIN).maxStackSize(1)));
        DIELECTRIC_PASTE = register("dielectric_paste", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
        DIELECTRIC_ROD = register("dielectric_rod", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
        DIELECTRIC_ROD_HORIZONTAL = register("dielectric_rod_horizontal", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
        DIELECTRIC_CASING = register("dielectric_casing", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
        CAPACITOR_BASIC_TINY = register("capacitor_basic_tiny", new CapacitorItem(new Item.Properties().group(ItemGroups.MAIN)));
        CAPACITOR_BASIC = register("capacitor_basic", new CapacitorItem(new Item.Properties().group(ItemGroups.MAIN)));
        CAPACITOR_BASIC_LARGE = register("capacitor_basic_large", new CapacitorItem(new Item.Properties().group(ItemGroups.MAIN)));
        CAPACITOR_HARDENED = register("capacitor_hardened", new CapacitorItem(new Item.Properties().group(ItemGroups.MAIN)));
        CAPACITOR_BLAZING = register("capacitor_blazing", new CapacitorItem(new Item.Properties().group(ItemGroups.MAIN)));
        CAPACITOR_NIOTIC = register("capacitor_niotic", new CapacitorItem(new Item.Properties().group(ItemGroups.MAIN)));
        CAPACITOR_SPIRITED = register("capacitor_spirited", new CapacitorItem(new Item.Properties().group(ItemGroups.MAIN)));
        AERIAL_PEARL = register("aerial_pearl", new AerialPearlItem(new Item.Properties().group(ItemGroups.MAIN)));
        PLAYER_AERIAL_PEARL = register("player_aerial_pearl", new AerialPearlItem(new Item.Properties().group(ItemGroups.MAIN)));
        BLANK_CARD = register("blank_card", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
        BINDING_CARD = register("binding_card", new BindingCardItem(new Item.Properties().maxStackSize(1).group(ItemGroups.MAIN)));
        PHOTOELECTRIC_PANE = register("photoelectric_pane", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
        THERMOELECTRIC_PLATE = register("thermoelectric_plate", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
        ENERGISED_STEEL = register("energised_steel", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
        BLAZING_CRYSTAL = register("blazing_crystal", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
        NIOTIC_CRYSTAL = register("niotic_crystal", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
        SPIRITED_CRYSTAL = register("spirited_crystal", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
        CHARGED_SNOWBALL = register("charged_snowball", new ChargedSnowballItem(new Item.Properties().group(ItemGroups.MAIN).maxStackSize(16)));
        ENDER_CORE = register("ender_core", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
        URANINITE_RAW_POOR = register("uraninite_raw_poor", new UraniniteItem(new Item.Properties().group(ItemGroups.MAIN)));
        URANINITE_RAW = register("uraninite_raw", new UraniniteItem(new Item.Properties().group(ItemGroups.MAIN)));
        URANINITE_RAW_DENSE = register("uraninite_raw_dense", new UraniniteItem(new Item.Properties().group(ItemGroups.MAIN)));
        URANINITE = register("uraninite", new UraniniteItem(new Item.Properties().group(ItemGroups.MAIN)));
    }

    static <T extends Item & IItemBase> T register(String name, T item) {
        item.setRegistryName(name);
        ITEMS.add(item);
        return item;
    }

    @SubscribeEvent
    public static void onRegistry(RegistryEvent.Register<Item> event) {
        ITEMS.forEach(item -> event.getRegistry().register(item));
    }
}
