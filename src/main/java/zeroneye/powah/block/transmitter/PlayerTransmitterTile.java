package zeroneye.powah.block.transmitter;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.dimension.DimensionType;
import zeroneye.lib.util.Energy;
import zeroneye.lib.util.Player;
import zeroneye.powah.block.ITiles;
import zeroneye.powah.block.PowahTile;
import zeroneye.powah.item.BindingCardItem;

public class PlayerTransmitterTile extends PowahTile {
    private boolean acrossDim;

    public PlayerTransmitterTile(int capacity, int transfer, boolean acrossDim) {
        super(ITiles.PLAYER_TRANSMITTER, capacity, transfer, transfer, false);
        this.acrossDim = acrossDim;
    }

    public PlayerTransmitterTile() {
        this(0, 0, false);
    }

    @Override
    public void readStorable(CompoundNBT compound) {
        super.readStorable(compound);
        this.acrossDim = compound.getBoolean("AcrossDimention");
    }

    @Override
    public CompoundNBT writeStorable(CompoundNBT compound) {
        compound.putBoolean("AcrossDimention", this.acrossDim);
        return super.writeStorable(compound);
    }

    @Override
    protected boolean postTicks() {
        final int[] i = {0};
        if (this.world == null) return false;
        if (this.isServerWorld) {
            if (this.internal.hasEnergy()) {
                this.stacks.forEach(stack -> {
                    if (stack.getItem() instanceof BindingCardItem) {
                        BindingCardItem item = (BindingCardItem) stack.getItem();
                        item.getPlayer(stack).ifPresent(player -> {
                            DimensionType type = this.world.dimension.getType();
                            if (this.acrossDim || player.dimension.equals(type)) {
                                for (ItemStack stack1 : Player.invStacks(player)) {
                                    int amount = Math.min(this.internal.getMaxExtract(), this.internal.getEnergyStored());
                                    int received = Energy.receive(stack1, amount, false);
                                    i[0] += extractEnergy(received, false, null);
                                }
                            }
                        });
                    }
                });
            }
        }
        return i[0] > 0;
    }

    @Override
    public int getSizeInventory() {
        return 2;
    }

    @Override
    protected ExtractionType getExtractionType() {
        return ExtractionType.OFF;
    }

    @Override
    public int getSyncTicks() {
        return isContainerOpen() ? 3 : 40;
    }

    @Override
    public boolean dropInventoryOnBreak() {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack itemStack) {
        if (this.world == null || !(itemStack.getItem() instanceof BindingCardItem)) {
            return false;
        } else {
            if (getBlock() instanceof PlayerTransmitterBlock && index == 1 && ((PlayerTransmitterBlock) getBlock()).getSlots() < 2) {
                return false;
            }
        }
        return ((BindingCardItem) itemStack.getItem()).getPlayer(itemStack).isPresent() || super.isItemValidForSlot(index, itemStack);

    }
}
