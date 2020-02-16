package owmii.powah.block.furnator;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import owmii.lib.block.TileBase;
import owmii.powah.block.ITiles;
import owmii.powah.block.Tier;
import owmii.powah.config.Configs;

public class FurnatorTile extends TileBase.EnergyProvider<Tier, FurnatorBlock> {
    public FurnatorTile(Tier variant) {
        super(ITiles.FURNATOR, variant);
        this.inv.add(1);
    }

    public FurnatorTile() {
        this(Tier.STARTER);
    }

    @Override
    protected void generate(World world) {
        final ItemStack fuelStack = this.inv.getStackInSlot(builtInSlots());
        if (this.nextBuff <= 0 && !fuelStack.isEmpty()) {
            this.buffer = ForgeHooks.getBurnTime(fuelStack) * Configs.GENERAL.fuelTicks.get();
            if (this.buffer <= 0) return;
            this.nextBuff = this.buffer;
            if (fuelStack.hasContainerItem())
                this.inv.setStack(1, fuelStack.getContainerItem());
            else {
                fuelStack.shrink(1);
            }
        }
    }

    @Override
    public boolean hasEnergyBuffer() {
        return true;
    }

    @Override
    public int getChargingSlots() {
        return 1;
    }

    @Override
    public boolean canInsert(int index, ItemStack stack) {
        return index == builtInSlots() ? ForgeHooks.getBurnTime(stack) > 0 : super.canInsert(index, stack);
    }
}
