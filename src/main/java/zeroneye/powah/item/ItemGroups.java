package zeroneye.powah.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import zeroneye.powah.Powah;

public class ItemGroups {
    public static final ItemGroup MAIN = new ItemGroup(Powah.MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.POTATO);
        }
    };
}
