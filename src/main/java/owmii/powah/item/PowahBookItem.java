package owmii.powah.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import owmii.lib.item.ItemBase;
import owmii.powah.client.screen.Screens;

public class PowahBookItem extends ItemBase {
    public PowahBookItem(Properties properties) {
        super(properties.rarity(Rarity.UNCOMMON));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        if (worldIn.isClientSide) {
            Screens.openManualScreen();
//            RecipeManager manager = Minecraft.getInstance().world.getRecipeManager();
//            System.out.println(manager.getRecipes(IRecipeType.CRAFTING).values().stream().filter(recipe -> recipe.getRecipeOutput().getItem() == Itms.DIELECTRIC_PASTE.get())
//                    .collect(Collectors.toList()));

        }
        return InteractionResultHolder.success(stack);
    }
}
