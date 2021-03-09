package owmii.powah.block.transmitter;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import owmii.lib.block.AbstractEnergyStorage;
import owmii.lib.block.IInventoryHolder;
import owmii.lib.compat.curios.CuriosCompat;
import owmii.lib.logistics.energy.Energy;
import owmii.lib.util.Player;
import owmii.lib.util.Stack;
import owmii.powah.block.Tier;
import owmii.powah.block.Tiles;
import owmii.powah.config.PlayerTransmitterConfig;
import owmii.powah.item.BindingCardItem;

import java.util.Optional;

public class PlayerTransmitterTile extends AbstractEnergyStorage<Tier, PlayerTransmitterConfig, PlayerTransmitterBlock> implements IInventoryHolder {

    public PlayerTransmitterTile(Tier variant) {
        super(Tiles.PLAYER_TRANSMITTER, variant);
        this.inv.add(1);
    }

    public PlayerTransmitterTile() {
        this(Tier.STARTER);
    }

    @Override
    protected int postTick(World world) {
        long extracted = 0;
        if (!isRemote() && checkRedstone()) {
            ItemStack stack = this.inv.getFirst();
            if (stack.getItem() instanceof BindingCardItem) {
                BindingCardItem card = (BindingCardItem) stack.getItem();
                Optional<ServerPlayerEntity> op = card.getPlayer(stack);
                if (op.isPresent()) {
                    ServerPlayerEntity player = op.get();
                    if (card.isMultiDim(stack) || player.world.getDimensionType().equals(world.getDimensionType())) {
                        long charging = getConfig().getChargingSpeed(this.variant);
                        for (ItemStack stack1 : Player.invStacks(player)) {
                            if (stack1.isEmpty() || !Energy.chargeable(stack1)) continue;
                            long amount = Math.min(charging, getEnergy().getStored());
                            int received = Energy.receive(stack1, amount, false);
                            extracted += extractEnergy(received, false, null);
                        }
                        for (ItemStack stack1 : CuriosCompat.getAllStacks(player)) {
                            if (stack1.isEmpty() || !Energy.chargeable(stack1)) continue;
                            long amount = Math.min(charging, getEnergy().getStored());
                            int received = Energy.receive(stack1, amount, false);
                            extracted += extractEnergy(received, false, null);
                        }
                    }
                }
            }
        }
        return extracted > 0 ? 5 : -1;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack) {
        return Stack.getTagOrEmpty(stack).hasUniqueId("bound_player_id");
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public boolean keepEnergy() {
        return true;
    }
}
