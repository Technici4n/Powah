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
import owmii.lib.client.screen.EnergyScreen;
import owmii.lib.client.screen.widget.IconButton;
import owmii.lib.energy.Energy;
import owmii.lib.inventory.slot.SlotBase;
import owmii.lib.util.Text;
import owmii.powah.Powah;
import owmii.powah.api.energy.endernetwork.IEnderExtender;
import owmii.powah.block.endercell.EnderCellTile;
import owmii.powah.block.endergate.EnderGateTile;
import owmii.powah.inventory.EnderCellContainer;
import owmii.powah.network.packet.SSetActiveChannel;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class EnderCellScreen extends EnergyScreen<EnderCellTile, EnderCellContainer> {
    public static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/gui/container/ender_cell.png");
    public static final ResourceLocation GUI_CONFIG_TEXTURE = new ResourceLocation(Lollipop.MOD_ID, "textures/gui/container/ender_blank.png");
    public static final ResourceLocation GUI_WIDGETS_TEXTURE = new ResourceLocation(Lollipop.MOD_ID, "textures/gui/container/ender_widget.png");
    protected IconButton[] channelButtons;

    public EnderCellScreen(EnderCellContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
        this.channelButtons = new IconButton[this.te.getTotalChannels()];
    }

    @Override
    protected void init() {
        super.init();
        label:
        for (int l = 0; l < 2; ++l) {
            for (int k = 0; k < 8; ++k) {
                final int fi = k + l * 8;
                if (fi >= this.channelButtons.length) break label;
                this.channelButtons[fi] = new IconButton(this.x + 34 + k * 13, this.y + 32 + l * 13, 12, 12, 0, 64, 12, GUI_WIDGETS_TEXTURE, (button) -> {
                    SSetActiveChannel.send(fi, this.mc.world, this.te.getPos());
                    this.te.setActiveChannel(fi);
                }, this).tooltip("info.powah.channel", TextFormatting.GRAY, "" + TextFormatting.DARK_AQUA + (fi + 1));
                this.channelButtons[fi].setIconDiff(fi == this.te.getActiveChannel() ? 12 : 0);
                addButton(this.channelButtons[fi]);
            }
        }
    }

    @Override
    protected boolean showConfigButton() {
        return !(this.te instanceof EnderGateTile);
    }

    @Override
    protected void refreshScreen() {
        super.refreshScreen();
        for (int i = 0; i < this.channelButtons.length; ++i) {
            this.channelButtons[i].setIconDiff(i == this.te.getActiveChannel() ? 12 : 0);
            this.channelButtons[i].visible = !this.configVisible;
        }
    }

    @Override
    protected void drawForeground(int mouseX, int mouseY) {
        super.drawForeground(mouseX, mouseY);
        if (this.configVisible) return;

        GameProfile gm = this.te.getOwner();
        if (gm != null) {
            this.font.drawStringWithShadow(I18n.format("info.lollipop.owner", gm.getName()), 33, 11, 0x529085);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translatef(18.0F, 19.0F, 0.0F);
        GlStateManager.scalef(0.5F, 0.5F, 1.0F);

        for (int l = 0; l < 2; ++l) {
            for (int k = 0; k < 8; ++k) {
                final int fi = k + l * 8;
                this.font.drawString("" + (fi + 1), (42 + k * 26) - (fi >= 9 ? 3 : 0), 35 + l * 26, fi >= 16 ? 0x224841 : 0x529085);

            }
        }
        GlStateManager.popMatrix();
    }

    @Override
    protected void drawEnergyGauge(int x, int y, Energy energy) {
        super.drawEnergyGauge(x, y, this.te.getSyncEnergy());
    }

    @Override
    public void drawSlot(Slot slot) {
        if (slot instanceof SlotBase && ((SlotBase) slot).isHidden()) return;
        ItemStack stack = slot.getStack();
        if (this.te.isExtender() && stack.getItem() instanceof IEnderExtender && hasShiftDown()) {
            Energy energy = this.te.getSyncEnergy();
            IEnderExtender e = (IEnderExtender) stack.getItem();
            long cap = e.getExtendedCapacity(stack);
            long newCap = energy.getMaxEnergyStored() + cap;
            if (cap <= Energy.MAX && newCap > 0 && newCap <= Energy.MAX) {
                RenderHelper.disableStandardItemLighting();
                bindTexture(GUI_WIDGETS_TEXTURE);
                blit(slot.xPos, slot.yPos, 24, 64, 16, 16);
                RenderHelper.setupGui3DDiffuseLighting();
            }
        }
        super.drawSlot(slot);
    }

    @Override
    public List<String> getTooltipFromItem(ItemStack stack) {
        List<String> tooltip = super.getTooltipFromItem(stack);
        if (this.te.isExtender() && stack.getItem() instanceof IEnderExtender && hasShiftDown()) {
            Energy energy = this.te.getSyncEnergy();
            IEnderExtender e = (IEnderExtender) stack.getItem();
            long cap = e.getExtendedCapacity(stack);
            long newCap = energy.getCapacity() + cap;
            if (cap <= Energy.MAX && newCap > 0 && newCap <= Energy.MAX) {
                List<String> info = new ArrayList<>();
                info.add(TextFormatting.GRAY + I18n.format("info.lollipop.channel", "" + TextFormatting.DARK_AQUA + (this.te.getActiveChannel() + 1)));
                info.add("");
                info.add(TextFormatting.GRAY + I18n.format("info.lollipop.energy.capacity.fe", "" + TextFormatting.DARK_GRAY + Text.addCommas(energy.getCapacity())));
                info.add(TextFormatting.DARK_AQUA + "+ " + Text.addCommas(cap) + " FE");
                info.add(TextFormatting.GRAY + I18n.format("info.powah.new.capacity", "" + TextFormatting.DARK_GRAY + Text.addCommas(newCap)));

                int stored = energy.getEnergyStored();
                int stackStored = Energy.getStored(stack);
                if ((stackStored + stored) > stored) {
                    info.add("");
                    info.add(TextFormatting.GRAY + I18n.format("info.lollipop.energy.fe", "" + TextFormatting.DARK_GRAY + Text.addCommas(stored)));
                    info.add(TextFormatting.DARK_AQUA + "+ " + Text.addCommas(stackStored) + " FE");
                    info.add(TextFormatting.GRAY + I18n.format("info.powah.new.energy", "" + TextFormatting.DARK_GRAY + Text.addCommas(stackStored + stored)));
                }

                info.add("");
                info.add(TextFormatting.DARK_PURPLE + I18n.format("info.powah.shift.to.apply"));
                return info;
            }
        }
        return tooltip;
    }

    @Override
    protected ResourceLocation getMachineBackGround() {
        return GUI_TEXTURE;
    }

    @Override
    protected ResourceLocation getConfigBackGround() {
        return GUI_CONFIG_TEXTURE;
    }

    @Override
    protected ResourceLocation getWidgetTexture() {
        return GUI_WIDGETS_TEXTURE;
    }
}
