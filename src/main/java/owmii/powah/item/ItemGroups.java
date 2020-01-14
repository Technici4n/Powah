package owmii.powah.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.powah.Powah;

public class ItemGroups {
    public static final ItemGroup MAIN = new ItemGroup(Powah.MOD_ID) {
        @Override
        @OnlyIn(Dist.CLIENT)
        public ItemStack createIcon() {
            return BREWING.getIcon();
        }
    };
}
