package owmii.powah.book.content;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class BookIcon {
    private Type type = Type.ITEM;
    private ItemStack stack = ItemStack.EMPTY;
    private ResourceLocation location = new ResourceLocation("");

    public static BookIcon create(ItemStack stack) {
        BookIcon icon = new BookIcon();
        icon.stack = stack;
        return icon;
    }

    public static BookIcon create(ResourceLocation location) {
        BookIcon icon = new BookIcon();
        icon.location = location;
        icon.type = Type.IMAGE;
        return icon;
    }

    public boolean isEmpty() {
        return this.type.equals(Type.ITEM) && this.stack.isEmpty();
    }

    public enum Type {
        ITEM, IMAGE
    }

    public Type getType() {
        return this.type;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public ResourceLocation getLocation() {
        return this.location;
    }
}
