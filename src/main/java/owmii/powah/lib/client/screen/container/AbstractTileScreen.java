package owmii.powah.lib.client.screen.container;

import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import owmii.powah.lib.block.AbstractTileEntity;
import owmii.powah.lib.block.IInventoryHolder;
import owmii.powah.lib.client.screen.Texture;
import owmii.powah.lib.client.screen.widget.IconButton;
import owmii.powah.lib.logistics.inventory.AbstractTileContainer;
import owmii.powah.network.Network;
import owmii.powah.network.packet.NextRedstoneModePacket;

public class AbstractTileScreen<T extends AbstractTileEntity<?, ?> & IInventoryHolder, C extends AbstractTileContainer<T>>
        extends AbstractContainerScreen<C> {
    protected final T te;
    protected IconButton redStoneButton = IconButton.EMPTY;

    public AbstractTileScreen(C container, Inventory inv, Component title, Texture backGround) {
        super(container, inv, title, backGround);
        this.te = container.te;
    }

    protected void addRedstoneButton(int x, int y) {
        if (hasRedstone()) {
            this.redStoneButton = addRenderableWidget(new IconButton(this.leftPos + this.imageWidth + x + 2, this.topPos + y + 3,
                    Texture.REDSTONE.get(this.te.getRedstoneMode()), b -> {
                        Network.toServer(new NextRedstoneModePacket(this.te.getBlockPos()));
                        this.te.setRedstoneMode(this.te.getRedstoneMode().next());
                    }, this).setTooltipSupplier(() -> List.of(this.te.getRedstoneMode().getDisplayName())));
        }
    }

    @Override
    public void containerTick() {
        super.containerTick();
        if (hasRedstone()) {
            this.redStoneButton.setTexture(Texture.REDSTONE.get(this.te.getRedstoneMode()));
        }
    }

    @Override
    protected void drawBackground(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(guiGraphics, partialTicks, mouseX, mouseY);
        if (hasRedstone()) {
            Texture.REDSTONE_BTN_BG.draw(guiGraphics, this.redStoneButton.getX() - 2, this.redStoneButton.getY() - 4); // TODO
        }
    }

    protected boolean hasRedstone() { // TODO invert
        return true;
    }
}
