package owmii.powah.lib.client.screen.container;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import owmii.powah.lib.client.screen.Texture;
import owmii.powah.lib.logistics.inventory.BaseMenu;
import owmii.powah.lib.logistics.inventory.slot.ITexturedSlot;

public class PowahBaseContainerScreen<C extends BaseMenu> extends net.minecraft.client.gui.screens.inventory.AbstractContainerScreen<C> {
    protected final Texture backGround;

    public PowahBaseContainerScreen(C container, Inventory inv, Component title, Texture backGround) {
        super(container, inv, title, backGround.getWidth(), backGround.getHeight());
        this.backGround = backGround;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor gui, int mouseX, int mouseY, float partialTicks) {
        super.extractRenderState(gui, mouseX, mouseY, partialTicks);
        extractTooltip(gui, mouseX, mouseY);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor gui, int mouseX, int mouseY, float partialTicks) {
        drawBackground(gui, partialTicks, mouseX, mouseY);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor gui, int mouseX, int mouseY) {
        drawForeground(gui, mouseX, mouseY);
    }

    protected void drawBackground(GuiGraphicsExtractor gui, float partialTicks, int mouseX, int mouseY) {
        this.backGround.draw(gui, this.leftPos, this.topPos);
    }

    protected void drawForeground(GuiGraphicsExtractor gui, int mouseX, int mouseY) {
        drawTitle(gui, 0, 0);
    }

    protected void drawTitle(GuiGraphicsExtractor gui, int x, int y) {
        String title = this.title.getString();
        int width = this.font.width(title);
        gui.text(this.font, title, x + this.imageWidth / 2 - width / 2, y - 14, 0xff999999);
    }

    @Override
    public void extractSlot(GuiGraphicsExtractor gui, Slot slot, int mouseX, int mouseY) {
        if (slot instanceof ITexturedSlot<?> base) {
            int x = slot.x;
            int y = slot.y;
            base.getBackground2().draw(gui, x, y);
            if (!slot.hasItem()) {
                base.getOverlay().draw(gui, x, y);
            }
        }
        super.extractSlot(gui, slot, mouseX, mouseY);
    }

    public boolean isMouseOver(double mouseX, double mouseY, int w, int h) {
        return mouseX >= this.leftPos && mouseY >= this.topPos && mouseX < this.leftPos + w && mouseY < this.topPos + h;
    }

    public List<Rect2i> getExtraAreas() {
        return new ArrayList<>();
    }

    protected Rect2i toRectangle2d(int x, int y, Texture texture) {
        return new Rect2i(x, y, texture.getWidth(), texture.getHeight());
    }
}
