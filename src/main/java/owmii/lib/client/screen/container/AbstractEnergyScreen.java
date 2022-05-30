package owmii.lib.client.screen.container;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.tuple.Pair;
import owmii.lib.Lollipop;
import owmii.lib.block.AbstractEnergyStorage;
import owmii.lib.block.IInventoryHolder;
import owmii.lib.client.screen.Texture;
import owmii.lib.client.screen.widget.IconButton;
import owmii.lib.client.util.Text;
import owmii.lib.logistics.inventory.AbstractEnergyContainer;
import owmii.lib.network.packets.NextEnergyConfigPacket;

import java.util.List;

public class AbstractEnergyScreen<T extends AbstractEnergyStorage<?, ?, ?> & IInventoryHolder, C extends AbstractEnergyContainer<T>> extends AbstractTileScreen<T, C> {
    protected IconButton[] configButtons = new IconButton[6];
    protected IconButton configButtonAll = IconButton.EMPTY;

    public AbstractEnergyScreen(C container, PlayerInventory inv, ITextComponent title, Texture backGround) {
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
            Direction side = Direction.byIndex(i);
            this.configButtons[i] = addButton(new IconButton(this.guiLeft + xOffset + this.xSize + x + 8, this.guiTop + yOffset + y + 10, Texture.CONFIG.get(this.te.getSideConfig().getType(side)), button -> {
                Lollipop.NET.toServer(new NextEnergyConfigPacket(id, this.te.getPos()));
                this.te.getSideConfig().nextType(side);
            }, this).setTooltip(tooltip -> {
                tooltip.add(new TranslationTextComponent("info.lollipop.facing").append(Text.COLON).mergeStyle(TextFormatting.GRAY)
                        .append(new TranslationTextComponent("info.lollipop.side." + side.getString()).mergeStyle(TextFormatting.DARK_GRAY)));
                tooltip.add(this.te.getSideConfig().getType(side).getDisplayName());
            }));
        }

        this.configButtonAll = addButton(new IconButton(this.guiLeft + this.xSize + x + 14, this.guiTop + y + 4, Texture.CONFIG_BTN, button -> {
            Lollipop.NET.toServer(new NextEnergyConfigPacket(6, this.te.getPos()));
            this.te.getSideConfig().nextTypeAll();
        }, this).setTooltip(tooltip -> {
            tooltip.add(new TranslationTextComponent("info.lollipop.facing").append(Text.COLON).mergeStyle(TextFormatting.GRAY)
                    .append(new TranslationTextComponent("info.lollipop.all").mergeStyle(TextFormatting.DARK_GRAY)));
            tooltip.add(this.te.getSideConfig().getType(Direction.UP).getDisplayName());
        }));
    }

    @Override
    public void tick() {
        super.tick();
        if (hasConfigButtons()) {
            for (int i = 0; i < 6; i++) {
                this.configButtons[i].setTexture(Texture.CONFIG.get(this.te.getSideConfig().getType(Direction.byIndex(i))));
            }
        }
    }

    protected List<Pair<Integer, Integer>> getSideButtonOffsets(int spacing) {
        return Lists.newArrayList(Pair.of(0, spacing), Pair.of(0, -spacing), Pair.of(0, 0), Pair.of(spacing, spacing), Pair.of(-spacing, 0), Pair.of(spacing, 0));
    }

    @Override
    protected void drawBackground(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(matrix, partialTicks, mouseX, mouseY);
        if (hasConfigButtons()) {
            Texture.CONFIG_BTN_BG.draw(matrix, this.configButtons[1].x - 8, this.configButtons[1].y - 4);
        }
    }

    @Override
    public List<Rectangle2d> getExtraAreas() {
        final List<Rectangle2d> extraAreas = super.getExtraAreas();
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
