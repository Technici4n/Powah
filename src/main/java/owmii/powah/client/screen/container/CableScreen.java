package owmii.powah.client.screen.container;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import owmii.powah.lib.Lollipop;
import owmii.powah.lib.client.screen.container.AbstractEnergyScreen;
import owmii.powah.lib.client.screen.widget.IconButton;
import owmii.powah.lib.network.packets.NextEnergyConfigPacket;
import owmii.powah.block.cable.CableTile;
import owmii.powah.client.screen.Textures;
import owmii.powah.inventory.CableContainer;

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

        this.configButton = addRenderableWidget(new IconButton(this.leftPos + 5, this.topPos + 5, Textures.CABLE_CONFIG.get(this.te.getSideConfig().getType(this.side)), button -> {
            Lollipop.NET.toServer(new NextEnergyConfigPacket(this.side.get3DDataValue(), this.te.getBlockPos()));
            this.te.getSideConfig().nextType(this.side);
        }, this).setTooltip(tooltip -> {
            tooltip.add(new TranslatableComponent("info.lollipop.side." + this.side.getSerializedName(), ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.GRAY));
            tooltip.add(this.te.getSideConfig().getType(this.side).getDisplayName2());
        }));
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
    protected void drawForeground(PoseStack matrix, int mouseX, int mouseY) {
        super.drawForeground(matrix, mouseX, mouseY);
        String title = I18n.get("info.lollipop.side." + this.side.getName(), ChatFormatting.DARK_GRAY);
        int width = this.font.width(title);
        this.font.draw(matrix, title, this.imageWidth / 2 - width / 2, 10.0F, 0x555555);
    }

    @Override
    protected boolean hasConfigButtons() {
        return false;
    }
}
