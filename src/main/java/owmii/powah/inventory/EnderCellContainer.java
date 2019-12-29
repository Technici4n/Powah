package owmii.powah.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import owmii.lib.util.Energy;
import owmii.lib.util.Server;
import owmii.powah.block.PowahBlock;
import owmii.powah.block.endercell.EnderCellTile;
import owmii.powah.block.endercell.EnderNetwork;
import owmii.powah.block.energycell.EnergyCellBlock;
import owmii.powah.energy.PowahStorage;
import owmii.powah.item.PowahBlockItem;

import javax.annotation.Nullable;
import java.util.Objects;

public class EnderCellContainer extends PowahContainer<EnderCellTile> {
    public EnderCellContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        super(containerType, id, playerInventory, buffer);
        addContainer(playerInventory, getTile());
    }

    public EnderCellContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, EnderCellTile tile) {
        super(containerType, id, playerInventory, tile);
        addContainer(playerInventory, tile);
    }

    public static EnderCellContainer create(int i, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new EnderCellContainer(IContainers.ENERGY_CELL, i, playerInventory, buffer);
    }

    private void addContainer(PlayerInventory playerInventory, EnderCellTile tile) {
        addPlayerInv(playerInventory, 8, 146, 88);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        Slot slot = this.inventorySlots.get(index);
        if (this.te.getOwner() != null && this.te.isExtender() && slot != null && slot.getHasStack()) {
            ItemStack stack1 = slot.getStack();
            if (Objects.requireNonNull(stack1.getItem().getRegistryName()).toString().startsWith("powah:energy_cell")) {
                if (stack1.getItem() instanceof PowahBlockItem) {
                    PowahBlockItem item = (PowahBlockItem) stack1.getItem();
                    PowahBlock block = item.getBlock();
                    if (block instanceof EnergyCellBlock) {
                        PowahStorage storage = this.te.getInternal();
                        int cap = block.getCapacity();
                        int newCap = this.te.internal.getMaxEnergyStored() + cap;
                        int maxCap = 2_000_000_000;
                        if (cap <= maxCap && newCap <= maxCap) {
                            int stored = Energy.getStored(stack1);
                            storage.setCapacity(newCap);
                            storage.setEnergy(stored + this.te.getEnergyStored());
                            if (this.te.isServerWorld()) {
                                EnderNetwork network = Server.getData(EnderNetwork::new);
                                network.setStorage(this.te.getOwner().getId(), this.te.getActiveChannel(), storage);
                                this.te.markDirtyAndSync();
                            }
                            stack1.shrink(1);
                            if (refresh(player)) {
                                player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDER_EYE_DEATH, SoundCategory.BLOCKS, 1.0F, 1.0F);
                            }
                            return stack1;
                        }
                    }
                }
            }
        }
        return super.transferStackInSlot(player, index);
    }
}
