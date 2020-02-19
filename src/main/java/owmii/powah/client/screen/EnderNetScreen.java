package owmii.powah.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.lib.client.screen.widget.IconButton;
import owmii.lib.util.Data;
import owmii.powah.client.screen.inventory.EnderCellScreen;
import owmii.powah.network.packet.SSetActiveChannelItem;

@OnlyIn(Dist.CLIENT)
public class EnderNetScreen extends Screen {
    protected final CompoundNBT nbt;
    protected IconButton[] channelButtons;
    public int x, y, w = 107, h = 29;

    private int channels;
    private int activeChannel;

    public EnderNetScreen(ItemStack stack, int totalChannels) {
        super(new StringTextComponent(""));
        this.nbt = stack.getOrCreateChildTag(Data.TAG_TE_STORABLE);
        this.channels = totalChannels;
        this.activeChannel = this.nbt.getInt("ActiveChannel");
        this.channelButtons = new IconButton[this.channels];
    }

    @Override
    protected void init() {
        super.init();
        this.x = (this.width - this.w) / 2;
        this.y = (this.height - this.h) / 2;
        label:
        for (int l = 0; l < 2; ++l) {
            for (int k = 0; k < 8; ++k) {
                final int fi = k + l * 8;
                if (fi >= this.channelButtons.length) break label;
                this.channelButtons[fi] = new IconButton(this.x + 2 + k * 13, this.y + 2 + l * 13, 12, 12, 0, 64, 12, EnderCellScreen.GUI_WIDGETS_TEXTURE, (button) -> {
                    SSetActiveChannelItem.send(fi);
                    this.activeChannel = fi;
                }, this).tooltip("info.powah.channel", TextFormatting.GRAY, "" + TextFormatting.DARK_AQUA + (fi + 1));
                this.channelButtons[fi].setIconDiff(fi == this.activeChannel ? 12 : 0);
                addButton(this.channelButtons[fi]);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        for (int i = 0; i < this.channelButtons.length; ++i) {
            this.channelButtons[i].setIconDiff(i == this.activeChannel ? 12 : 0);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        Minecraft.getInstance().textureManager.bindTexture(EnderCellScreen.GUI_TEXTURE);
        this.blit(this.x, this.y, 32, 30, this.w, this.h);

        GlStateManager.pushMatrix();
        GlStateManager.translatef(this.x - 14.0F, this.y - 11.0F, 0.0F);
        GlStateManager.scalef(0.5F, 0.5F, 1.0F);

        for (int l = 0; l < 2; ++l) {
            for (int k = 0; k < 8; ++k) {
                final int fi = k + l * 8;
                this.font.drawString("" + (fi + 1), (42 + k * 26) - (fi >= 9 ? 3 : 0), 35 + l * 26, fi >= this.channels ? 0x224841 : 0x529085);

            }
        }

        GlStateManager.popMatrix();
        super.render(mouseX, mouseY, partialTicks);
        for (Widget widget : this.buttons) {
            if (widget.isHovered()) {
                widget.renderToolTip(mouseX, mouseY + 20);
                break;
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(int i, int i1, int i2) {
        if (super.keyPressed(i, i1, i2)) {
            return true;
        } else {
            InputMappings.Input mouseKey = InputMappings.getInputByCode(i, i1);
            if (i == 256 || Minecraft.getInstance().gameSettings.keyBindInventory.isActiveAndMatches(mouseKey)) {
                Minecraft.getInstance().displayGuiScreen(null);
                return true;
            }
        }
        return false;
    }
}
