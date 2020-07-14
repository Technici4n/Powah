package owmii.powah.block.energizing;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import owmii.lib.block.AbstractTickableTile;
import owmii.lib.block.IInventoryHolder;
import owmii.lib.block.IVariant;
import owmii.lib.logistics.energy.Energy;
import owmii.powah.block.ITiles;
import owmii.powah.recipe.Recipes;

import javax.annotation.Nullable;
import java.util.Optional;

public class EnergizingOrbTile extends AbstractTickableTile<IVariant.Single, EnergizingOrbBlock> implements IInventoryHolder {
    private final Energy buffer = Energy.create(0);
    private boolean containRecipe;

    @Nullable
    private EnergizingRecipe recipe;

    public EnergizingOrbTile() {
        super(ITiles.ENERGIZING_ORB);
        this.inv.set(7);
    }

    @Override
    public void readSync(CompoundNBT nbt) {
        super.readSync(nbt);
        this.buffer.read(nbt, "buffer", true, false);
        this.buffer.setTransfer(this.buffer.getCapacity());
        this.containRecipe = nbt.getBoolean("contain_recipe");
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT nbt) {
        this.buffer.write(nbt, "buffer", true, false);
        nbt.putBoolean("contain_recipe", this.containRecipe);
        return super.writeSync(nbt);
    }

    @Nullable
    public EnergizingRecipe currRecipe() {
        return this.recipe;
    }

    @Override
    protected void onFirstTick(World world) {
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
        if (this.world != null && !isRemote()) {
            Optional<EnergizingRecipe> recipe = this.world.getRecipeManager().getRecipe(Recipes.ENERGIZING, new RecipeWrapper(getInventory()), this.world);
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
        if (this.world != null) {
            if (this.recipe != null) {
                this.buffer.produce(filled);
                if (this.buffer.isFull()) {
                    ItemStack stack = this.recipe.getRecipeOutput();
                    this.inv.clear();
                    this.inv.setStack(0, stack.copy());
                    this.buffer.setCapacity(0);
                    this.buffer.setStored(0);
                    this.buffer.setTransfer(0);
                    markDirty();
                }
            }
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
