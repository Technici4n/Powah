package owmii.powah.client.screen.container;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import owmii.powah.inventory.EnergizingOrbContainer;
import owmii.powah.lib.client.screen.Texture;
import owmii.powah.lib.client.screen.container.AbstractContainerScreen;

public class EnergizingOrbScreen extends AbstractContainerScreen<EnergizingOrbContainer> {
    private static final Texture.Builder TEXTURES = new Texture.Builder("lollipop");
    private static final Texture BACKGROUND = TEXTURES.make("container/background", 176, 164, 0, 0);
    private static final Texture PANEL = TEXTURES.make("container/blank", 176, 72, 0, 0);
    private static final Texture SLOT = TEXTURES.make("container/slot/slot", 18, 18);
    private static final int FILTER_X = 147;
    private static final int FILTER_Y = 28;

    public EnergizingOrbScreen(EnergizingOrbContainer menu, Inventory inventory, Component title) {
        super(menu, inventory, title, BACKGROUND);
    }

    @Override
    protected void drawBackground(GuiGraphics gui, float partialTick, int mouseX, int mouseY) {
        int x = leftPos;
        int y = topPos;
        super.drawBackground(gui, partialTick, mouseX, mouseY);
        PANEL.draw(gui, x, y);
        for (int i = 0; i < menu.te.getInventory().getSlots(); i++) {
            Slot slot = menu.getSlot(i);
            SLOT.draw(gui, x + slot.x - 1, y + slot.y - 1);
        }
        SLOT.draw(gui, x + FILTER_X - 1, y + FILTER_Y - 1);
        gui.renderFakeItem(menu.te.getOutputFilter(), x + FILTER_X, y + FILTER_Y);
        gui.fill(x + 68, y + 35, x + 96, y + 40, 0xff475156);
        int filled = Math.round(26 * menu.te.getBuffer().subSized());
        gui.fill(x + 69, y + 36, x + 69 + filled, y + 39, 0xff4fd6de);
    }

    @Override
    protected void drawForeground(GuiGraphics gui, int mouseX, int mouseY) {
        super.drawForeground(gui, mouseX, mouseY);
        gui.drawCenteredString(font, Component.translatable("gui.powah.orb.filter"), FILTER_X + 8, 14, 0xffffff);
        if (menu.te.isOutputFull()) {
            gui.drawString(font, Component.translatable("gui.powah.orb.output_full"), 10, 58, 0x802d12, false);
        } else if (menu.te.hasActiveBatch()) {
            gui.drawCenteredString(font, menu.te.getBuffer().getPercent() + "%", 82, 23, 0xffffff);
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics gui, int mouseX, int mouseY) {
        if (isHovering(FILTER_X, FILTER_Y, 16, 16, mouseX, mouseY)) {
            gui.renderTooltip(font, Component.translatable("gui.powah.orb.filter_hint"), mouseX, mouseY);
        } else {
            super.renderTooltip(gui, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && isHovering(FILTER_X, FILTER_Y, 16, 16, mouseX, mouseY)) {
            minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 0);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
