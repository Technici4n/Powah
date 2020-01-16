package owmii.powah.block.generator.reactor;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.apache.commons.lang3.tuple.Pair;
import owmii.lib.util.Data;
import owmii.lib.util.Energy;
import owmii.lib.util.Ticker;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.ITiles;
import owmii.powah.block.generator.GeneratorTile;
import owmii.powah.item.IItems;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class ReactorTile extends GeneratorTile {
    protected final FluidTank tank = new FluidTank(FluidAttributes.BUCKET_VOLUME) {
        @Override
        public boolean isFluidValid(FluidStack stack) {
            return PowahAPI.REACTOR_COOLANTS.containsKey(stack.getFluid()) && super.isFluidValid(stack);
        }

        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            ReactorTile.this.sync(5);
        }
    };
    private final LazyOptional<IFluidHandler> holder;
    private List<BlockPos> posList = new ArrayList<>();
    private BlockPos corePos = BlockPos.ZERO;
    private boolean isCore;
    private boolean built;

    private final Ticker fuel = new Ticker(1000);
    private final Ticker carbon = Ticker.empty();
    private final Ticker redstone = Ticker.empty();

    private final Ticker solidCoolant = Ticker.empty();
    private int solidCoolantTemp;

    private final Ticker temp = new Ticker(1000);
    private int baseTemp;
    private int carbonTemp;
    private int redstoneTemp;

    public ReactorTile(int capacity, int transfer, int perTick) {
        super(ITiles.REACTOR, capacity, transfer, perTick);
        this.holder = LazyOptional.of(() -> this.tank);
        this.inv.add(4);
    }

    public ReactorTile() {
        this(0, 0, 0);
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.posList = Data.readPosList(compound, "QueuedPos", new ArrayList<>());
        this.baseTemp = compound.getInt("BaseTemp");
        this.carbonTemp = compound.getInt("CarbonTemp");
        this.redstoneTemp = compound.getInt("RedstoneTemp");
    }

    @Override
    public void readSync(CompoundNBT compound) {
        this.tank.readFromNBT(compound);
        this.corePos = Data.readPos(compound, "CorePos");
        this.isCore = compound.getBoolean("IsCore");
        this.built = compound.getBoolean("Built");
        this.fuel.read(compound, "Fuel");
        this.carbon.read(compound, "Carbon");
        this.redstone.read(compound, "Redstone");
        this.solidCoolant.read(compound, "SolidCoolant");
        this.solidCoolantTemp = compound.getInt("SolidCoolantTemp");
        this.temp.read(compound, "Temperature");
        super.readSync(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        Data.writePosList(compound, this.posList, "QueuedPos");
        compound.putInt("BaseTemp", this.baseTemp);
        compound.putInt("CarbonTemp", this.carbonTemp);
        compound.putInt("RedstoneTemp", this.redstoneTemp);
        return super.write(compound);
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT compound) {
        this.tank.writeToNBT(compound);
        Data.writePos(compound, this.corePos, "CorePos");
        compound.putBoolean("IsCore", this.isCore);
        compound.putBoolean("Built", this.built);
        this.fuel.write(compound, "Fuel");
        this.carbon.write(compound, "Carbon");
        this.redstone.write(compound, "Redstone");
        this.solidCoolant.write(compound, "SolidCoolant");
        compound.putInt("SolidCoolantTemp", this.solidCoolantTemp);
        this.temp.write(compound, "Temperature");
        return super.writeSync(compound);
    }

    @Override
    protected boolean doTick() {
        return this.isCore;
    }

    @Override
    protected boolean postTicks() {
        if (this.world == null) return false;
        if (this.world.isRemote) return false;
        if (!this.isCore) return false;

        boolean flag = false;
        int extracted = 0;

        if (this.built) {
            for (Direction direction : Direction.values()) {
                if (canExtract(direction)) {
                    int amount = Math.min(getMaxExtract(), getEnergyStored());
                    BlockPos pos = this.pos.offset(direction, direction.getAxis().isHorizontal() ? 2 : direction.equals(Direction.UP) ? 4 : 1);
                    int received = Energy.receive(this.world.getTileEntity(pos), direction, amount, false);
                    extracted += extractEnergy(received, false, direction);
                }
            }

            boolean generating = !this.internal.isFull() && !this.fuel.isEmpty();

            if (this.fuel.getTicks() <= 900) {
                ItemStack stack = this.inv.getStackInSlot(0);
                if (stack.getItem() == IItems.URANINITE) {
                    this.fuel.add(100);
                    this.baseTemp = 700;
                    stack.shrink(1);
                    flag = true;
                }
            }

            if (this.fuel.isEmpty()) {
                this.baseTemp = 0;
            }

            if (this.carbon.isEmpty()) {
                ItemStack stack = this.inv.getStackInSlot(1);
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

            if (this.redstone.isEmpty()) {
                ItemStack stack = this.inv.getStackInSlot(2);
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

            if (this.solidCoolant.isEmpty()) {
                ItemStack stack = this.inv.getStackInSlot(3);
                if (!stack.isEmpty()) {
                    Pair<Integer, Integer> coolant = PowahAPI.getReactorSolidCoolant(stack.getItem());
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
                int coldness = PowahAPI.getReactorCoolant(this.tank.getFluid().getFluid()) - 2;
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
        } else if (!build()) {
            return false;
        }

        if (flag && this.isContainerOpen) {
            sync(3);
        }

        return super.postTicks() || extracted > 0;
    }

    @Override
    protected void generate() {
        if (this.nextGen <= 0 && !this.fuel.isEmpty()) {
            double temp = this.temp.getTicks();
            this.fuel.back(calcConsumption());
            this.nextGenCap = (int) calcProduction();
            this.nextGen = this.nextGenCap;
        }
    }

    public double calcProduction() {
        double d = this.carbon.isEmpty() ? 1 : 1.2D;
        double d1 = this.redstone.isEmpty() ? 1 : 1.4D;
        return ((((1.0D - calcConsumption()) * (this.fuel.getTicks() / 100)) * this.perTick) * d) * d1;
    }

    public double calcConsumption() {
        double d = this.redstone.isEmpty() ? 1 : 1.4D;
        return (this.temp.getTicks() / 1000.0D * 0.98D / 2.0D) * d;
    }

    private boolean build() {
        if (this.world == null) return false;
        if (!this.posList.isEmpty()) {
            boolean flag = true;
            for (BlockPos pos : this.posList) {
                if (!this.world.getBlockState(pos).getMaterial().isReplaceable()) {
                    flag = false;
                    break;
                }
            }
            if (flag && this.ticks > 3 && this.ticks % 5 == 0) {
                Iterator<BlockPos> itr = this.posList.iterator();
                while (itr.hasNext()) {
                    BlockPos pos = itr.next();
                    this.world.setBlockState(pos, getBlock().getDefaultState(), 3);
                    TileEntity tileEntity = this.world.getTileEntity(pos);
                    if (tileEntity instanceof ReactorTile) {
                        ReactorTile part = (ReactorTile) tileEntity;
                        part.setCorePos(this.pos);
                        this.world.playEvent(2001, pos, Block.getStateId(getBlockState()));
                        itr.remove();
                        return false;
                    }
                }
            }
        } else {
            for (BlockPos pos : getPosList()) {
                TileEntity tileEntity = this.world.getTileEntity(pos);
                if (tileEntity instanceof ReactorTile) {
                    ReactorTile part = (ReactorTile) tileEntity;
                    part.built = true;
                    part.markDirtyAndSync();
                }
            }
            this.built = true;
            markDirtyAndSync();
        }
        return true;
    }

    public void demolish() {
        if (this.world == null) return;
        if (this.isCore) {

            List<BlockPos> list = getPosList();
            list.add(this.pos);
            for (int i = 0; i < list.size(); i++) {
                BlockPos blockPos = list.get(i);
                if (this.world.getBlockState(blockPos).getBlock().equals(getBlock())) {
                    Block.spawnAsEntity(this.world, this.pos, new ItemStack(getBlock()));
                    this.world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 3);
                }
            }

            this.posList.forEach(pos -> {
                Block.spawnDrops(getBlockState(), this.world, this.pos, this);
            });

            while (this.fuel.getTicks() >= 100) {
                Block.spawnAsEntity(this.world, this.pos, new ItemStack(IItems.URANINITE));
                this.fuel.back(100);
            }

            this.posList.clear();
            this.world.setBlockState(this.pos, Blocks.AIR.getDefaultState(), 3);
        } else {
            TileEntity tile = this.world.getTileEntity(this.corePos);
            if (tile instanceof ReactorTile) {
                ReactorTile reactor = (ReactorTile) tile;
                if (reactor.getPos().equals(this.corePos)) {
                    reactor.demolish();
                }
            }
        }
    }

    @Override
    public int getChargingSlots() {
        return 0;
    }

    @Override
    public boolean isNBTStorable() {
        return false;
    }

    public FluidTank getTank() {
        return this.tank;
    }

    public boolean isCore() {
        return this.isCore;
    }

    public void setCore(boolean core) {
        this.isCore = core;
        if (core) {
            this.posList.addAll(getPosList());
            Collections.shuffle(this.posList);
        }
    }

    public boolean isBuilt() {
        return this.built;
    }

    public Optional<ReactorTile> core() {
        if (this.world == null || isCore()) return Optional.empty();

        TileEntity tile = this.world.getTileEntity(this.corePos);
        if (tile instanceof ReactorTile) {
            ReactorTile reactor = (ReactorTile) tile;
            if (reactor.isCore()) {
                return Optional.of(reactor);
            }
        }

        return Optional.empty();
    }

    public BlockPos getCorePos() {
        return this.corePos;
    }

    public void setCorePos(BlockPos corePos) {
        this.corePos = corePos;
    }

    public Ticker getFuel() {
        return this.fuel;
    }

    public Ticker getCarbon() {
        return this.carbon;
    }

    public Ticker getRedstone() {
        return this.redstone;
    }

    public Ticker getSolidCoolant() {
        return this.solidCoolant;
    }

    public int getSolidCoolantTemp() {
        return this.solidCoolantTemp;
    }

    public Ticker getTemp() {
        return this.temp;
    }

    @Override
    public boolean isExtractor() {
        return true;
    }

    @Override
    public boolean canExtractFromSides() {
        return false;
    }

    @Override
    public boolean canChargeItems() {
        return false;
    }

    public List<BlockPos> getPosList() {
        return BlockPos.getAllInBox(this.pos.add(-1, 0, -1), this.pos.add(1, 3, 1))
                .map(BlockPos::toImmutable)
                .filter(pos1 -> !pos1.equals(this.pos)).collect(Collectors.toList());
    }

    @Override
    public boolean isEnergyPresent(@Nullable Direction side) {
        return side != null && (this.isCore
                || side.getAxis().isHorizontal() && this.pos.offset(side.getOpposite()).equals(this.corePos)
                || side.equals(Direction.UP) && this.pos.offset(side.getOpposite()).add(0, -2, 0).equals(this.corePos));
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(this.pos).grow(1.0D, 3.0D, 1.0D);
    }

    @Override
    public boolean canInsert(int index, ItemStack stack) {
        if (index == 0) {
            return stack.getItem() == IItems.URANINITE;
        } else if (index == 1) {
            return ForgeHooks.getBurnTime(stack) > 0 && !stack.hasContainerItem();
        } else if (index == 2) {
            return stack.getItem() == Items.REDSTONE || stack.getItem() == Items.REDSTONE_BLOCK;
        } else if (index == 3) {
            Pair<Integer, Integer> coolant = PowahAPI.getReactorSolidCoolant(stack.getItem());
            return coolant.getLeft() > 0 && coolant.getRight() < 2;
        } else return super.canInsert(index, stack);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (this.world == null) return LazyOptional.empty();
        final ReactorTile reactor = core().orElse(null);
        if (reactor != null) return reactor.getCapability(cap, side);
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return this.holder.cast();
        }
        return super.getCapability(cap, side);
    }
}
