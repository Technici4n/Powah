package owmii.powah.lib.client.wiki.page.panel;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import owmii.powah.lib.client.screen.Texture;
import owmii.powah.lib.client.screen.widget.IconButton;
import owmii.powah.lib.client.screen.wiki.WikiScreen;
import owmii.powah.lib.client.util.MC;
import owmii.powah.lib.client.wiki.Section;

public class CraftingPanel<T extends ItemLike> extends ItemPanel<T> {
    private int currRecipe;

    private IconButton nextRecipe = IconButton.EMPTY;
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

    public CraftingPanel(ItemLike[] items, Section parent) {
        super(items, parent);
    }

    @Override
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
    public void refresh() {
        super.refresh();
        this.currRecipe = Mth.clamp(this.currRecipe, 0, Math.max(0, getRecipe().size() - 1));
        this.nextRecipe.visible = this.currRecipe < getRecipe().size() - 1;
        this.prevRecipe.visible = this.currRecipe > 0;
    }

    @Override
    public void render(PoseStack matrix, int x, int y, int mx, int my, float pt, Font font, WikiScreen screen) {
        super.render(matrix, x, y, mx, my, pt, font, screen);
        if (0 <= this.currRecipe && this.currRecipe < getRecipe().size()) {
            NonNullList<Ingredient> ingredients = NonNullList.withSize(9, Ingredient.EMPTY);
            NonNullList<Ingredient> ingredients1 = getRecipe().get(this.currRecipe).getIngredients();
            for (int i = 0; i < ingredients1.size(); i++)
                ingredients.set(i, ingredients1.get(i));
            if (!ingredients.isEmpty()) {
                for (int i = 0; i < 3; ++i) {
                    for (int j = 0; j < 3; ++j) {
                        int id = j + i * 3;
                        Ingredient ingredient = ingredients.get(id);
                        ItemStack[] stacks = ingredient.getItems();

                        var globalStack = RenderSystem.getModelViewStack();
                        globalStack.pushPose();
                        matrix.pushPose();

                        globalStack.translate(x + 24 + j * 40, y + 90 + i * 40, 0);
                        matrix.translate(x + 24 + j * 40, y + 90 + i * 40, 0);

                        Texture.WIKI_RCP_FRM.draw(matrix, 0, 0);
                        if (stacks.length > 0) {
                            boolean b = ingredients1.size() == 4 && id == 2;
                            globalStack.translate(b ? -36 : 4.0F, b ? 44 : 4.0F, 0.0F);
                            globalStack.scale(1.5F, 1.5F, 1.0F);
                            ItemStack stack = stacks[Mth.floor(MC.ticks / 20.0F) % stacks.length];
                            if (Texture.WIKI_RCP_FRM.isMouseOver(x + 24 + j * 40, y + 90 + i * 40, mx, my)) {
                                screen.hoveredStack = stack;
                            }
                            Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(stack, 0, 0);
                        }

                        globalStack.popPose();
                        matrix.popPose();
                        RenderSystem.applyModelViewMatrix();
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

    protected List<Recipe<?>> getRecipe() {
        return getWiki().getCrafting().getOrDefault(getItem(), new ArrayList<>());
    }
}
