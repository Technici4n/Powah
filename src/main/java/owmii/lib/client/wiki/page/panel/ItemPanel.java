package owmii.lib.client.wiki.page.panel;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.lib.client.screen.Texture;
import owmii.lib.client.screen.widget.IconButton;
import owmii.lib.client.screen.wiki.WikiScreen;
import owmii.lib.client.util.MC;
import owmii.lib.client.wiki.Section;
import owmii.lib.registry.IRegistryObject;

import java.util.List;

public class ItemPanel<T extends IItemProvider> extends Panel {
    private final IItemProvider[] items;
    private int currItem;

    @OnlyIn(Dist.CLIENT)
    private IconButton nextItem = IconButton.EMPTY;
    @OnlyIn(Dist.CLIENT)
    private IconButton prevItem = IconButton.EMPTY;

    @SuppressWarnings("unchecked")
    public ItemPanel(Section parent) {
        this((T) parent.getEntry().getStack().getItem(), parent);
    }

    public ItemPanel(T item, Section parent) {
        this(getSiblings(item), parent);
    }

    public ItemPanel(List<T> items, Section parent) {
        this(items.toArray(new IItemProvider[0]), parent);
    }

    public ItemPanel(IItemProvider[] items, Section parent) {
        super("", parent);
        this.items = items;
    }

    @SuppressWarnings("unchecked")
    protected static IItemProvider[] getSiblings(IItemProvider item) {
        if (item.equals(Items.AIR)) {
            return new IItemProvider[0];
        } else {
            if (item instanceof IRegistryObject) {
                IRegistryObject object = (IRegistryObject) item;
                List<IItemProvider> list = Lists.newArrayList(object.getSiblings());
                return list.toArray(new IItemProvider[0]);
            }
        }
        return new IItemProvider[]{item};
    }

    @Override
    @OnlyIn(Dist.CLIENT)
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
    @OnlyIn(Dist.CLIENT)
    public void refresh() {
        super.refresh();
        this.nextItem.visible = this.currItem < this.items.length - 1;
        this.prevItem.visible = this.currItem > 0;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(MatrixStack matrix, int x, int y, int mx, int my, float pt, FontRenderer font, WikiScreen screen) {
        ItemStack stack = new ItemStack(getItem());
        if (Texture.WIKI_BIG_FRM.isMouseOver(x + 161 / 2 - 42 / 2, y + 10, mx, my)) {
            screen.hoveredStack = stack;
        }
        RenderSystem.pushMatrix();
        RenderSystem.translatef(x + 4 + 161 / 2.0F - 42 / 2.0F, y + 4 + 10, 0);
        Texture.WIKI_BIG_FRM.draw(matrix, -4, -4);
        RenderSystem.scaled(2.1, 2.1, 1);
        Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(stack, 0, 0);
        RenderSystem.popMatrix();
        String s = stack.getDisplayName().getString();
        font.drawString(matrix, s, x + 161 / 2.0F - font.getStringWidth(s) / 2.0F, y + 61, 0x2D3F48);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean mouseScrolled(double mouseX, double mouseY, double i) {
        if (i == -1 && this.nextItem.visible) {
            this.nextItem.onPress();
            return true;
        } else if (i == 1 && this.prevItem.visible) {
            this.prevItem.onPress();
            return true;
        }
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onClose() {
        this.currItem = 0;
    }

    public IItemProvider getItem() {
        return this.items[this.currItem].asItem();
    }
}
