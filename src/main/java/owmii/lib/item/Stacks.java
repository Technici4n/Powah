package owmii.lib.item;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

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
        return new Stacks(Arrays.asList((ItemStack[]) objects), fill);
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

    public int sum() {
        return stream().filter(stack -> !stack.isEmpty())
                .mapToInt(ItemStack::getCount).sum();
    }
}
