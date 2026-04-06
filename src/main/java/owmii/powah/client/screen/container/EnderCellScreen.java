package owmii.powah.client.screen.container;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import owmii.powah.api.energy.endernetwork.IEnderExtender;
import owmii.powah.block.ender.PowahBaseEnderBlockEntity;
import owmii.powah.client.screen.Textures;
import owmii.powah.inventory.EnderCellMenu;
import owmii.powah.lib.client.screen.Texture;
import owmii.powah.lib.client.screen.container.PowahBaseEnergyScreen;
import owmii.powah.lib.client.screen.widget.IconButton;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.network.Network;
import owmii.powah.network.packet.SetChannelPacket;
import owmii.powah.util.Util;

public class EnderCellScreen extends PowahBaseEnergyScreen<PowahBaseEnderBlockEntity<?>, EnderCellMenu> {
    private final IconButton[] iconButtons;

    public EnderCellScreen(EnderCellMenu container, Inventory inv, Component title) {
        super(container, inv, title, Textures.ENDER_CELL);
        this.iconButtons = new IconButton[this.te.getMaxChannels()];
    }

    @Override
    protected void init() {
        super.init();
        for (int i = 0; i < this.iconButtons.length; i++) {
            int channel = i;
            this.iconButtons[i] = addRenderableWidget(new IconButton(this.leftPos + 5 + i * 14, this.topPos + 55,
                    i == this.te.getChannel().get() ? Textures.ENDER_CELL_BTN_0 : Textures.ENDER_CELL_BTN_1, button -> {
                        Network.toServer(new SetChannelPacket(this.te.getBlockPos(), channel));
                        this.te.getChannel().set(channel);
                    }));
        }
    }

    @Override
    public void containerTick() {
        super.containerTick();
        for (int i = 0; i < this.iconButtons.length; i++) {
            this.iconButtons[i].setTexture(i == this.te.getChannel().get() ? Textures.ENDER_CELL_BTN_0 : Textures.ENDER_CELL_BTN_1);
        }
    }

    @Override
    protected void drawBackground(GuiGraphicsExtractor guiGraphics, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(guiGraphics, partialTicks, mouseX, mouseY);
        Textures.ENDER_CELL_GAUGE.drawScalableW(guiGraphics, this.te.getEnergy().subSized(), this.leftPos + 31, this.topPos + 6);
    }

    @Override
    protected void drawForeground(GuiGraphicsExtractor gui, int mouseX, int mouseY) {
        super.drawForeground(gui, mouseX, mouseY);
        gui.pose().pushMatrix();
        Energy e = this.te.getEnergy();
        String s = Util.addCommas(e.getStored()) + "/" + Util.numFormat(e.getCapacity()) + " FE";
        gui.text(this.font, s, 38, 13, ARGB.color(0.4f, 0x4affde), false);
        gui.text(this.font, Util.numFormat(e.getMaxExtract()) + " FE/t", 38, 27, ARGB.color(0.4f, 0x4affde), false);

        gui.pose().scale(0.5F, 0.5F);
        for (int i = 1; i < 13; i++) {
            var f = i > 9 ? -2 : 0;
            if (i > 1)
                gui.pose().translate(14F, 0.0F);
            gui.text(this.font, "" + i, 19 + (i * 14) - 14 + f, 119, i <= this.te.getMaxChannels() ? 0xff3e8087 : ARGB.color(0.4f, 0x3e8087),
                    false);
        }

        gui.pose().popMatrix();
    }

    @Override
    public void extractSlot(GuiGraphicsExtractor matrix, Slot slot, int mouseX, int mouseY) {
        ItemStack stack = slot.getItem();
        if (this.te.isExtender() && stack.getItem() instanceof IEnderExtender && minecraft.hasShiftDown()) {
            Energy energy = this.te.getEnergy();
            IEnderExtender e = (IEnderExtender) stack.getItem();
            long cap = e.getExtendedCapacity(stack);
            long newCap = energy.getCapacity() + cap;
            if (cap > 0 && cap <= Energy.MAX && newCap > 0 && newCap <= Energy.MAX) {
                Texture.SLOT_HIGHLIGHT_BG.draw(matrix, slot.x, slot.y);
            }
        }
        super.extractSlot(matrix, slot, mouseX, mouseY);
    }
}
