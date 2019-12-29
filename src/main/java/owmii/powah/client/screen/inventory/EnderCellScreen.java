package owmii.powah.client.screen.inventory;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.lib.Lollipop;
import owmii.lib.client.screen.botton.IconButton;
import owmii.lib.util.Energy;
import owmii.lib.util.Text;
import owmii.powah.Powah;
import owmii.powah.block.PowahBlock;
import owmii.powah.block.endercell.EnderCellTile;
import owmii.powah.block.energycell.EnergyCellBlock;
import owmii.powah.energy.PowahStorage;
import owmii.powah.inventory.EnderCellContainer;
import owmii.powah.item.PowahBlockItem;
import owmii.powah.network.packet.SetActiveChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class EnderCellScreen extends PowahScreen<EnderCellContainer> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/gui/container/ender_cell.png");
    private static final ResourceLocation GUI_CONFIG_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/gui/container/ender_configuration.png");
    private static final ResourceLocation GUI_WIDGETS_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/gui/ender_widgets.png");

    protected IconButton[] channelButtons;

    private final EnderCellTile cell;

    public EnderCellScreen(EnderCellContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
        this.cell = container.getTile();
        this.channelButtons = new IconButton[this.cell.getChannels()];
    }

    @Override
    protected void init() {
        super.init();
        label:
        for (int l = 0; l < 2; ++l) {
            for (int k = 0; k < 8; ++k) {
                final int fi = k + l * 8;
                if (fi >= this.channelButtons.length) break label;
                this.channelButtons[fi] = new IconButton(this.x + 38 + k * 13, this.y + 32 + l * 13, 12, 12, 0, 60, 12, GUI_WIDGETS_TEXTURE, (button) -> {
                    Lollipop.NET.toServer(new SetActiveChannel(fi, this.world.dimension.getType().getId(), this.cell.getPos()));
                    this.cell.setActiveChannel(fi);
                    refresh();
                }, this).tooltip("info.powah.channel", TextFormatting.GRAY, "" + TextFormatting.DARK_AQUA + (fi + 1));
                this.channelButtons[fi].setIconDiff(fi == this.cell.getActiveChannel() ? 12 : 0);
                addButton(this.channelButtons[fi]);
            }
        }
    }

    @Override
    protected void onRefresh() {
        super.onRefresh();
        onRefreshDelayed();
    }

    @Override
    protected void onRefreshDelayed() {
        for (int i = 0; i < this.channelButtons.length; ++i) {
            this.channelButtons[i].setIconDiff(i == this.cell.getActiveChannel() ? 12 : 0);
            this.channelButtons[i].visible = !this.sideButtons[0].visible;
        }
    }

    @Override
    protected void drawForeground(int mouseX, int mouseY) {
        super.drawForeground(mouseX, mouseY);
        if (this.sideButtons[0].visible) return;

        GameProfile gm = this.cell.getOwner();
        if (gm != null) {
            this.font.drawStringWithShadow(I18n.format("info.powah.owner", gm.getName()), 38, 11, 0x529085);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translatef(22.0F, 19.0F, 0.0F);
        GlStateManager.scalef(0.5F, 0.5F, 1.0F);

        for (int l = 0; l < 2; ++l) {
            for (int k = 0; k < 8; ++k) {
                final int fi = k + l * 8;
                this.font.drawString("" + (fi + 1), (42 + k * 26) - (fi >= 9 ? 3 : 0), 35 + l * 26, fi >= this.cell.getChannels() ? 0x224841 : 0x529085);

            }
        }
        GlStateManager.popMatrix();
    }

    @Override
    public void drawSlot(Slot slotIn) {
        ItemStack stack = slotIn.getStack();
        if (!stack.isEmpty() && slotIn.isEnabled() && hasShiftDown() && this.cell.isExtender()) {
            if (Objects.requireNonNull(stack.getItem().getRegistryName()).toString().startsWith("powah:energy_cell")) {
                PowahBlockItem item = (PowahBlockItem) stack.getItem();
                PowahBlock block = item.getBlock();
                if (block instanceof EnergyCellBlock) {
                    PowahStorage storage = this.cell.getInternal();
                    int cap = block.getCapacity();
                    int newCap = this.cell.internal.getMaxEnergyStored() + cap;
                    int maxCap = 2_000_000_000;
                    if (cap <= maxCap && newCap <= maxCap) {
                        RenderHelper.disableStandardItemLighting();
                        bindTexture(GUI_WIDGETS_TEXTURE);
                        blit(slotIn.xPos, slotIn.yPos, 120, 0, 16, 16);
                        RenderHelper.enableGUIStandardItemLighting();
                    }
                }
            }
        }

        super.drawSlot(slotIn);
    }

    @Override
    public List<String> getTooltipFromItem(ItemStack stack) {
        List<String> tooltip = super.getTooltipFromItem(stack);
        if (hasShiftDown() && this.cell.isExtender()) {
            if (Objects.requireNonNull(stack.getItem().getRegistryName()).toString().startsWith("powah:energy_cell")) {
                PowahBlockItem item = (PowahBlockItem) stack.getItem();
                PowahBlock block = item.getBlock();
                if (block instanceof EnergyCellBlock) {
                    PowahStorage storage = this.cell.getInternal();
                    int cap = block.getCapacity();
                    int newCap = this.cell.internal.getMaxEnergyStored() + cap;
                    int maxCap = 2_000_000_000;
                    if (cap <= maxCap && newCap <= maxCap) {
                        List<String> info = new ArrayList<>();
                        info.add(TextFormatting.GRAY + I18n.format("info.powah.channel", "" + TextFormatting.DARK_AQUA + (this.cell.getActiveChannel() + 1)));
                        info.add("");
                        info.add(TextFormatting.GRAY + I18n.format("info.powah.capacity", "" + TextFormatting.DARK_GRAY + Text.addCommas(this.cell.internal.getMaxEnergyStored())));
                        info.add(TextFormatting.DARK_AQUA + "+ " + Text.addCommas(cap) + " FE");
                        info.add(TextFormatting.GRAY + I18n.format("info.powah.new.capacity", "" + TextFormatting.DARK_GRAY + Text.addCommas(newCap)));

                        int stored = this.cell.internal.getEnergyStored();
                        int stackStored = Energy.getStored(stack);
                        if ((stackStored + stored) > stored) {
                            info.add("");
                            info.add(TextFormatting.GRAY + I18n.format("info.powah.energy.2", "" + TextFormatting.DARK_GRAY + Text.addCommas(stored)));
                            info.add(TextFormatting.DARK_AQUA + "+ " + Text.addCommas(stackStored) + " FE");
                            info.add(TextFormatting.GRAY + I18n.format("info.powah.new.energy", "" + TextFormatting.DARK_GRAY + Text.addCommas(stackStored + stored)));
                        }

                        info.add("");
                        info.add(TextFormatting.DARK_PURPLE + I18n.format("info.powah.shift.to.apply"));
                        return info;
                    }
                }
            }
        }
        return tooltip;
    }

    @Override
    public void renderEnergyBare(int cap, int stored) {
        super.renderEnergyBare(this.cell.internal.getMaxEnergyStored(), this.cell.internal.getEnergyStored());
    }

    @Override
    protected void renderEnergyTooltip(int mouseX, int mouseY, int cap, int stored, int out, int in) {
        super.renderEnergyTooltip(mouseX, mouseY, this.cell.internal.getMaxEnergyStored(), this.cell.internal.getEnergyStored(), out, in);
    }

    @Override
    protected ResourceLocation getSubBackGroundImage() {
        return GUI_TEXTURE;
    }

    @Override
    protected ResourceLocation getConfigBackGroundImage() {
        return GUI_CONFIG_TEXTURE;
    }

    @Override
    protected ResourceLocation getGuiWidgetsTexture() {
        return GUI_WIDGETS_TEXTURE;
    }
}
