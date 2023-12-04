package owmii.powah.lib.client.wiki.page.panel;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import owmii.powah.Powah;
import owmii.powah.lib.client.screen.Texture;
import owmii.powah.lib.client.screen.widget.IconButton;
import owmii.powah.lib.client.screen.wiki.WikiScreen;
import owmii.powah.lib.client.util.MC;
import owmii.powah.lib.client.wiki.Section;
import owmii.powah.lib.registry.IVariantEntry;
import owmii.powah.lib.registry.VarReg;

public class ItemPanel<T extends ItemLike> extends Panel {
    private final ItemLike[] items;
    private int currItem;

    private IconButton nextItem = IconButton.EMPTY;
    private IconButton prevItem = IconButton.EMPTY;

    @SuppressWarnings("unchecked")
    public ItemPanel(Section parent) {
        this((T) parent.getEntry().getStack().getItem(), parent);
    }

    public ItemPanel(T item, Section parent) {
        this(getSiblings(item), parent);
    }

    public ItemPanel(List<T> items, Section parent) {
        this(items.toArray(new ItemLike[0]), parent);
    }

    public ItemPanel(ItemLike[] items, Section parent) {
        super("", parent);
        this.items = items;
    }

    protected static ItemLike[] getSiblings(ItemLike item) {
        if (item.equals(Items.AIR)) {
            return new ItemLike[0];
        } else {
            var id = BuiltInRegistries.ITEM.getKey(item.asItem());
            if (item instanceof IVariantEntry variantEntry) {
                id = new ResourceLocation(id.getNamespace(), id.getPath().replace("_" + variantEntry.getVariant().getName(), ""));
            }
            return VarReg.getSiblingIds(Objects.requireNonNull(id).getPath()).stream().map(rl -> BuiltInRegistries.ITEM.get(Powah.id(rl)))
                    .toArray(ItemLike[]::new);
        }
    }

    @Override
    public void init(int x, int y, WikiScreen screen) {
        super.init(x, y, screen);
        this.nextItem = screen.addButton2(new IconButton(x + 139 - 28, y + 26, Texture.WIKI_ITM_NEXT, button -> {
            if (this.currItem < this.items.length - 1) {
                this.currItem = this.currItem + 1;
                screen.setPanel(this);
                MC.open(screen);
            }
        }, screen));
        this.prevItem = screen.addButton2(new IconButton(x + 69 - 28, y + 26, Texture.WIKI_ITM_PREV, button -> {
            if (this.currItem > 0) {
                this.currItem = this.currItem - 1;
                screen.setPanel(this);
                MC.open(screen);
            }
        }, screen));
        refresh();
    }

    @Override
    public void refresh() {
        super.refresh();
        this.nextItem.visible = this.currItem < this.items.length - 1;
        this.prevItem.visible = this.currItem > 0;
    }

    @Override
    public void render(GuiGraphics gui, int x, int y, int mx, int my, float pt, Font font, WikiScreen screen) {
        ItemStack stack = new ItemStack(getItem());
        if (Texture.WIKI_BIG_FRM.isMouseOver(x + 161 / 2 - 42 / 2, y + 10, mx, my)) {
            screen.hoveredStack = stack;
        }
        var poseStack = gui.pose();
        poseStack.pushPose();
        poseStack.translate(x + 4 + 161 / 2.0F - 42 / 2.0F, y + 4 + 10, 0);
        RenderSystem.applyModelViewMatrix();
        Texture.WIKI_BIG_FRM.draw(gui, -4, -4);
        poseStack.scale(2.1f, 2.1f, 1);
        gui.renderFakeItem(stack, 0, 0);
        gui.renderItemDecorations(font, stack, 0, 0);
        poseStack.popPose();
        RenderSystem.applyModelViewMatrix();
        String s = stack.getHoverName().getString();
        gui.drawString(font, s, Math.round(x + 161 / 2.0F - font.width(s) / 2.0F), y + 61, 0x2D3F48, false);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
        if (deltaY == -1 && this.nextItem.visible) {
            this.nextItem.onPress();
            return true;
        } else if (deltaY == 1 && this.prevItem.visible) {
            this.prevItem.onPress();
            return true;
        }
        return true;
    }

    @Override
    public void onClose() {
        this.currItem = 0;
    }

    public ItemLike getItem() {
        return this.items[this.currItem].asItem();
    }
}
