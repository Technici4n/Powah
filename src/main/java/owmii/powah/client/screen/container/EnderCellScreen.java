package owmii.powah.client.screen.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import owmii.lib.client.screen.AbstractEnergyScreen;
import owmii.lib.client.screen.widget.IconButton;
import owmii.lib.logistics.energy.Energy;
import owmii.lib.util.Util;
import owmii.powah.Powah;
import owmii.powah.block.ender.AbstractEnderTile;
import owmii.powah.client.screen.Textures;
import owmii.powah.inventory.EnderCellContainer;
import owmii.powah.network.packet.SetChannelPacket;

public class EnderCellScreen extends AbstractEnergyScreen<AbstractEnderTile<?, ?, ?>, EnderCellContainer> {
    private final IconButton[] iconButtons;

    public EnderCellScreen(EnderCellContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title, Textures.ENDER_CELL);
        this.iconButtons = new IconButton[this.te.getMaxChannels()];
    }

    @Override
    protected void init() {
        super.init();
        for (int i = 0; i < this.iconButtons.length; i++) {
            int channel = i;
            this.iconButtons[i] = addButton(new IconButton(this.guiLeft + 5 + i * 14, this.guiTop + 55, i == this.te.getChannel().get() ? Textures.ENDER_CELL_BTN_0 : Textures.ENDER_CELL_BTN_1, button -> {
                Powah.NET.toServer(new SetChannelPacket(this.te.getPos(), channel));
                this.te.getChannel().set(channel);
            }, this));
        }
    }

    @Override
    public void tick() {
        super.tick();
        for (int i = 0; i < this.iconButtons.length; i++) {
            this.iconButtons[i].setTexture(i == this.te.getChannel().get() ? Textures.ENDER_CELL_BTN_0 : Textures.ENDER_CELL_BTN_1);
        }
    }

    @Override
    protected void drawBackground(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(matrix, partialTicks, mouseX, mouseY);
        Textures.ENDER_CELL_GAUGE.drawScalableW(matrix, this.te.getEnergy().subSized(), this.guiLeft + 31, this.guiTop + 6);
    }

    @Override
    protected void drawForeground(MatrixStack matrix, int mouseX, int mouseY) {
        super.drawForeground(matrix, mouseX, mouseY);
        RenderSystem.pushMatrix();
        RenderSystem.enableBlend();
        int a = (int) (255.0D * 0.45D) << 24;
        Energy e = this.te.getEnergy();
        String s = Util.addCommas(e.getStored()) + "/" + Util.numFormat(e.getCapacity()) + " FE";
        this.font.drawString(matrix, s, 38, 13.0F, a + 0x4affde);
        this.font.drawString(matrix, Util.numFormat(e.getMaxExtract()) + " FE/t", 38, 27.0F, a + 0x4affde);

        RenderSystem.scalef(0.5F, 0.5F, 1.0F);
        for (int i = 1; i < 13; i++) {
            float f = i > 9 ? -2 : 0;
            if (i > 1)
                RenderSystem.translatef(14F, 0.0F, 0.0F);
            this.font.drawString(matrix, "" + i, 19F + (i * 14) - 14 + f, 119F, i <= this.te.getMaxChannels() ? 0x3e8087 : a + 0x3e8087);
        }

        RenderSystem.disableBlend();
        RenderSystem.popMatrix();
    }

//    @Override
//    public void drawSlot(Slot slot) { TODO
//        if (slot instanceof SlotBase && ((SlotBase) slot).isHidden()) return;
//        ItemStack stack = slot.getStack();
//        if (this.te.isExtender() && stack.getItem() instanceof IEnderExtender && hasShiftDown()) {
//            Energy energy = this.te.getEnergy();
//            IEnderExtender e = (IEnderExtender) stack.getItem();
//            long cap = e.getExtendedCapacity(stack);
//            long newCap = energy.getCapacity() + cap;
//            if (cap <= Energy.MAX && newCap > 0 && newCap <= Energy.MAX) {
//                RenderHelper.disableStandardItemLighting();
//                Texture.SLOT_HIGHLIGHT_BG.draw(slot.xPos, slot.yPos);
//                RenderHelper.setupGui3DDiffuseLighting();
//            }
//        }
//        super.drawSlot(slot);
//    }
}
