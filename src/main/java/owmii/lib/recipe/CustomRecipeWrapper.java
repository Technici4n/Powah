package owmii.lib.recipe;

import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import owmii.lib.block.AbstractTileEntity;

public class CustomRecipeWrapper extends RecipeWrapper {
    private final AbstractTileEntity<?, ?> tile;

    public CustomRecipeWrapper(IItemHandlerModifiable inv, AbstractTileEntity<?, ?> tile) {
        super(inv);
        this.tile = tile;
    }

    public AbstractTileEntity<?, ?> getTile() {
        return this.tile;
    }
}
