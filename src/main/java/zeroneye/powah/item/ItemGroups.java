package zeroneye.powah.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import zeroneye.powah.Powah;
import zeroneye.powah.block.IBlocks;

public class ItemGroups {
    public static final ItemGroup MAIN = new ItemGroup(Powah.MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(IBlocks.ENERGY_CELLS[0]);
        }
    };
}
