package owmii.powah.item;

import guideme.GuidesCommon;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import owmii.powah.Powah;
import owmii.powah.lib.item.ItemBase;

public class PowahBookItem extends ItemBase {
    public static final ResourceLocation GUIDE_ID = Powah.id("book");

    public PowahBookItem(Properties properties) {
        super(properties.rarity(Rarity.UNCOMMON));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        GuidesCommon.openGuide(playerIn, GUIDE_ID);
        return InteractionResultHolder.success(stack);
    }
}
