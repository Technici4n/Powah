package owmii.powah.block.energizing;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import owmii.powah.Powah;
import owmii.powah.block.Tiles;
import owmii.powah.lib.block.IInventoryHolder;
import owmii.powah.lib.block.PowahBaseTickingBlockEntity;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.lib.logistics.inventory.Inventory;
import owmii.powah.recipe.Recipes;

public class EnergizingOrbBlockEntity extends PowahBaseTickingBlockEntity<EnergizingOrbBlock> implements IInventoryHolder {
    private final Energy buffer = Energy.create(0);
    private boolean containRecipe;

    @Nullable
    private RecipeHolder<EnergizingRecipe> recipe;

    public EnergizingOrbBlockEntity(BlockPos pos, BlockState state) {
        super(Tiles.ENERGIZING_ORB.get(), pos, state);
    }

    @Override
    protected int getInternalInventorySize() {
        return 7;
    }

    @Override
    public void readSync(ValueInput input) {
        super.readSync(input);
        this.buffer.read(input, "buffer", true, false);
        this.buffer.setTransfer(this.buffer.getCapacity());
        this.containRecipe = input.getBooleanOr("contain_recipe", false);
    }

    @Override
    public void writeSync(ValueOutput output) {
        this.buffer.write(output, "buffer", true, false);
        output.putBoolean("contain_recipe", this.containRecipe);
        super.writeSync(output);
    }

    public Direction getOrbUp() {
        if (this.level != null) {
            BlockState state = this.getBlockState();
            if (state.hasProperty(BlockStateProperties.FACING)) {
                return state.getValue(BlockStateProperties.FACING).getOpposite();
            }
        }
        return Direction.UP;
    }

    public Vec3 getOrbCenter() {
        Direction up = getOrbUp();
        double scale = 0.1;
        return Vec3.atCenterOf(this.worldPosition).add(up.getStepX() * scale, up.getStepY() * scale, up.getStepZ() * scale);
    }

    @Nullable
    public RecipeHolder<EnergizingRecipe> currRecipe() {
        return this.recipe;
    }

    @Override
    protected void onFirstTick(Level world) {
        super.onFirstTick(world);
        checkRecipe();
    }

    @Override
    public void onSlotChanged(int index) {
        if (!isRemote()) {
            this.buffer.setCapacity(0);
            this.buffer.setStored(0);
            this.buffer.setTransfer(0);
            checkRecipe();
        }
    }

    record OrbInput(Inventory inventory) implements RecipeInput {
        @Override
        public ItemStack getItem(int index) {
            return inventory.getStackInSlot(index).copy();
        }

        @Override
        public int size() {
            return inventory.size();
        }
    }

    private void checkRecipe() {
        if (this.level instanceof ServerLevel serverLevel) {
            var recipe = serverLevel.recipeAccess().getRecipeFor(Recipes.ENERGIZING.get(),
                    new OrbInput(getInventory()), this.level);
            if (recipe.isPresent()) {
                this.recipe = recipe.get();
                this.buffer.setCapacity(this.recipe.value().getScaledEnergy());
                this.buffer.setTransfer(this.recipe.value().getScaledEnergy());
            } else {
                this.buffer.setCapacity(0);
                this.buffer.setStored(0);
                this.buffer.setTransfer(0);
            }
            setContainRecipe(recipe.isPresent());
            sync(1);
        }
    }

    public long fillEnergy(long amount) {
        long filled = Math.min(this.buffer.getEmpty(), amount);
        if (this.level != null) {
            if (this.recipe != null) {
                this.buffer.produce(filled);
                if (this.buffer.isFull()) {
                    ItemStack stack = this.recipe.value().getResultItem().create();
                    this.inv.clear();
                    this.inv.setStackInSlot(0, stack.copy());
                    this.buffer.setCapacity(0);
                    this.buffer.setStored(0);
                    this.buffer.setTransfer(0);
                    setChanged();
                }
                sync(5);
            }

//            if (this.containRecipe && this.world.isRemote) {
//                if (Math.random() < 0.2D) {
//                    BlockPos pos = getPos();
//                    double x = pos.getX() + 0.5D + Math.random() * 0.3D - Math.random() * 0.3D;
//                    double y = pos.getY() + 0.1D + 0.5D + Math.random() * 0.3D - Math.random() * 0.3D;
//                    double z = pos.getZ() + 0.5D + Math.random() * 0.3D - Math.random() * 0.3D;
//                    this.world.addParticle(Particles.ENERGIZING, x, y, z, 0.0D, 0.0D, 0.0D);
//                }
//            }
        }
        return filled;
    }

    public boolean containRecipe() {
        return this.containRecipe;
    }

    public void setContainRecipe(boolean containRecipe) {
        this.containRecipe = containRecipe;
    }

    public Energy getBuffer() {
        return this.buffer;
    }

    @Override
    public int getSlotLimit(int index) {
        return index == 0 ? 64 : 1;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack) {
        return slot == 0;
    }

    @Override
    public boolean canInsert(int index, ItemStack stack) {
        return index != 0 && this.inv.getStackInSlot(0).isEmpty() && this.inv.getStackInSlot(index).isEmpty();
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);

        int range = Powah.config().general.energizing_range;
        List<BlockPos> list = BlockPos.betweenClosedStream(pos.offset(-range, -range, -range), pos.offset(range, range, range))
                .map(BlockPos::immutable)
                .filter(pos1 -> !pos.equals(pos1))
                .toList();

        list.forEach(pos1 -> {
            BlockEntity tileEntity1 = getLevel().getBlockEntity(pos1);
            if (tileEntity1 instanceof EnergizingRodBlockEntity rod) {
                if (pos.equals(rod.getOrbPos())) {
                    rod.setOrbPos(BlockPos.ZERO);
                }
            }
        });

        list.forEach(pos1 -> {
            BlockState state1 = getLevel().getBlockState(pos1);
            if (state1.getBlock() instanceof EnergizingOrbBlock otherOrb) {
                otherOrb.search(getLevel(), pos1);
            }
        });
    }
}
