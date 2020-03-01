package owmii.powah.client.screen.inventory;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.lib.client.screen.EnergyScreen;
import owmii.lib.client.screen.widget.IconButton;
import owmii.lib.energy.SideConfig;
import owmii.lib.network.packets.SNextEnergyConfigPacket;
import owmii.lib.network.packets.SNextRedstoneModePacket;
import owmii.powah.Powah;
import owmii.powah.block.cable.EnergyCableTile;
import owmii.powah.inventory.EnergyCableContainer;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class EnergyCableScreen extends EnergyScreen<EnergyCableTile, EnergyCableContainer> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/gui/container/energy_cable.png");
    private IconButton configButton = ICON_BUTTON;

    private Direction side;

    public EnergyCableScreen(EnergyCableContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
        this.xSize = 153;
        this.ySize = 88;
        this.side = container.getSide();
    }

    @Override
    protected void mainButtons(int x, int y) {
        this.redStoneButton = addIconButton(x + this.xSize - 20, y + this.ySize - 20, 15, 15, 45, 0, 15, getWidgetTexture(), (button) -> {
            SNextRedstoneModePacket.send(this.mc.world, this.te.getPos());
            this.te.nextRedstoneMode();
        }).tooltip(this.te.getRedstoneMode().getDisplayName());
    }

    @Override
    protected void configButtons(int x, int y) {
        this.configButton = addIconButton(x + 131, y + 5, 17, 17, 0, 30, 17, getWidgetTexture(), (button) -> {
            SNextEnergyConfigPacket.send(this.side.getIndex(), this.mc.world, this.te.getPos());
            this.te.getSideConfig().nextType(this.side);
        }).tooltip("info.lollipop.side." + this.side.getName(), TextFormatting.GRAY, TextFormatting.DARK_GRAY).tooltip(this.te.getSideConfig().getType(this.side).getDisplayName());
    }

    @Override
    protected void refreshScreen() {
        SideConfig config = this.te.getSideConfig();
        this.configButton.setIconDiff(this.te.getSideConfig().getType(this.side).getXuv());
        if (this.configButton.isHovered()) {
            List<String> list = this.configButton.getTooltip();
            list.add(config.getType(this.side).getDisplayName());
            list.remove(1);
        }
        this.redStoneButton.setIconDiff(this.te.getRedstoneMode().getXuv());
        if (this.redStoneButton.isHovered()) {
            List<String> list = this.redStoneButton.getTooltip();
            list.add(this.te.getRedstoneMode().getDisplayName());
            list.remove(0);
        }
    }

    @Override
    protected void drawBackground(float partialTicks, int mouseX, int mouseY) {
        bindTexture(GUI_TEXTURE);
        blit(this.x, this.y, 0, 0, this.xSize, this.ySize);
        String s = I18n.format("info.lollipop.side." + this.side.getName(), TextFormatting.DARK_GRAY);
        int sw = this.font.getStringWidth(s);
        this.font.drawString(s, this.x + (this.xSize / 2f) - (sw / 2f), this.y + 10, 0x444444);
    }
}
