package owmii.powah.block.energizing;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import owmii.lib.block.TileBase;
import owmii.lib.util.IVariant;
import owmii.powah.block.ITiles;
import owmii.powah.recipe.Recipes;

import javax.annotation.Nullable;
import java.util.Optional;

public class EnergizingOrbTile extends TileBase.Tickable<IVariant.Single, EnergizingOrbBlock> {
    private boolean containRecipe;
    private long requiredEnergy;
    private long energy;

    @Nullable
    private EnergizingRecipe recipe;

    public EnergizingOrbTile() {
        super(ITiles.ENERGIZING_ORB);
        this.inv.set(7);
    }

    @Override
    public void readSync(CompoundNBT compound) {
        super.readSync(compound);
        this.containRecipe = compound.getBoolean("ContainRecipe");
        this.requiredEnergy = compound.getLong("RequiredEnergy");
        this.energy = compound.getLong("EnergyBuffer");
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT compound) {
        compound.putBoolean("ContainRecipe", this.containRecipe);
        compound.putLong("RequiredEnergy", this.requiredEnergy);
        compound.putLong("EnergyBuffer", this.energy);
        return super.writeSync(compound);
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
            this.energy = 0;
            checkRecipe();
        }
    }

    private void checkRecipe() {
        if (this.world != null && !this.world.isRemote) {
            Optional<EnergizingRecipe> recipe = this.world.getRecipeManager().getRecipe(Recipes.ENERGIZING, new RecipeWrapper(getInventory()), this.world);
            if (recipe.isPresent()) {
                this.recipe = recipe.get();
                this.requiredEnergy = this.recipe.getEnergy();
            } else {
                this.requiredEnergy = 0;
            }
            setContainRecipe(recipe.isPresent());
            sync(1);
        }
    }

    public long fillEnergy(long amount) {
        long filled = Math.min(this.requiredEnergy - this.energy, amount);
        if (this.world != null) {
            if (this.recipe != null) {
                this.energy += filled;
                if (this.energy >= this.requiredEnergy) {
                    ItemStack stack = this.recipe.getRecipeOutput();
                    this.inv.clear();
                    this.inv.setStack(0, stack.copy());
                    this.requiredEnergy = 0;
                    this.energy = 0;
                    markDirty();
                }
            }
//            if (this.containRecipe && this.world.isRemote) { TODO : energizing particles
//                if (Math.random() < 0.2D) {
//                    Effects.create(Effect.GLOW_SMALL, this.world, new V3d(this.pos)
//                            .east(0.5D + Math.random() * 0.3D - Math.random() * 0.3D)
//                            .south(0.5D + Math.random() * 0.3D - Math.random() * 0.3D)
//                            .up(0.5D + Math.random() * 0.3D - Math.random() * 0.3D))
//                            .scale(0, 3, 0).alpha(0.7F, 2).color(0xac7fbd)
//                            .maxAge(20).blend().spawn();
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

    public long getEnergy() {
        return this.energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
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
