package owmii.powah.client.screen.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import owmii.lib.Lollipop;
import owmii.lib.client.screen.AbstractEnergyScreen;
import owmii.lib.client.screen.widget.IconButton;
import owmii.lib.network.packets.NextEnergyConfigPacket;
import owmii.powah.block.cable.CableTile;
import owmii.powah.client.screen.Textures;
import owmii.powah.inventory.CableContainer;

public class CableScreen extends AbstractEnergyScreen<CableTile, CableContainer> {
    private IconButton configButton = IconButton.EMPTY;
    private Direction side;

    public CableScreen(CableContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title, Textures.CABLE);
        this.side = container.getSide();
    }

    @Override
    protected void func_231160_c_() {
        super.func_231160_c_();

        this.configButton = func_230480_a_(new IconButton(this.guiLeft + 5, this.guiTop + 5, Textures.CABLE_CONFIG.get(this.te.getSideConfig().getType(this.side)), button -> {
            Lollipop.NET.toServer(new NextEnergyConfigPacket(this.side.getIndex(), this.te.getPos()));
            this.te.getSideConfig().nextType(this.side);
        }, this).setTooltip(tooltip -> {
            tooltip.add(new TranslationTextComponent("info.lollipop.side." + this.side.func_176610_l(), TextFormatting.DARK_GRAY).func_240699_a_(TextFormatting.GRAY));
            tooltip.add(this.te.getSideConfig().getType(this.side).getDisplayName());
        }));
    }

    @Override
    public void func_231023_e_() {
        super.func_231023_e_();
        this.configButton.setTexture(Textures.CABLE_CONFIG.get(this.te.getSideConfig().getType(this.side)));
    }

    @Override
    protected void addRedstoneButton(int x, int y) {
        super.addRedstoneButton(-21, 7);
    }

    @Override
    protected void drawForeground(MatrixStack matrix, int mouseX, int mouseY) {
        super.drawForeground(matrix, mouseX, mouseY);
        String title = I18n.format("info.lollipop.side." + this.side.getName2(), TextFormatting.DARK_GRAY);
        int width = this.field_230712_o_.getStringWidth(title);
        this.field_230712_o_.func_238421_b_(matrix, title, this.xSize / 2 - width / 2, 10.0F, 0x555555);
    }

    @Override
    protected boolean hasConfigButtons() {
        return false;
    }
}
