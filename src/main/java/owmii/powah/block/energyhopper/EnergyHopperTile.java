package owmii.powah.block.energyhopper;

import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import owmii.lib.block.TileBase;
import owmii.lib.util.Text;
import owmii.powah.block.ITiles;
import owmii.powah.block.Tier;

import java.util.List;

public class EnergyHopperTile extends TileBase.EnergyStorage<Tier, EnergyHopperBlock> {
    public EnergyHopperTile(Tier variant) {
        super(ITiles.ENERGY_HOPPER, variant);
    }

    public EnergyHopperTile() {
        this(Tier.STARTER);
    }

    @Override
    protected boolean postTicks(World world) {
        final int[] extracted = {0};
        Direction side = getBlockState().get(EnergyHopperBlock.FACING);
        TileEntity tile = getTileEntity(this.pos.offset(side));
        long charging = getBlock().getChargingSpeed();
        if (tile instanceof IInventory) {
            IInventory inventory = (IInventory) tile;
            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (stack.getCount() == 1) {
                    extracted[0] += chargeItem(stack, charging);
                }
            }
        } else if (tile != null) {
            tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(iItemHandler -> {
                for (int i = 0; i < iItemHandler.getSlots(); i++) {
                    ItemStack stack = iItemHandler.getStackInSlot(i);
                    if (stack.getCount() == 1) {
                        extracted[0] += chargeItem(stack, charging);
                    }
                }
            });
        }
        return extracted[0] > 0;
    }

    @Override
    public void getListedEnergyInfo(List<String> list) {
        super.getListedEnergyInfo(list);
        list.add(TextFormatting.GRAY + I18n.format("info.powah.charging.speed", TextFormatting.DARK_GRAY + Text.numFormat(getBlock().getChargingSpeed())));
    }
}
