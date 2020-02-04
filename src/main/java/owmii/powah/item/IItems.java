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
    public static final Item WRENCH = register("wrench", (Item) new WrenchItem(new Item.Properties().group(ItemGroups.MAIN).maxStackSize(1)));
    public static final Item BATTERY_STARTER = register("battery_starter", (Item) new BatteryItem(new Item.Properties().group(ItemGroups.MAIN).maxStackSize(1), Tier.STARTER));
    public static final Item BATTERY_BASIC = register("battery_basic", (Item) new BatteryItem(new Item.Properties().group(ItemGroups.MAIN).maxStackSize(1), Tier.BASIC));
    public static final Item BATTERY_HARDENED = register("battery_hardened", (Item) new BatteryItem(new Item.Properties().group(ItemGroups.MAIN).maxStackSize(1), Tier.HARDENED));
    public static final Item BATTERY_BLAZING = register("battery_blazing", (Item) new BatteryItem(new Item.Properties().group(ItemGroups.MAIN).maxStackSize(1), Tier.BLAZING));
    public static final Item BATTERY_NIOTIC = register("battery_niotic", (Item) new BatteryItem(new Item.Properties().group(ItemGroups.MAIN).maxStackSize(1), Tier.NIOTIC));
    public static final Item BATTERY_SPIRITED = register("battery_spirited", (Item) new BatteryItem(new Item.Properties().group(ItemGroups.MAIN).maxStackSize(1), Tier.SPIRITED));
    public static final Item BATTERY_NITRO = register("battery_nitro", (Item) new BatteryItem(new Item.Properties().group(ItemGroups.MAIN).maxStackSize(1), Tier.NITRO));
    public static final Item ENERGIZED_STEEL = register("steel_energized", (Item) new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item BLAZING_CRYSTAL = register("crystal_blazing", (Item) new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item NIOTIC_CRYSTAL = register("crystal_niotic", (Item) new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item SPIRITED_CRYSTAL = register("crystal_spirited", (Item) new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
    public static final Item NITRO_CRYSTAL = register("crystal_nitro", (Item) new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));

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
