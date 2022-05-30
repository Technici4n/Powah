package owmii.lib.client.renderer.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;
import java.util.Collection;

public interface IItemColorHolder {
    @OnlyIn(Dist.CLIENT)
    IItemColor getItemColor();

    static void registerAll(Item... items) {
        registerAll(Arrays.asList(items));
    }

    static void registerAll(Collection<Item> items) {
        items.forEach(IItemColorHolder::register);
    }

    static void register(Item item) {
        if (item instanceof IItemColorHolder) {
            Minecraft.getInstance().getItemColors()
                    .register(((IItemColorHolder) item)
                            .getItemColor(), item);
        }
    }
}
