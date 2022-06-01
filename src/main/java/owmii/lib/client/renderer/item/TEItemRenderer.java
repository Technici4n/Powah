package owmii.lib.client.renderer.item;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.lib.item.IItem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.HashSet;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class TEItemRenderer extends BlockEntityWithoutLevelRenderer {
    private static final Set<Item> ITEMS = new HashSet<>();

    public TEItemRenderer() {
        //noinspection ConstantConditions
        super(null, null);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack matrix, MultiBufferSource rtb, int light, int ov) {
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
