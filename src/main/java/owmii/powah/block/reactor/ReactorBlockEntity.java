package owmii.powah.block.reactor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.Tiles;
import owmii.powah.lib.block.IInventoryHolder;
import owmii.powah.lib.block.ITankHolder;
import owmii.powah.lib.block.PowahBaseGeneratorBlockEntity;
import owmii.powah.lib.logistics.fluid.Tank;
import owmii.powah.recipe.ReactorFuel;
import owmii.powah.util.ChargeUtil;
import owmii.powah.util.EnergyUtil;
import owmii.powah.util.Ticker;
import owmii.powah.util.Util;

public class ReactorBlockEntity extends PowahBaseGeneratorBlockEntity<ReactorBlock> implements IInventoryHolder, ITankHolder {
    private final Builder builder = new Builder(this);

    public final Ticker fuel = new Ticker(1000);
    public final Ticker carbon = Ticker.empty();
    public final Ticker redstone = Ticker.empty();

    public final Ticker solidCoolant = Ticker.empty();
    public int solidCoolantTemp;

    public final Ticker temp = new Ticker(1000);
    private int redstoneTemp;
    private int carbonTemp;
    private int baseTemp;

    public final Ticker bright = new Ticker(20);

    private boolean running;
    private boolean genModeOn;
    private boolean generate = true;

    public ReactorBlockEntity(BlockPos pos, BlockState state) {
        super(Tiles.REACTOR.get(), pos, state);
        this.tank.setValidator(stack -> PowahAPI.getCoolant(stack.getFluid()).isPresent());
        this.tank.setChange(() -> ReactorBlockEntity.this.sync(10));
    }

    @Override
    protected int getInternalInventorySize() {
        return 5;
    }

    @Override
    protected int getInternalTankCapacity() {
        return Util.bucketAmount();
    }

    @Override
    public void loadServerOnly(ValueInput input) {
        super.loadServerOnly(input);
        this.baseTemp = input.getIntOr("base_temp", 0);
        this.carbonTemp = input.getIntOr("carbon_temp", 0);
        this.redstoneTemp = input.getIntOr("redstone_temp", 0);
    }

    @Override
    public void saveServerOnly(ValueOutput output) {
        output.putInt("base_temp", this.baseTemp);
        output.putInt("carbon_temp", this.carbonTemp);
        output.putInt("redstone_temp", this.redstoneTemp);
        super.saveServerOnly(output);
    }

    @Override
    public void readSync(ValueInput input) {
        super.readSync(input);
        this.builder.read(input);
        this.fuel.read(input, "fuel");
        this.carbon.read(input, "carbon");
        this.redstone.read(input, "redstone");
        this.solidCoolant.read(input, "solid_coolant");
        this.solidCoolantTemp = input.getIntOr("solid_coolant_temp", 0);
        this.running = input.getBooleanOr("running", false);
        this.genModeOn = input.getBooleanOr("gen_mode", false);
        this.generate = input.getBooleanOr("generate", false);
        this.temp.read(input, "temperature");
    }

    @Override
    public void writeSync(ValueOutput output) {
        this.builder.write(output);
        this.fuel.write(output, "fuel");
        this.carbon.write(output, "carbon");
        this.redstone.write(output, "redstone");
        this.solidCoolant.write(output, "solid_coolant");
        output.putInt("solid_coolant_temp", this.solidCoolantTemp);
        output.putBoolean("running", this.running);
        output.putBoolean("gen_mode", this.genModeOn);
        output.putBoolean("generate", this.generate);
        this.temp.write(output, "temperature");
        super.writeSync(output);
    }

    @Override
    protected int postTick(Level world) {
        if (isRemote() || !this.builder.isDone(world))
            return -1;
        long extracted = chargeItems(1);
        boolean flag = false;
        boolean flag2 = false;

        if (checkRedstone() && this.generate) {
            boolean generating = !getEnergy().isFull() && !this.fuel.isEmpty();
            boolean b0 = processFuel();
            boolean b1 = processCarbon(world, generating);
            boolean b2 = processRedstone(world, generating);
            boolean b3 = processTemperature(world, generating);
            if (b0 || b1 || b2 || b3) {
                flag = true;
            }

            if (generating) {
                this.fuel.back(calcConsumption());
                getEnergy().produce((long) calcProduction());
                flag = true;
                flag2 = true;
            }

            if (flag && this.isContainerOpen) {
                sync(3);
            }
        }

        checkGenMode();

        try (var tx = Transaction.openRoot()) {
            for (Direction direction : Direction.values()) {
                if (canExtractEnergy(direction)) {
                    long amount = Math.min(getEnergyTransfer(), getEnergy().getStored());
                    BlockPos pos = this.worldPosition.relative(direction,
                            direction.getAxis().isHorizontal() ? 2 : direction.equals(Direction.UP) ? 4 : 1);
                    long received = EnergyUtil.pushEnergy(world, pos, direction.getOpposite(), amount, tx);
                    extracted += extractEnergy((int) received, tx, direction);
                }
            }
            tx.commit();
        }

        if (this.running != flag2) {
            this.running = flag2;
            sync(5);
        }

        return extracted > 0 ? 5 : -1;
    }

    @Override
    protected void clientTick(Level world) {
        if (this.running) {
            this.bright.onward();
        } else {
            this.bright.back();
        }
    }

    private void checkGenMode() {
        if (this.genModeOn) {
            if (getEnergy().isFull()) {
                this.generate = false;
            } else if (getEnergy().getPercent() <= 70) {
                this.generate = true;
            }
        }
    }

    public double calcProduction() {
        double d = this.carbon.isEmpty() ? 1.2D : 1D;
        double d1 = this.redstone.isEmpty() ? 1.4D : 1D;
        return (1.0D - calc()) * (this.fuel.getTicks() / 1000) * getEnergyGeneration() / d / d1;
    }

    public double calcConsumption() {
        if (this.running) {
            return (1.0D + getBlock().getTier().ordinal() * 0.25D) * calc();
        } else
            return 0.0D;
    }

    public double calc() {
        double d0 = this.redstone.isEmpty() ? 1.0D : 1.4D;
        return (this.temp.getTicks() / 1000.0D * 0.98D / 2.0D) * d0;
    }

    private boolean processTemperature(Level world, boolean generating) {
        boolean flag = false;
        if (this.solidCoolant.isEmpty()) {
            ItemStack stack = this.inv.getStackInSlot(4);
            if (!stack.isEmpty()) {
                var coolant = PowahAPI.getSolidCoolant(stack.getItem());
                int size = coolant.amount();
                int temp = coolant.temperature();
                if (size > 0 && temp < 2) {
                    this.solidCoolant.setAll(size);
                    this.solidCoolantTemp = temp;
                    stack.shrink(1);
                    flag = true;
                }
            }
        }
        if (!this.solidCoolant.isEmpty()) {
            if (!this.tank.isEmpty()) {
                if (generating) {
                    if (this.ticks % 40 == 0) {
                        this.solidCoolant.back();
                        if (this.solidCoolant.isEmpty()) {
                            this.solidCoolant.setMax(0);
                        }
                        flag = true;
                    }
                }
            }
        } else {
            this.solidCoolantTemp = 0;
        }
        double temp = Math.min(this.baseTemp + this.carbonTemp + this.redstoneTemp, this.temp.getMax());
        if (!this.tank.isEmpty()) {
            int coldness = -PowahAPI.getCoolant(this.tank.getFluid().getFluid()).orElse(0);
            int i = Math.abs(coldness + this.solidCoolantTemp) + 1;
            temp /= i;
            sync(5);
        }
        if (this.temp.getTicks() < temp) {
            this.temp.onward();
            flag = true;
        }
        if (this.ticks % (this.tank.isEmpty() ? 5 : this.solidCoolant.isEmpty() ? 3 : 1) == 0) {
            if (this.temp.getTicks() > temp) {
                this.temp.back();
                flag = true;
            }
        }
        return flag;
    }

    private boolean processRedstone(Level world, boolean generating) {
        boolean flag = false;
        if (this.redstone.isEmpty()) {
            ItemStack stack = this.inv.getStackInSlot(3);
            if (stack.is(Tags.Items.DUSTS_REDSTONE)) {
                this.redstone.setAll(18);
            } else if (stack.is(Tags.Items.STORAGE_BLOCKS_REDSTONE)) {
                this.redstone.setAll(162);
            }
            this.redstoneTemp = 120;
            stack.shrink(1);
            flag = true;
        }
        if (!this.redstone.isEmpty()) {
            if (generating) {
                if (this.ticks % 40 == 0) {
                    this.redstone.back();
                    if (this.redstone.isEmpty()) {
                        this.redstone.setMax(0);
                    }
                    flag = true;
                }
            }
        } else
            this.redstoneTemp = 0;
        return flag;
    }

    private boolean processCarbon(Level world, boolean generating) {
        boolean flag = false;
        if (this.carbon.isEmpty()) {
            ItemStack stack = this.inv.getStackInSlot(2);
            if (!stack.isEmpty()) {
                int carbon = stack.getBurnTime(RecipeType.SMELTING, getLevel().fuelValues());
                if (carbon > 0) {
                    this.carbon.setAll(carbon);
                    this.carbonTemp = 180;
                    stack.shrink(1);
                    flag = true;
                }
            }

        }
        if (!this.carbon.isEmpty()) {
            if (generating) {
                this.carbon.back();
                if (this.carbon.isEmpty()) {
                    this.carbon.setMax(0);
                }
            }
        } else
            this.carbonTemp = 0;
        return flag;
    }

    private boolean processFuel() {
        boolean flag = false;

        var stack = this.inv.getStackInSlot(1);
        if (!stack.isEmpty()) {
            var fuel = ReactorFuel.getFuel(stack.getItem());
            if (fuel != null) {
                // Try not to waste fuel
                if (this.fuel.isEmpty() || this.fuel.getTicks() + fuel.fuelAmount() <= this.fuel.getMax()) {
                    this.fuel.add(fuel.fuelAmount());
                    this.baseTemp = fuel.temperature();
                    stack.shrink(1);
                    flag = true;
                }
            }
        }

        if (this.fuel.isEmpty()) {
            this.baseTemp = 0;
        }
        return flag;
    }

    @Override
    public void onPlaced(Level world, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onPlaced(world, state, placer, stack);
        this.builder.shuffle();
    }

    public void demolish(Level world) {
        this.builder.demolish(world);
    }

    public boolean isBuilt() {
        return this.builder.built;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack) {
        if (slot == 1) {
            return ReactorFuel.getFuel(stack.getItem()) != null;
        } else if (slot == 2) {
            return stack.getBurnTime(RecipeType.SMELTING, getLevel().fuelValues()) > 0 && stack.getCraftingRemainder() == null;
        } else if (slot == 3) {
            return stack.is(Tags.Items.DUSTS_REDSTONE) || stack.is(Tags.Items.STORAGE_BLOCKS_REDSTONE);
        } else if (slot == 4) {
            var coolant = PowahAPI.getSolidCoolant(stack.getItem());
            return coolant.amount() > 0 && coolant.temperature() < 2;
        } else
            return ChargeUtil.isChargeableItem(stack);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public Tank getTank() {
        return this.tank;
    }

    @Override
    public boolean keepFluid() {
        return false;
    }

    public boolean isRunning() {
        return this.running;
    }

    public boolean isGenModeOn() {
        return this.genModeOn;
    }

    public void setGenModeOn(boolean genModeOn) {
        this.genModeOn = genModeOn;
        sync();
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);
        demolish(getLevel());
    }
}
