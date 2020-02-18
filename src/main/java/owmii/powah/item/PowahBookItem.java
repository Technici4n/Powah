package owmii.powah.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import owmii.lib.item.ItemBase;
import owmii.powah.client.screen.book.BookScreen;

public class PowahBookItem extends ItemBase {
    public PowahBookItem(Properties properties) {
        super(properties.rarity(Rarity.UNCOMMON));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (worldIn.isRemote) {
            BookScreen.open();
        }
        return ActionResult.resultSuccess(stack);
    }
}
