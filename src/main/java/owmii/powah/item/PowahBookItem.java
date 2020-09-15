package owmii.powah.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import owmii.lib.item.ItemBase;
import owmii.powah.client.screen.Screens;

public class PowahBookItem extends ItemBase {
    public PowahBookItem(Properties properties) {
        super(properties.rarity(Rarity.UNCOMMON));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (worldIn.isRemote) {
            Screens.openManualScreen();
//            RecipeManager manager = Minecraft.getInstance().world.getRecipeManager();
//            System.out.println(manager.getRecipes(IRecipeType.CRAFTING).values().stream().filter(recipe -> recipe.getRecipeOutput().getItem() == Itms.DIELECTRIC_PASTE)
//                    .collect(Collectors.toList()));

        }
        return ActionResult.resultSuccess(stack);
    }
}
