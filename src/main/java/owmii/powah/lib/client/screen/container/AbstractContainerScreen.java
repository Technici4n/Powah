package owmii.powah.lib.client.screen.container;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import owmii.powah.lib.client.screen.Texture;
import owmii.powah.lib.logistics.inventory.AbstractContainer;
import owmii.powah.lib.logistics.inventory.slot.ITexturedSlot;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AbstractContainerScreen<C extends AbstractContainer> extends net.minecraft.client.gui.screens.inventory.AbstractContainerScreen<C> {
    protected final Minecraft mc = Minecraft.getInstance();
    protected final Texture backGround;

    @Nullable
    protected Runnable delayedClick;
    protected int clickDelay;

    public AbstractContainerScreen(C container, Inventory inv, Component title, Texture backGround) {
        super(container, inv, title);
        this.backGround = backGround;
        this.imageWidth = backGround.getWidth();
        this.imageHeight = backGround.getHeight();
    }

    public void setDelayedClick(int delay, @Nullable Runnable delayedClick) {
        this.clickDelay = delay;
        this.delayedClick = delayedClick;
    }

    @Override
    public void containerTick() {
        super.containerTick();
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
    public void render(PoseStack matrix, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, partialTicks);
        renderTooltip(matrix, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrix, float partialTicks, int mouseX, int mouseY) {
        drawBackground(matrix, partialTicks, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack matrix, int mouseX, int mouseY) {
        drawForeground(matrix, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(PoseStack matrix, int mouseX, int mouseY) {
        super.renderTooltip(matrix, mouseX, mouseY);
        for (var iWidget : this.renderables) {
            if (iWidget instanceof AbstractWidget widget && widget.isHoveredOrFocused()) {
                widget.renderToolTip(matrix, mouseX, mouseY);
                return;
            }
        }
    }

    protected void drawBackground(PoseStack matrix, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.backGround.draw(matrix, this.leftPos, this.topPos);
    }

    protected void drawForeground(PoseStack matrix, int mouseX, int mouseY) {
        drawTitle(matrix, 0, 0);
    }

    protected void drawTitle(PoseStack matrix, int x, int y) {
        String title = this.title.getString();
        int width = this.font.width(title);
        this.font.drawShadow(matrix, title, x + this.imageWidth / 2 - width / 2, y - 14.0F, 0x999999);
    }

    @Override
    public void renderSlot(PoseStack matrixStack, Slot slot) {
        if (slot instanceof ITexturedSlot) {
            ITexturedSlot base = (ITexturedSlot) slot;
            int x = slot.x;
            int y = slot.y;
            base.getBackground2().draw(matrixStack, x, y);
            if (!slot.hasItem()) {
                RenderSystem.enableBlend();
                RenderSystem.enableBlend();
                base.getOverlay().draw(matrixStack, x, y);
                RenderSystem.disableBlend();
            }
        }
        super.renderSlot(matrixStack, slot);
    }

    public void bindTexture(ResourceLocation guiTexture) {
        RenderSystem.setShaderTexture(0, guiTexture);
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
