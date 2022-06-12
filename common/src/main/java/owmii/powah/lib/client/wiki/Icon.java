package owmii.powah.lib.client.wiki;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import owmii.powah.lib.client.screen.Texture;

public class Icon {
    private final int type;
    private ItemStack stack = ItemStack.EMPTY;
    private Texture texture = Texture.EMPTY;

    public Icon(ItemLike provider) {
        this(new ItemStack(provider));
    }

    public Icon(ItemStack stack) {
        this.type = 0;
        this.stack = stack;
    }

    public Icon(Texture texture) {
        this.type = 1;
        this.texture = texture;
    }

    public void draw(PoseStack matrix, int x, int y) {
        if (this.type == 1) {
            this.texture.draw(matrix, x, y);
        } else {

        }
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public Icon setStackInSlot(ItemStack stack) {
        this.stack = stack;
        return this;
    }
}
