package zeroneye.powah.item;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import zeroneye.lib.item.IItemBase;
import zeroneye.lib.item.ItemBase;
import zeroneye.powah.block.IBlocks;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class IItems {
    public static final List<Item> ITEMS = new ArrayList<>(IBlocks.BLOCK_ITEMS);
    public static final Item DIELECTRIC_PASTE;
    public static final Item DIELECTRIC_CASING;
    public static final Item CAPACITOR_BASIC_TINY;
    public static final Item CAPACITOR_BASIC;
    public static final Item CAPACITOR_BASIC_LARGE;
    public static final Item CAPACITOR_BLAZING;
    public static final Item CAPACITOR_NIOTIC;
    public static final Item CAPACITOR_SPIRITED;
    public static final Item BINDING_CARD;

    static {
        DIELECTRIC_PASTE = register("dielectric_paste", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
        DIELECTRIC_CASING = register("dielectric_casing", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
        CAPACITOR_BASIC_TINY = register("capacitor_basic_tiny", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
        CAPACITOR_BASIC = register("capacitor_basic", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
        CAPACITOR_BASIC_LARGE = register("capacitor_basic_large", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
        CAPACITOR_BLAZING = register("capacitor_blazing", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
        CAPACITOR_NIOTIC = register("capacitor_niotic", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
        CAPACITOR_SPIRITED = register("capacitor_spirited", new ItemBase(new Item.Properties().group(ItemGroups.MAIN)));
        BINDING_CARD = register("binding_card", new BindingCardItem(new Item.Properties().maxStackSize(1).group(ItemGroups.MAIN)));
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
