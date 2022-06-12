package owmii.powah.block.energizing;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import owmii.powah.lib.block.AbstractTickableTile;
import owmii.powah.lib.block.IInventoryHolder;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.lib.logistics.inventory.RecipeWrapper;
import owmii.powah.lib.registry.IVariant;
import owmii.powah.block.Tiles;
import owmii.powah.recipe.Recipes;

import javax.annotation.Nullable;
import java.util.Optional;

public class EnergizingOrbTile extends AbstractTickableTile<IVariant.Single, EnergizingOrbBlock> implements IInventoryHolder {
    private final Energy buffer = Energy.create(0);
    private boolean containRecipe;

    @Nullable
    private EnergizingRecipe recipe;

    public EnergizingOrbTile(BlockPos pos, BlockState state) {
        super(Tiles.ENERGIZING_ORB.get(), pos, state);
        this.inv.set(7);
    }

    @Override
    public void readSync(CompoundTag nbt) {
        super.readSync(nbt);
        this.buffer.read(nbt, "buffer", true, false);
        this.buffer.setTransfer(this.buffer.getCapacity());
        this.containRecipe = nbt.getBoolean("contain_recipe");
    }

    @Override
    public CompoundTag writeSync(CompoundTag nbt) {
        this.buffer.write(nbt, "buffer", true, false);
        nbt.putBoolean("contain_recipe", this.containRecipe);
        return super.writeSync(nbt);
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
    public EnergizingRecipe currRecipe() {
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

    private void checkRecipe() {
        if (this.level != null && !isRemote()) {
            Optional<EnergizingRecipe> recipe = this.level.getRecipeManager().getRecipeFor(Recipes.ENERGIZING.get(), new RecipeWrapper(getInventory()), this.level);
            if (recipe.isPresent()) {
                this.recipe = recipe.get();
                this.buffer.setCapacity(this.recipe.getEnergy());
                this.buffer.setTransfer(this.recipe.getEnergy());
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
                    ItemStack stack = this.recipe.getResultItem();
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
}
