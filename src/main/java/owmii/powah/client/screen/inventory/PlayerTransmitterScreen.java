package owmii.powah.client.screen.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.powah.Powah;
import owmii.powah.inventory.PlayerTransmitterContainer;

@OnlyIn(Dist.CLIENT)
public class PlayerTransmitterScreen extends PowahScreen<PlayerTransmitterContainer> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/gui/container/player_transmitter.png");
    private static final ResourceLocation GUI_TEXTURE_DIM = new ResourceLocation(Powah.MOD_ID, "textures/gui/container/player_transmitter_dim.png");

    public PlayerTransmitterScreen(PlayerTransmitterContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
    }

    @Override
    protected ResourceLocation getSubBackGroundImage() {
        return this.container.slots > 1 ? GUI_TEXTURE_DIM : GUI_TEXTURE;
    }
}
