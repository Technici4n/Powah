package owmii.lib.client.wiki.page.panel;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import owmii.lib.client.screen.Texture;
import owmii.lib.client.screen.widget.IconButton;
import owmii.lib.client.screen.wiki.WikiScreen;
import owmii.lib.client.util.MC;
import owmii.lib.client.wiki.Section;
import owmii.lib.item.IItem;
import owmii.lib.registry.IVariantEntry;
import owmii.lib.registry.VarReg;
import owmii.powah.Powah;

import java.util.List;
import java.util.Objects;

public class ItemPanel<T extends ItemLike> extends Panel {
    private final ItemLike[] items;
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
            var id = item.asItem().getRegistryName();
            if (item instanceof IVariantEntry variantEntry) {
                id = variantEntry.getSiblingsKey(variantEntry);
            }
            return VarReg.getSiblingIds(Objects.requireNonNull(id).getPath()).stream().map(rl -> ForgeRegistries.ITEMS.getValue(Powah.id(rl))).toArray(ItemLike[]::new);
        }
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
    public void render(PoseStack matrix, int x, int y, int mx, int my, float pt, Font font, WikiScreen screen) {
        ItemStack stack = new ItemStack(getItem());
        if (Texture.WIKI_BIG_FRM.isMouseOver(x + 161 / 2 - 42 / 2, y + 10, mx, my)) {
            screen.hoveredStack = stack;
        }
        var globalStack = RenderSystem.getModelViewStack();
        globalStack.pushPose();
        globalStack.translate(x + 4 + 161 / 2.0F - 42 / 2.0F, y + 4 + 10, 0);
        RenderSystem.applyModelViewMatrix();
        Texture.WIKI_BIG_FRM.draw(matrix, -4, -4);
        globalStack.scale(2.1f, 2.1f, 1);
        Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(stack, 0, 0);
        globalStack.popPose();
        RenderSystem.applyModelViewMatrix();
        String s = stack.getHoverName().getString();
        font.draw(matrix, s, x + 161 / 2.0F - font.width(s) / 2.0F, y + 61, 0x2D3F48);
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

    public ItemLike getItem() {
        return this.items[this.currItem].asItem();
    }
}
