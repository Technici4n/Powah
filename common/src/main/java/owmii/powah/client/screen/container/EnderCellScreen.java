package owmii.powah.client.screen.container;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import owmii.powah.lib.client.screen.Texture;
import owmii.powah.lib.client.screen.container.AbstractEnergyScreen;
import owmii.powah.lib.client.screen.widget.IconButton;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.lib.util.Util;
import owmii.powah.Powah;
import owmii.powah.api.energy.endernetwork.IEnderExtender;
import owmii.powah.block.ender.AbstractEnderTile;
import owmii.powah.client.screen.Textures;
import owmii.powah.inventory.EnderCellContainer;
import owmii.powah.network.Network;
import owmii.powah.network.packet.SetChannelPacket;

public class EnderCellScreen extends AbstractEnergyScreen<AbstractEnderTile<?>, EnderCellContainer> {
    private final IconButton[] iconButtons;

    public EnderCellScreen(EnderCellContainer container, Inventory inv, Component title) {
        super(container, inv, title, Textures.ENDER_CELL);
        this.iconButtons = new IconButton[this.te.getMaxChannels()];
    }

    @Override
    protected void init() {
        super.init();
        for (int i = 0; i < this.iconButtons.length; i++) {
            int channel = i;
            this.iconButtons[i] = addRenderableWidget(new IconButton(this.leftPos + 5 + i * 14, this.topPos + 55, i == this.te.getChannel().get() ? Textures.ENDER_CELL_BTN_0 : Textures.ENDER_CELL_BTN_1, button -> {
                Network.toServer(new SetChannelPacket(this.te.getBlockPos(), channel));
                this.te.getChannel().set(channel);
            }, this));
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
    protected void drawBackground(PoseStack matrix, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(matrix, partialTicks, mouseX, mouseY);
        Textures.ENDER_CELL_GAUGE.drawScalableW(matrix, this.te.getEnergy().subSized(), this.leftPos + 31, this.topPos + 6);
    }

    @Override
    protected void drawForeground(PoseStack matrix, int mouseX, int mouseY) {
        super.drawForeground(matrix, mouseX, mouseY);
        matrix.pushPose();
        RenderSystem.enableBlend();
        int a = (int) (255.0D * 0.45D) << 24;
        Energy e = this.te.getEnergy();
        String s = Util.addCommas(e.getStored()) + "/" + Util.numFormat(e.getCapacity()) + " FE";
        this.font.draw(matrix, s, 38, 13.0F, a + 0x4affde);
        this.font.draw(matrix, Util.numFormat(e.getMaxExtract()) + " FE/t", 38, 27.0F, a + 0x4affde);

        matrix.scale(0.5F, 0.5F, 1.0F);
        for (int i = 1; i < 13; i++) {
            float f = i > 9 ? -2 : 0;
            if (i > 1)
                matrix.translate(14F, 0.0F, 0.0F);
            this.font.draw(matrix, "" + i, 19F + (i * 14) - 14 + f, 119F, i <= this.te.getMaxChannels() ? 0x3e8087 : a + 0x3e8087);
        }

        RenderSystem.disableBlend();
        matrix.popPose();
    }

    @Override
    public void renderSlot(PoseStack matrix, Slot slot) {
        ItemStack stack = slot.getItem();
        if (this.te.isExtender() && stack.getItem() instanceof IEnderExtender && hasShiftDown()) {
            Energy energy = this.te.getEnergy();
            IEnderExtender e = (IEnderExtender) stack.getItem();
            long cap = e.getExtendedCapacity(stack);
            long newCap = energy.getCapacity() + cap;
            if (cap > 0 && cap <= Energy.MAX && newCap > 0 && newCap <= Energy.MAX) {
                Texture.SLOT_HIGHLIGHT_BG.draw(matrix, slot.x, slot.y);
            }
        }
        super.renderSlot(matrix, slot);
    }
}
