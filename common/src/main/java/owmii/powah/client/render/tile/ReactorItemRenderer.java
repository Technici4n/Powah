package owmii.powah.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.ItemStack;
import owmii.powah.block.Blcks;
import owmii.powah.block.Tier;
import owmii.powah.block.reactor.ReactorPartTile;
import owmii.powah.item.ReactorItem;

public class ReactorItemRenderer extends BlockEntityWithoutLevelRenderer {
	public ReactorItemRenderer() {
		super(null, null);
	}

	@Override
	public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		if (stack.getItem() instanceof ReactorItem reactorItem) {
			var tile = new ReactorPartTile(BlockPos.ZERO, reactorItem.getBlock().defaultBlockState(), reactorItem.getVariant());
			var dispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();
			dispatcher.renderItem(tile, poseStack, buffer, packedLight, packedOverlay);
		}
	}
}
