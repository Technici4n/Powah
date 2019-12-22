package owmii.powah.client.gui.container;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.powah.Powah;
import owmii.powah.energy.PowahStorage;
import owmii.powah.inventory.CableContainer;

@OnlyIn(Dist.CLIENT)
public class CableScreen extends PowahScreen<CableContainer> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/gui/container/blank_wide.png");
    private static final ResourceLocation CONF_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/gui/container/configuration_wide.png");

    public CableScreen(CableContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
    }

    @Override
    protected void drawForeground(int mouseX, int mouseY) {
        super.drawForeground(mouseX, mouseY);
        if (this.sideButtons[0].visible) return;
        PowahStorage storage = this.container.getTile().getInternal();
        int maxOut = storage.getMaxReceive();
        int maxIn = storage.getMaxExtract();
        this.font.drawString(I18n.format("info.powah.max.io", "" + TextFormatting.DARK_GREEN + (maxIn == maxOut ? maxOut : maxIn == 0 || maxOut == 0 ? Math.max(maxIn, maxOut) : (maxIn + "/" + maxOut))), 8.0F, 10, 4210752);
    }

    @Override
    protected ResourceLocation getSubBackGroundImage() {
        return GUI_TEXTURE;
    }

    @Override
    protected ResourceLocation getConfigBackGroundImage() {
        return CONF_TEXTURE;
    }

    @Override
    protected boolean hasEnergyBare() {
        return false;
    }
}
