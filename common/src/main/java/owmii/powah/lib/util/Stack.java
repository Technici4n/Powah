package owmii.powah.lib.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import owmii.powah.lib.item.Stacks;
import owmii.powah.lib.logistics.inventory.ItemHandlerHelper;

public class Stack {
    public static Stacks copy(Stacks stacks) {
        Stacks itemStacks = Stacks.create();
        stacks.forEach(stack -> itemStacks.add(stack.copy()));
        return itemStacks;
    }

    public static Stacks singleton(ItemStack stack) {
        Stacks itemStacks = Stacks.create();
        itemStacks.add(stack);
        return itemStacks;
    }

    public static boolean canMergeInSlot(ItemStack inSlot, ItemStack stack) {
        return !stack.isEmpty() && (inSlot.isEmpty() || ItemHandlerHelper.canItemStacksStack(inSlot, stack)
                && inSlot.getCount() + stack.getCount() <= inSlot.getMaxStackSize());
    }

    public static boolean equals(ItemStack stack, ItemStack other) {
        return !stack.isEmpty() && ItemStack.isSameItemSameTags(stack, other);
    }

    public static boolean isFull(ItemStack stack) {
        return stack.getCount() >= stack.getMaxStackSize();
    }

    public static boolean isNBTEqual(ItemStack stack, ItemStack stack1) {
        return getTagOrEmpty(stack).equals(getTagOrEmpty(stack1));
    }

    public static CompoundTag getTagOrEmptyChild(ItemStack stack, String key) {
        CompoundTag nbt = stack.getTagElement(key);
        return nbt != null ? nbt : new CompoundTag();
    }

    public static CompoundTag getTagOrEmpty(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        return nbt != null ? nbt : new CompoundTag();
    }

    public static String path(ItemStack provider) {
        return location(provider).getPath();
    }

    public static String modId(ItemStack provider) {
        return location(provider).getNamespace();
    }

    public static ResourceLocation location(ItemStack stack) {
        return location(stack.getItem());
    }

    public static String path(ItemLike provider) {
        return location(provider).getPath();
    }

    public static String modId(ItemLike provider) {
        return location(provider).getNamespace();
    }

    @SuppressWarnings("ConstantConditions")
    public static ResourceLocation location(ItemLike provider) {
        return BuiltInRegistries.ITEM.getKey(provider.asItem());
    }

    public static boolean orEquals(ItemStack stack, ItemStack... stacks) {
        for (ItemStack stack1 : stacks) {
            if (stack.equals(stack1)) {
                return true;
            }
        }
        return false;
    }

    public static void drop(Entity entity, ItemStack stack) {
        drop(entity.level(), entity.position().add(0.0D, 0.3D, 0.0D), stack);
    }

    public static void drop(Level world, BlockPos pos, ItemStack stack) {
        drop(world, new Vec3(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D), stack);
    }

    public static void drop(Level world, Vec3 pos, ItemStack stack) {
        ItemEntity entity = new ItemEntity(world, pos.x(), pos.y(), pos.z(), stack);
        entity.setPickUpDelay(8);
        world.addFreshEntity(entity);
    }
}
