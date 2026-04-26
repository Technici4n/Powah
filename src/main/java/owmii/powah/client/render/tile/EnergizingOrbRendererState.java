package owmii.powah.client.render.tile;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;

public class EnergizingOrbRendererState extends BlockEntityRenderState {
    public float ticks;
    public ItemStackRenderState outputItem = new ItemStackRenderState();
    public ItemStackRenderState[] inputItems = new ItemStackRenderState[0];
    public Direction orbUp;
}
