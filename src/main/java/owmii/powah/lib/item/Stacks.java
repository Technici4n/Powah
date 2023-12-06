package owmii.powah.lib.item;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

public class Stacks extends NonNullList<ItemStack> {
    protected Stacks(List<ItemStack> delegateIn, @Nullable ItemStack listType) {
        super(delegateIn, listType);
    }

    protected Stacks() {
        this(Lists.newArrayList(), null);
    }

    public static Stacks create() {
        return new Stacks();
    }

    public static Stacks withSize(int size, ItemStack fill) {
        Validate.notNull(fill);
        ItemStack[] objects = new ItemStack[size];
        Arrays.fill(objects, fill);
        return new Stacks(Arrays.asList(objects), fill);
    }

    public static Stacks from(ItemStack... elements) {
        return new Stacks(Arrays.asList(elements), ItemStack.EMPTY);
    }

    public static Stacks from(NonNullList<ItemStack> stacks) {
        return new Stacks(stacks, ItemStack.EMPTY);
    }

    public Stacks copy() {
        Stacks stacks = Stacks.create();
        forEach(stack -> stacks.add(stack.copy()));
        return stacks;
    }
}
