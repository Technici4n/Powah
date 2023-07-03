package owmii.powah.client.screen.container;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import owmii.powah.block.cable.CableTile;
import owmii.powah.client.screen.Textures;
import owmii.powah.inventory.CableContainer;
import owmii.powah.lib.client.screen.container.AbstractEnergyScreen;
import owmii.powah.lib.client.screen.widget.IconButton;
import owmii.powah.network.Network;
import owmii.powah.network.packet.NextEnergyConfigPacket;

import java.util.List;

public class CableScreen extends AbstractEnergyScreen<CableTile, CableContainer> {
    private IconButton configButton = IconButton.EMPTY;
    private Direction side;

    public CableScreen(CableContainer container, Inventory inv, Component title) {
        super(container, inv, title, Textures.CABLE);
        this.side = container.getSide();
    }

    @Override
    protected void init() {
        super.init();

        this.configButton = addRenderableWidget(
                new IconButton(this.leftPos + 5, this.topPos + 5, Textures.CABLE_CONFIG.get(this.te.getSideConfig().getType(this.side)), button -> {
                    Network.toServer(new NextEnergyConfigPacket(this.side.get3DDataValue(), this.te.getBlockPos()));
                    this.te.getSideConfig().nextType(this.side);
                }, this).setTooltipSupplier(() -> List.of(
                    Component.translatable("info.lollipop.side." + this.side.getSerializedName(), ChatFormatting.DARK_GRAY)
                            .withStyle(ChatFormatting.GRAY),
                    this.te.getSideConfig().getType(this.side).getDisplayName2()
                )));
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.configButton.setTexture(Textures.CABLE_CONFIG.get(this.te.getSideConfig().getType(this.side)));
    }

    @Override
    protected void addRedstoneButton(int x, int y) {
        super.addRedstoneButton(-21, 7);
    }

    @Override
    protected void drawForeground(GuiGraphics gui, int mouseX, int mouseY) {
        super.drawForeground(gui, mouseX, mouseY);
        String title = I18n.get("info.lollipop.side." + this.side.getName(), ChatFormatting.DARK_GRAY);
        int width = this.font.width(title);
        gui.drawString(font, title, this.imageWidth / 2 - width / 2, 10, 0x555555, false);
    }

    @Override
    protected boolean hasConfigButtons() {
        return false;
    }
}
