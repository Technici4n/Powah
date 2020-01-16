package owmii.powah.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import owmii.lib.item.ItemBase;
import owmii.lib.util.Server;
import owmii.powah.block.endercell.EnderNetwork;

public class EnderProbeItem extends ItemBase {
    public EnderProbeItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (world.isRemote || !(entity instanceof PlayerEntity)) return;
        EnderNetwork network = Server.getData(EnderNetwork::new);

    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
