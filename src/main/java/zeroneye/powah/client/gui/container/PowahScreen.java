package zeroneye.powah.client.gui.container;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import zeroneye.lib.Lollipop;
import zeroneye.lib.block.TileBase;
import zeroneye.lib.client.gui.ContainerScreenBase;
import zeroneye.lib.client.gui.botton.IconButton;
import zeroneye.lib.client.util.Draw2D;
import zeroneye.lib.inventory.slot.SlotBase;
import zeroneye.powah.Powah;
import zeroneye.powah.block.PowahTile;
import zeroneye.powah.block.generator.GeneratorTile;
import zeroneye.powah.energy.PowahStorage;
import zeroneye.powah.energy.PowerMode;
import zeroneye.powah.energy.RedstoneMode;
import zeroneye.powah.energy.SideConfig;
import zeroneye.powah.inventory.PowahContainer;
import zeroneye.powah.network.packet.NextPowerMode;
import zeroneye.powah.network.packet.NextRedstoneMode;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public abstract class PowahScreen<T extends PowahContainer> extends ContainerScreenBase<T> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/gui/container/energy.png");
    private static final ResourceLocation GUI_CONFIG_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/gui/container/configuration.png");
    protected static final ResourceLocation GUI_WIDGETS_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/gui/widgets.png");
    protected IconButton sideConfigButton = IconButton.EMPTY;
    protected IconButton[] sideButtons = new IconButton[7];
    protected IconButton switchRMButton = IconButton.EMPTY;
    protected PowerMode[] powerModes = new PowerMode[7];
    protected RedstoneMode redstoneMode = RedstoneMode.IGNORE;
    protected SideConfig sideConfig;

    protected final PowahTile tile;

    public PowahScreen(T container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
        this.tile = (PowahTile) container.getInv();
        this.sideConfig = this.tile.getSideConfig();
        this.ySize = 170;
        for (int i = 0; i < 7; i++) {
            this.sideButtons[i] = IconButton.EMPTY;
            this.powerModes[i] = PowerMode.NON;
        }
    }

    @Override
    protected void init() {
        super.init();
        addMainButtons(this.x + 155, this.y + 6, 3);
        addSideConfig(this.x + 114, this.y + 29, 18);
        refresh();
    }

    protected void addMainButtons(int x, int y, int space) {
        this.sideConfigButton = new IconButton(x, y, 15, 15, 0, 0, 15, GUI_WIDGETS_TEXTURE, (button) -> {
            onSideConfig();
            refresh();
        }, this).tooltip("info.powah.configuration", TextFormatting.GRAY);
        addButton(this.sideConfigButton);
        this.redstoneMode = this.tile.getRedstoneMode();
        this.switchRMButton = new IconButton(x, y + space + 15, 15, 15, 15, 0, 15, GUI_WIDGETS_TEXTURE, (button) -> {
            if (this.tile.getWorld() != null) {
                Lollipop.NET.toServer(new NextRedstoneMode(this.tile.getWorld().dimension.getType().getId(), this.tile.getPos()));
            }
            refresh();
        }, this).tooltip(this.redstoneMode.getDisplayName());
        this.switchRMButton.setIconDiff(this.redstoneMode.getIconXUV());
        addButton(this.switchRMButton);
    }

    protected void addSideConfig(int x, int y, int space) {
        for (int i = 0; i < this.sideButtons.length; i++) {
            int x1 = x + (i == 4 ? -space : i == 3 ? space : i == 5 ? space : i == 6 ? space : 0);
            int y1 = y + (i == 0 ? space : i == 3 ? space : i == 1 ? -space : i == 6 ? -space : 0);
            final int finalI = i;
            this.powerModes[i] = i == 6 ? PowerMode.ALL : this.sideConfig.getPowerMode(Direction.byIndex(i));
            int finalI1 = i;
            this.sideButtons[i] = new IconButton(x1, y1, 15, 15, i == 6 ? 0 : 15, i == 6 ? 30 : 0, 15, GUI_WIDGETS_TEXTURE, (button) -> {
                if (this.tile.getWorld() != null) {
                    Lollipop.NET.toServer(new NextPowerMode(finalI, this.tile.getWorld().dimension.getType().getId(), this.tile.getPos()));
                }
                this.powerModes[finalI1] = finalI == 6 ? this.powerModes[finalI1 - 1] : this.sideConfig.getPowerMode(Direction.byIndex(finalI));
                refresh();
            }, this).tooltip("info.lollipop.side." + (i < 6 ? Direction.byIndex(i).getName() : "all"), TextFormatting.GRAY, TextFormatting.DARK_GRAY)
                    .tooltip(this.powerModes[i].getDisplayName());
            this.sideButtons[i].setIconDiff(i == 6 ? 0 : this.sideConfig.getPowerMode(Direction.byIndex(i)).getIconXUV());
            this.sideButtons[i].visible = false;
            addButton(this.sideButtons[i]);
        }
    }

    private void onSideConfig() {
        for (int i = 0; i < this.sideButtons.length; i++) {
            this.sideButtons[i].visible = !this.sideButtons[i].visible;
        }
    }

    @Override
    public void tick() {
        super.tick();
        onRefresh();
    }

    protected void refresh() {
        this.refreshDelay = 3;
    }

    private int refreshDelay;

    private void onRefresh() {
        if (this.refreshDelay > 0) {
            this.sideConfig = this.tile.getSideConfig();
            this.redstoneMode = this.tile.getRedstoneMode();
            this.switchRMButton.setIconDiff(this.redstoneMode.getIconXUV());
            if (this.sideButtons[0].visible) {
                PowerMode powerMode = null;
                boolean flag = false;
                for (int i = 0; i < this.sideButtons.length; i++) {
                    PowerMode prevPowerMode = powerMode;
                    powerMode = this.sideConfig.getPowerMode(Direction.byIndex(i));
                    if (prevPowerMode != null && powerMode != prevPowerMode) {
                        flag = true;
                    }
                    this.powerModes[i] = i == 6 ? this.powerModes[i - 1] : powerMode;
                    this.sideButtons[i].setIconDiff(i == 6 ? 0 : this.sideConfig.getPowerMode(Direction.byIndex(i)).getIconXUV());
                    List<String> list = this.sideButtons[i].getTooltip();
                    list.add(this.powerModes[i].getDisplayName());
                    list.remove(1);

                }
                List<String> list = this.sideButtons[6].getTooltip();
                if (flag && list.size() == 2) {
                    list.remove(1);
                } else if (!flag && list.size() == 1) {
                    list.add(this.powerModes[6].getDisplayName());
                }
            }
            if (this.switchRMButton.isHovered()) {
                List<String> list = this.switchRMButton.getTooltip();
                list.add(this.redstoneMode.getDisplayName());
                list.remove(0);
            }
            this.refreshDelay--;
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.title.getFormattedText();
        int sw = this.font.getStringWidth(s);
        this.font.drawStringWithShadow(s, -(sw / 2) + (this.xSize / 2), -14.0F, 0x777777);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float) (this.ySize - 96 + 3), 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        if (getBackGroundImage() != null) {
            if (!this.sideButtons[0].visible) {
                bindTexture(getSubBackGroundImage());
            } else {
                bindTexture(getConfigBackGroundImage());
            }
            this.blit(this.x + 23, this.y, 0, 0, 153, 72);
        }
        renderEnergyBare();
    }

    public void renderEnergyBare() {
        TileBase inv = this.container.getInv();
        if (inv instanceof PowahTile) {
            PowahStorage storage = ((PowahTile) inv).getInternal();
            int cap = storage.getMaxEnergyStored();
            int stored = storage.getEnergyStored();
            if (cap > 0 && stored > 0) {
                if (getBackGroundImage() != null) {
                    bindTexture(getBackGroundImage());
                    Draw2D.gaugeV(this.x + 4, this.y + 4, 14, 64, 0, 170, cap, stored);
                }
            }
        }
    }

    @Override
    protected boolean hideSlot(Slot slot) {
        return this.sideButtons[0].visible && slot instanceof SlotBase;
    }

    @Override
    protected void renderHoveredToolTip(int mouseX, int mouseY) {
        super.renderHoveredToolTip(mouseX, mouseY);
        if (isMouseOver(mouseX - 3, mouseY - 3, 16, 66)) {
            PowahStorage storage = this.tile.getInternal();
            List<String> list = new ArrayList<>();
            list.add(TextFormatting.GRAY + getTitle().getString());
            list.add(" " + TextFormatting.GRAY + I18n.format("info.powah.stored", "" + TextFormatting.DARK_GRAY + storage.getEnergyStored(), storage.getMaxEnergyStored()));
            if (this.tile instanceof GeneratorTile) {
                list.add(" " + TextFormatting.GRAY + I18n.format("info.powah.generates", "" + TextFormatting.DARK_GRAY + ((GeneratorTile) this.tile).perTick()));
            }
            int maxOut = storage.getMaxReceive();
            int maxIn = storage.getMaxExtract();
            list.add(" " + TextFormatting.GRAY + I18n.format("info.powah.max.io", "" + TextFormatting.DARK_GRAY + (maxIn == maxOut ? maxOut : maxIn == 0 || maxOut == 0 ? Math.max(maxIn, maxOut) : (maxIn + "/" + maxOut))));
            renderTooltip(list, mouseX, mouseY);
        }
    }

    @Nullable
    @Override
    protected ResourceLocation getBackGroundImage() {
        return GUI_TEXTURE;
    }

    protected ResourceLocation getConfigBackGroundImage() {
        return GUI_CONFIG_TEXTURE;
    }

    protected abstract ResourceLocation getSubBackGroundImage();
}
