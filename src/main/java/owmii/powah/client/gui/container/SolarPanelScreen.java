package owmii.powah.client.gui.container;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.powah.Powah;
import owmii.powah.block.generator.panel.solar.SolarPanelTile;
import owmii.powah.inventory.SolarPanelContainer;

@OnlyIn(Dist.CLIENT)
public class SolarPanelScreen extends PowahScreen<SolarPanelContainer> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/gui/container/solar_panel.png");

    public SolarPanelScreen(SolarPanelContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
    }

    @Override
    protected void drawForeground(int mouseX, int mouseY) {
        super.drawForeground(mouseX, mouseY);
        if (this.sideButtons[0].visible) return;
        SolarPanelTile genTile = this.container.getTile();
        boolean b = genTile.canSeeSunLight();
        String s = I18n.format("info.powah.sunlight", b ? TextFormatting.DARK_GREEN : TextFormatting.DARK_RED);
        String y = I18n.format("info.powah.yes");
        String n = I18n.format("info.powah.no");
        this.font.drawString(s + (b ? y : n), 60, 32.0F, 0x777777);

    }

    @Override
    protected void drawBackground(float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(partialTicks, mouseX, mouseY);
        SolarPanelTile genTile = this.container.getTile();
        if (genTile.canSeeSunLight() && !this.sideButtons[0].visible) {
            bindTexture(getSubBackGroundImage());
            this.blit(this.x + 73, this.y + 5, 153, 0, 30, 30);
        }
    }

    @Override
    protected ResourceLocation getSubBackGroundImage() {
        return GUI_TEXTURE;
    }
}
