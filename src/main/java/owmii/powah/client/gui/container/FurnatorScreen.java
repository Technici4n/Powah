package owmii.powah.client.gui.container;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.lib.client.util.Draw2D;
import owmii.powah.Powah;
import owmii.powah.block.generator.furnator.FurnatorTile;
import owmii.powah.inventory.FurnatorContainer;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class FurnatorScreen extends PowahScreen<FurnatorContainer> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/gui/container/furnator.png");

    public FurnatorScreen(FurnatorContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
    }

    @Override
    protected void drawBackground(float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(partialTicks, mouseX, mouseY);
        FurnatorTile genTile = this.container.getTile();
        if (genTile.nextGen > 0 && !this.sideButtons[0].visible) {
            bindTexture(getSubBackGroundImage());
            if (genTile.nextGenCap >= genTile.nextGen) {
                Draw2D.gaugeV(this.x + 83, this.y + 40, 11, 23, 0, 72, genTile.nextGenCap, genTile.nextGen);
            }
        }
    }

    @Override
    protected void renderHoveredToolTip(int mouseX, int mouseY) {
        super.renderHoveredToolTip(mouseX, mouseY);
        FurnatorTile genTile = this.container.getTile();
        if (isMouseOver(mouseX - 83, mouseY - 40, 11, 23)) {
            List<String> list = new ArrayList<>();
            int percent = genTile.nextGenCap > 0 ? (100 * genTile.nextGen) / genTile.nextGenCap : 0;
            list.add(TextFormatting.GRAY + I18n.format("info.powah.buffer", "" + TextFormatting.DARK_GRAY + percent + "%"));
            renderTooltip(list, mouseX, mouseY);
        }
    }

    @Override
    protected ResourceLocation getSubBackGroundImage() {
        return GUI_TEXTURE;
    }
}
