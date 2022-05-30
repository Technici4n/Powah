package owmii.lib.client.renderer.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.lib.item.IItem;

import java.util.HashSet;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class TEItemRenderer extends ItemStackTileEntityRenderer {
    private static final Set<Item> ITEMS = new HashSet<>();

    @Override
    public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrix, IRenderTypeBuffer rtb, int light, int ov) {
        final Item item = stack.getItem();
        if (item instanceof IItem) {
            IItem base = (IItem) item;
            if (ITEMS.contains(base)) {
                base.renderByItem(stack, matrix, rtb, light, ov);
            }
        }
    }

    public static void register(Block... blocks) {
        for (Block block : blocks) {
            Item item = block.asItem();
            if (item instanceof IItem) {
                ITEMS.add(item);
            }
        }
    }

    public static void register(Item... items) {
        for (Item item : items) {
            if (item instanceof IItem) {
                ITEMS.add(item);
            }
        }
    }
}
