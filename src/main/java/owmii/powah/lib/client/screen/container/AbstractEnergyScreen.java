package owmii.powah.lib.client.screen.container;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import org.apache.commons.lang3.tuple.Pair;
import owmii.powah.lib.Lollipop;
import owmii.powah.lib.block.AbstractEnergyStorage;
import owmii.powah.lib.block.IInventoryHolder;
import owmii.powah.lib.client.screen.Texture;
import owmii.powah.lib.client.screen.widget.IconButton;
import owmii.powah.lib.client.util.Text;
import owmii.powah.lib.logistics.inventory.AbstractEnergyContainer;
import owmii.powah.lib.network.packets.NextEnergyConfigPacket;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;

public class AbstractEnergyScreen<T extends AbstractEnergyStorage<?, ?, ?> & IInventoryHolder, C extends AbstractEnergyContainer<T>> extends AbstractTileScreen<T, C> {
    protected IconButton[] configButtons = new IconButton[6];
    protected IconButton configButtonAll = IconButton.EMPTY;

    public AbstractEnergyScreen(C container, Inventory inv, Component title, Texture backGround) {
        super(container, inv, title, backGround);
    }

    @Override
    protected void init() {
        super.init();
        if (hasConfigButtons()) {
            addSideConfigButtons(0, 4);
        }
        if (hasRedstoneButton()) {
            addRedstoneButton(0, 31);
        }
    }

    protected void addSideConfigButtons(int x, int y) {
        for (int i = 0; i < 6; i++) {
            final int id = i;
            Pair<Integer, Integer> offset = getSideButtonOffsets(6).get(i);
            int xOffset = offset.getLeft();
            int yOffset = offset.getRight();
            Direction side = Direction.from3DDataValue(i);
            this.configButtons[i] = addRenderableWidget(new IconButton(this.leftPos + xOffset + this.imageWidth + x + 8, this.topPos + yOffset + y + 10, Texture.CONFIG.get(this.te.getSideConfig().getType(side)), button -> {
                Lollipop.NET.toServer(new NextEnergyConfigPacket(id, this.te.getBlockPos()));
                this.te.getSideConfig().nextType(side);
            }, this).setTooltip(tooltip -> {
                tooltip.add(new TranslatableComponent("info.lollipop.facing").append(Text.COLON).withStyle(ChatFormatting.GRAY)
                        .append(new TranslatableComponent("info.lollipop.side." + side.getSerializedName()).withStyle(ChatFormatting.DARK_GRAY)));
                tooltip.add(this.te.getSideConfig().getType(side).getDisplayName());
            }));
        }

        this.configButtonAll = addRenderableWidget(new IconButton(this.leftPos + this.imageWidth + x + 14, this.topPos + y + 4, Texture.CONFIG_BTN, button -> {
            Lollipop.NET.toServer(new NextEnergyConfigPacket(6, this.te.getBlockPos()));
            this.te.getSideConfig().nextTypeAll();
        }, this).setTooltip(tooltip -> {
            tooltip.add(new TranslatableComponent("info.lollipop.facing").append(Text.COLON).withStyle(ChatFormatting.GRAY)
                    .append(new TranslatableComponent("info.lollipop.all").withStyle(ChatFormatting.DARK_GRAY)));
            tooltip.add(this.te.getSideConfig().getType(Direction.UP).getDisplayName());
        }));
    }

    @Override
    public void containerTick() {
        super.containerTick();
        if (hasConfigButtons()) {
            for (int i = 0; i < 6; i++) {
                this.configButtons[i].setTexture(Texture.CONFIG.get(this.te.getSideConfig().getType(Direction.from3DDataValue(i))));
            }
        }
    }

    protected List<Pair<Integer, Integer>> getSideButtonOffsets(int spacing) {
        return Lists.newArrayList(Pair.of(0, spacing), Pair.of(0, -spacing), Pair.of(0, 0), Pair.of(spacing, spacing), Pair.of(-spacing, 0), Pair.of(spacing, 0));
    }

    @Override
    protected void drawBackground(PoseStack matrix, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(matrix, partialTicks, mouseX, mouseY);
        if (hasConfigButtons()) {
            Texture.CONFIG_BTN_BG.draw(matrix, this.configButtons[1].x - 8, this.configButtons[1].y - 4);
        }
    }

    @Override
    public List<Rect2i> getExtraAreas() {
        final List<Rect2i> extraAreas = super.getExtraAreas();
        if (hasConfigButtons()) {
            extraAreas.add(toRectangle2d(this.configButtons[1].x - 8, this.configButtons[1].y - 4, Texture.CONFIG_BTN_BG));
        }
        return extraAreas;
    }

    protected boolean hasConfigButtons() {
        return true;
    }

    protected boolean hasRedstoneButton() {
        return true;
    }
}
