package owmii.powah.compat.jei;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ItemLike;

public abstract class AbstractCategory<T> implements IRecipeCategory<T> {
    private final Component title;
    private final IDrawable background;
    private final IDrawable icon;

    public AbstractCategory(IGuiHelper guiHelper, ItemLike iconItemLike, Component title, IDrawable background) {
        this.title = title;
        this.background = background;
        this.icon = guiHelper.createDrawableItemLike(iconItemLike);
    }

    @Override
    public final Component getTitle() {
        return title;
    }

    @Override
    public final IDrawable getBackground() {
        return background;
    }

    @Override
    public final IDrawable getIcon() {
        return icon;
    }
}
