package owmii.powah.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.energy.IEnergyStorage;
import owmii.lib.config.IEnergyConfig;
import owmii.lib.item.EnergyItem;
import owmii.lib.logistics.energy.Energy;
import owmii.powah.api.energy.endernetwork.IEnderExtender;
import owmii.powah.block.Tier;
import owmii.powah.config.Configs;
import owmii.powah.config.item.BatteryConfig;

public class BatteryItem extends EnergyItem<Tier, BatteryConfig, BatteryItem> implements IEnderExtender {
    public BatteryItem(Item.Properties properties, Tier variant) {
        super(properties, variant);
    }

    @Override
    public IEnergyConfig<Tier> getConfig() {
        return Configs.BATTERY;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof Player && isCharging(stack)) {
            Energy.ifPresent(stack, storage -> {
                if (storage instanceof Energy) {
                    ((Energy) storage).chargeInventory((Player) entity, stack1 -> !(stack1.getItem() instanceof BatteryItem));
                }
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
    public boolean showDurabilityBar(ItemStack stack) {
        IEnergyStorage energy = Energy.get(stack).orElse(Energy.Item.create(0));
        return energy.getEnergyStored() < energy.getMaxEnergyStored();
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        IEnergyStorage energy = Energy.get(stack).orElse(Energy.Item.create(0));
        return 1.0F - ((double) energy.getEnergyStored() / energy.getMaxEnergyStored());
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
