package owmii.powah.block.reactor;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.FluidAttributes;
import org.apache.commons.lang3.tuple.Pair;
import owmii.lib.block.AbstractEnergyProvider;
import owmii.lib.block.IInventoryHolder;
import owmii.lib.block.ITankHolder;
import owmii.lib.logistics.energy.Energy;
import owmii.lib.logistics.fluid.Tank;
import owmii.lib.util.Ticker;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.Tier;
import owmii.powah.block.Tiles;
import owmii.powah.config.generator.ReactorConfig;
import owmii.powah.item.Itms;

import javax.annotation.Nullable;

public class ReactorTile extends AbstractEnergyProvider<Tier, ReactorConfig, ReactorBlock> implements IInventoryHolder, ITankHolder {
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

    public ReactorTile(Tier variant) {
        super(Tiles.REACTOR, variant);
        this.tank.setCapacity(FluidAttributes.BUCKET_VOLUME)
                .validate(stack -> PowahAPI.COOLANTS.containsKey(stack.getFluid()))
                .setChange(() -> ReactorTile.this.sync(10));
        this.inv.add(5);
    }

    public ReactorTile() {
        this(Tier.STARTER);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.baseTemp = nbt.getInt("base_temp");
        this.carbonTemp = nbt.getInt("carbon_temp");
        this.redstoneTemp = nbt.getInt("redstone_temp");
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        nbt.putInt("base_temp", this.baseTemp);
        nbt.putInt("carbon_temp", this.carbonTemp);
        nbt.putInt("redstone_temp", this.redstoneTemp);
        return super.write(nbt);
    }

    @Override
    public void readSync(CompoundNBT nbt) {
        super.readSync(nbt);
        this.builder.read(nbt);
        this.fuel.read(nbt, "fuel");
        this.carbon.read(nbt, "carbon");
        this.redstone.read(nbt, "redstone");
        this.solidCoolant.read(nbt, "solid_coolant");
        this.solidCoolantTemp = nbt.getInt("solid_coolant_temp");
        this.running = nbt.getBoolean("running");
        this.genModeOn = nbt.getBoolean("gen_mode");
        this.generate = nbt.getBoolean("generate");
        this.temp.read(nbt, "temperature");
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT nbt) {
        this.builder.write(nbt);
        this.fuel.write(nbt, "fuel");
        this.carbon.write(nbt, "carbon");
        this.redstone.write(nbt, "redstone");
        this.solidCoolant.write(nbt, "solid_coolant");
        nbt.putInt("solid_coolant_temp", this.solidCoolantTemp);
        nbt.putBoolean("running", this.running);
        nbt.putBoolean("gen_mode", this.genModeOn);
        nbt.putBoolean("generate", this.generate);
        this.temp.write(nbt, "temperature");
        return super.writeSync(nbt);
    }

    @Override
    protected int postTick(World world) {
        if (isRemote() || !this.builder.isDone(world)) return -1;
        long extracted = chargeItems(1);
        boolean flag = false;
        boolean flag2 = false;

        if (checkRedstone() && checkGenMode()) {
            boolean generating = !this.energy.isFull() && !this.fuel.isEmpty();
            boolean b0 = processFuel(world);
            boolean b1 = processCarbon(world, generating);
            boolean b2 = processRedstone(world, generating);
            boolean b3 = processTemperature(world, generating);
            if (b0 || b1 || b2 || b3) {
                flag = true;
            }

            if (generating) {
                this.fuel.back(calcConsumption());
                this.energy.produce((long) calcProduction());
                flag = true;
                flag2 = true;
            }

            if (flag && this.isContainerOpen) {
                sync(3);
            }
        }

        for (Direction direction : Direction.values()) {
            if (canExtractEnergy(direction)) {
                long amount = Math.min(getEnergyTransfer(), getEnergy().getStored());
                BlockPos pos = this.pos.offset(direction, direction.getAxis().isHorizontal() ? 2 : direction.equals(Direction.UP) ? 4 : 1);
                int received = Energy.receive(world.getTileEntity(pos), direction, amount, false);
                extracted += extractEnergy(received, false, direction);
            }
        }

        if (this.running != flag2) {
            this.running = flag2;
            sync(5);
        }

        return extracted > 0 ? 5 : -1;
    }

    @Override
    protected void clientTick(World world) {
        if (this.running) {
            this.bright.onward();
        } else {
            this.bright.back();
        }
    }

    private boolean checkGenMode() {
        if (this.genModeOn) {
            if (this.energy.isFull()) {
                this.generate = false;
            } else if (this.energy.getPercent() <= 70) {
                this.generate = true;
            }
            return this.generate;
        }
        return true;
    }

    public double calcProduction() {
        double d = this.carbon.isEmpty() ? 1 : 1.2D;
        double d1 = this.redstone.isEmpty() ? 1 : 1.4D;
        return (1.0D - calc()) * (this.fuel.getTicks() / 100) * getGeneration() * d * d1;
    }

    public double calcConsumption() {
        if (this.running) {
            double d1 = 1.0D + (this.variant.ordinal() * 0.25D);
            return (1.0D + this.variant.ordinal() * 0.25D) * calc();
        } else return 0.0D;
    }

    public double calc() {
        double d0 = this.redstone.isEmpty() ? 1.0D : 1.4D;
        return (this.temp.getTicks() / 1000.0D * 0.98D / 2.0D) * d0;
    }

    private boolean processTemperature(World world, boolean generating) {
        boolean flag = false;
        if (this.solidCoolant.isEmpty()) {
            ItemStack stack = this.inv.getStackInSlot(4);
            if (!stack.isEmpty()) {
                Pair<Integer, Integer> coolant = PowahAPI.getSolidCoolant(stack.getItem());
                int size = coolant.getLeft();
                int temp = coolant.getRight();
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
            int coldness = PowahAPI.getCoolant(this.tank.getFluid().getFluid()) - 2;
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

    private boolean processRedstone(World world, boolean generating) {
        boolean flag = false;
        if (this.redstone.isEmpty()) {
            ItemStack stack = this.inv.getStackInSlot(3);
            if (stack.getItem() == Items.REDSTONE) {
                this.redstone.setAll(18);
            } else if (stack.getItem() == Items.REDSTONE_BLOCK) {
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
        } else this.redstoneTemp = 0;
        return flag;
    }

    private boolean processCarbon(World world, boolean generating) {
        boolean flag = false;
        if (this.carbon.isEmpty()) {
            ItemStack stack = this.inv.getStackInSlot(2);
            if (!stack.isEmpty()) {
                int carbon = ForgeHooks.getBurnTime(stack);
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
        } else this.carbonTemp = 0;
        return flag;
    }

    private boolean processFuel(World world) {
        boolean flag = false;
        if (this.fuel.getTicks() <= 900) {
            ItemStack stack = this.inv.getStackInSlot(1);
            if (stack.getItem() == Itms.URANINITE) {
                this.fuel.add(100);
                this.baseTemp = 700;
                stack.shrink(1);
                flag = true;
            }
        }

        if (this.fuel.isEmpty()) {
            this.baseTemp = 0;
        }
        return flag;
    }

    @Override
    public void onPlaced(World world, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onPlaced(world, state, placer, stack);
        this.builder.shuffle();
    }

    public void demolish(World world) {
        this.builder.demolish(world);
        while (this.fuel.getTicks() >= 100) {
            Block.spawnAsEntity(world, this.pos, new ItemStack(Itms.URANINITE));
            this.fuel.back(100);
        }
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
            return stack.getItem() == Itms.URANINITE;
        } else if (slot == 2) {
            return ForgeHooks.getBurnTime(stack) > 0 && !stack.hasContainerItem();
        } else if (slot == 3) {
            return stack.getItem() == Items.REDSTONE || stack.getItem() == Items.REDSTONE_BLOCK;
        } else if (slot == 4) {
            Pair<Integer, Integer> coolant = PowahAPI.getSolidCoolant(stack.getItem());
            return coolant.getLeft() > 0 && coolant.getRight() < 2;
        } else return Energy.chargeable(stack);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public Tank getTank() {
        return this.tank;
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
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(this.pos).grow(1.0D, 3.0D, 1.0D);
    }
}
