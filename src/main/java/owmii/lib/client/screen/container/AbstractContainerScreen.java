package owmii.lib.client.screen.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import owmii.lib.client.screen.Texture;
import owmii.lib.logistics.inventory.AbstractContainer;
import owmii.lib.logistics.inventory.slot.ITexturedSlot;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AbstractContainerScreen<C extends AbstractContainer> extends ContainerScreen<C> {
    protected final Minecraft mc = Minecraft.getInstance();
    protected final Texture backGround;

    @Nullable
    protected Runnable delayedClick;
    protected int clickDelay;

    public AbstractContainerScreen(C container, PlayerInventory inv, ITextComponent title, Texture backGround) {
        super(container, inv, title);
        this.backGround = backGround;
        this.xSize = backGround.getWidth();
        this.ySize = backGround.getHeight();
    }

    public void setDelayedClick(int delay, @Nullable Runnable delayedClick) {
        this.clickDelay = delay;
        this.delayedClick = delayedClick;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.delayedClick != null) {
            if (this.clickDelay >= 0) {
                this.clickDelay--;
                if (this.clickDelay == 0) {
                    this.delayedClick.run();
                    this.delayedClick = null;
                }
            }
        }
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, partialTicks);
        renderHoveredTooltip(matrix, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        drawBackground(matrix, partialTicks, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrix, int mouseX, int mouseY) {
        drawForeground(matrix, mouseX, mouseY);
    }

    @Override
    protected void renderHoveredTooltip(MatrixStack matrix, int mouseX, int mouseY) {
        super.renderHoveredTooltip(matrix, mouseX, mouseY);
        for (Widget widget : this.buttons) {
            if (widget.isHovered()) {
                widget.renderToolTip(matrix, mouseX, mouseY);
                return;
            }
        }
    }

    protected void drawBackground(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.backGround.draw(matrix, this.guiLeft, this.guiTop);
    }

    protected void drawForeground(MatrixStack matrix, int mouseX, int mouseY) {
        drawTitle(matrix, 0, 0);
    }

    protected void drawTitle(MatrixStack matrix, int x, int y) {
        String title = this.title.getString();
        int width = this.font.getStringWidth(title);
        this.font.drawStringWithShadow(matrix, title, x + this.xSize / 2 - width / 2, y - 14.0F, 0x999999);
    }

    @Override
    public void moveItems(MatrixStack matrixStack, Slot slot) {
        if (slot instanceof ITexturedSlot) {
            ITexturedSlot base = (ITexturedSlot) slot;
            int x = slot.xPos;
            int y = slot.yPos;
            base.getBackground2().draw(matrixStack, x, y);
            if (!slot.getHasStack()) {
                RenderSystem.pushMatrix();
                RenderSystem.enableBlend();
                base.getOverlay().draw(matrixStack, x, y);
                RenderSystem.disableBlend();
                RenderSystem.popMatrix();
            }
        }
        super.moveItems(matrixStack, slot);
    }

    public void bindTexture(ResourceLocation guiTexture) {
        this.mc.getTextureManager().bindTexture(guiTexture);
    }

    public boolean isMouseOver(double mouseX, double mouseY, int w, int h) {
        return mouseX >= this.guiLeft && mouseY >= this.guiTop && mouseX < this.guiLeft + w && mouseY < this.guiTop + h;
    }

    protected <T extends Widget> T addWidget(T w) {
        this.buttons.add(w);
        return addListener(w);
    }

    public List<Rectangle2d> getExtraAreas() {
        return new ArrayList<>();
    }

    protected Rectangle2d toRectangle2d(int x, int y, Texture texture) {
        return new Rectangle2d(x, y, texture.getWidth(), texture.getHeight());
    }

    protected Rectangle2d toRectangle2d(Widget widget) {
        return new Rectangle2d(widget.x, widget.y, widget.getWidth(), widget.getHeightRealms());
    }
}
