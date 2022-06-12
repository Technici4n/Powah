package owmii.powah.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import owmii.powah.Powah;
import owmii.powah.config.IEnergyConfig;
import owmii.powah.config.v2.types.EnergyConfig;
import owmii.powah.lib.item.EnergyItem;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.api.energy.endernetwork.IEnderExtender;
import owmii.powah.block.Tier;

public class BatteryItem extends EnergyItem<Tier, EnergyConfig, BatteryItem> implements IEnderExtender {
    public BatteryItem(Item.Properties properties, Tier variant) {
        super(properties, variant);
    }

    @Override
    public IEnergyConfig<Tier> getConfig() {
        return Powah.config().devices.batteries;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof Player && isCharging(stack)) {
            Energy.ifPresent(stack, storage -> {
                storage.chargeInventory((Player) entity, stack1 -> !(stack1.getItem() instanceof BatteryItem));
            });
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isShiftKeyDown()) {
            switchCharging(stack);
            return InteractionResultHolder.success(stack);
        }
        return super.use(world, player, hand);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        var energy = Energy.getEnergy(stack).orElse(Energy.Item.create(0));
        return energy.getEnergyStored() < energy.getMaxEnergyStored();
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        var energy = Energy.getEnergy(stack).orElse(Energy.Item.create(0));
        return Math.min(1 + 12 * energy.getEnergyStored() / energy.getMaxEnergyStored(), 13);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return isCharging(stack);
    }

    private void switchCharging(ItemStack stack) {
        setCharging(stack, !isCharging(stack));
    }

    private boolean isCharging(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("charging");
    }

    private void setCharging(ItemStack stack, boolean charging) {
        stack.getOrCreateTag().putBoolean("charging", charging);
    }

    @Override
    public long getExtendedCapacity(ItemStack stack) {
        return getConfig().getCapacity(getVariant());
    }

    @Override
    public long getExtendedEnergy(ItemStack stack) {
        return Energy.getStored(stack);
    }
}
