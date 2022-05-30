package owmii.lib.client.wiki.page.panel;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.lib.client.screen.Texture;
import owmii.lib.client.screen.widget.IconButton;
import owmii.lib.client.screen.wiki.WikiScreen;
import owmii.lib.client.util.MC;
import owmii.lib.client.wiki.Section;

import java.util.ArrayList;
import java.util.List;

public class CraftingPanel<T extends IItemProvider> extends ItemPanel<T> {
    private int currRecipe;

    @OnlyIn(Dist.CLIENT)
    private IconButton nextRecipe = IconButton.EMPTY;
    @OnlyIn(Dist.CLIENT)
    private IconButton prevRecipe = IconButton.EMPTY;

    public CraftingPanel(Section parent) {
        super(parent);
    }

    public CraftingPanel(T item, Section parent) {
        super(item, parent);
    }

    public CraftingPanel(List<T> items, Section parent) {
        super(items, parent);
    }

    public CraftingPanel(IItemProvider[] items, Section parent) {
        super(items, parent);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void init(int x, int y, WikiScreen screen) {
        super.init(x, y, screen);
        this.nextRecipe = screen.addButton2(new IconButton(x + 144, y + 140, Texture.WIKI_ITM_NEXT, button -> {
            if (this.currRecipe < getRecipe().size() - 1) {
                this.currRecipe = this.currRecipe + 1;
                screen.setPanel(this);
                MC.open(screen);
            }
        }, screen));
        this.prevRecipe = screen.addButton2(new IconButton(x + 6, y + 140, Texture.WIKI_ITM_PREV, button -> {
            if (this.currRecipe > 0) {
                this.currRecipe = this.currRecipe - 1;
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
        this.currRecipe = MathHelper.clamp(this.currRecipe, 0, Math.max(0, getRecipe().size() - 1));
        this.nextRecipe.visible = this.currRecipe < getRecipe().size() - 1;
        this.prevRecipe.visible = this.currRecipe > 0;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(MatrixStack matrix, int x, int y, int mx, int my, float pt, FontRenderer font, WikiScreen screen) {
        super.render(matrix, x, y, mx, my, pt, font, screen);
        if (0 <= this.currRecipe && this.currRecipe < getRecipe().size()) {
            NonNullList<Ingredient> ingredients = NonNullList.withSize(9, Ingredient.EMPTY);
            NonNullList<Ingredient> ingredients1 = getRecipe().get(this.currRecipe).getIngredients();
            for (int i = 0; i < ingredients1.size(); i++) ingredients.set(i, ingredients1.get(i));
            if (!ingredients.isEmpty()) {
                for (int i = 0; i < 3; ++i) {
                    for (int j = 0; j < 3; ++j) {
                        int id = j + i * 3;
                        Ingredient ingredient = ingredients.get(id);
                        ItemStack[] stacks = ingredient.getMatchingStacks();
                        RenderSystem.pushMatrix();
                        RenderSystem.translatef(x + 24 + j * 40, y + 90 + i * 40, 0);
                        Texture.WIKI_RCP_FRM.draw(matrix, 0, 0);
                        if (stacks.length > 0) {
                            boolean b = ingredients1.size() == 4 && id == 2;
                            RenderSystem.translatef(b ? -36 : 4.0F, b ? 44 : 4.0F, 0.0F);
                            RenderSystem.scaled(1.5F, 1.5F, 1.0F);
                            ItemStack stack = stacks[MathHelper.floor(MC.ticks / 20.0F) % stacks.length];
                            if (Texture.WIKI_RCP_FRM.isMouseOver(x + 24 + j * 40, y + 90 + i * 40, mx, my)) {
                                screen.hoveredStack = stack;
                            }
                            Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(stack, 0, 0);
                        }
                        RenderSystem.popMatrix();
                    }
                }
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double i) {
        if (mouseY > 90) {
            if (i == -1 && this.nextRecipe.visible) {
                this.nextRecipe.onPress();
                return true;
            } else if (i == 1 && this.prevRecipe.visible) {
                this.prevRecipe.onPress();
                return true;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, i);
    }

    protected List<IRecipe<?>> getRecipe() {
        return getWiki().getCrafting().getOrDefault(getItem(), new ArrayList<>());
    }
}
