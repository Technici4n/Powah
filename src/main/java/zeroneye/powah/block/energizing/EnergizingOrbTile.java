package zeroneye.powah.block.energizing;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import zeroneye.lib.block.TileBase;
import zeroneye.lib.client.particle.Effect;
import zeroneye.lib.client.particle.Effects;
import zeroneye.lib.util.math.V3d;
import zeroneye.powah.api.recipe.energizing.EnergizingRecipeSorter;
import zeroneye.powah.api.recipe.energizing.IEnergizingRecipe;
import zeroneye.powah.block.ITiles;

import javax.annotation.Nullable;

public class EnergizingOrbTile extends TileBase.Tickable {
    private boolean containRecipe;
    private int requiredEnergy;
    private int energy;

    @Nullable
    private IEnergizingRecipe recipe;

    public EnergizingOrbTile() {
        super(ITiles.ENERGIZING_ORB);
        this.inv.set(7);
    }

    @Override
    public void readSync(CompoundNBT compound) {
        super.readSync(compound);
        this.containRecipe = compound.getBoolean("ContainRecipe");
        this.requiredEnergy = compound.getInt("RequiredEnergy");
        this.energy = compound.getInt("Energy");
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT compound) {
        compound.putBoolean("ContainRecipe", this.containRecipe);
        compound.putInt("RequiredEnergy", this.requiredEnergy);
        compound.putInt("Energy", this.energy);
        return super.writeSync(compound);
    }

    @Nullable
    public IEnergizingRecipe currRecipe() {
        return this.recipe;
    }

    @Override
    protected void onFirstTick() {
        super.onFirstTick();
        checkRecipe();
    }

    @Override
    public void onSlotChanged(int index) {
        if (isServerWorld()) {
            this.energy = 0;
            checkRecipe();
        }
    }

    private void checkRecipe() {
        if (this.world != null && !this.world.isRemote) {
            this.recipe = EnergizingRecipeSorter.get(this.inv, this.world, this.pos);
            if (this.recipe != null) {
                this.requiredEnergy = this.recipe.getEnergy();
            } else {
                this.requiredEnergy = 0;
            }
            setContainRecipe(this.recipe != null);
        }
        sync(1);
    }

    public int fillEnergy(int amount) {
        int filled = Math.min(this.requiredEnergy - this.energy, amount);
        if (this.world != null) {
            if (this.recipe != null) {
                this.energy += filled;
                if (this.energy >= this.requiredEnergy) {
                    ItemStack stack = this.recipe.getOutput();
                    this.inv.clear();
                    this.inv.setStack(0, stack.copy());
                    this.requiredEnergy = 0;
                    this.energy = 0;
                    markDirty();
                }
            }
            if (this.containRecipe && this.world.isRemote) {
                if (Math.random() < 0.2D) {
                    Effects.create(Effect.GLOW_SMALL, this.world, new V3d(this.pos)
                            .east(0.5D + Math.random() * 0.3D - Math.random() * 0.3D)
                            .south(0.5D + Math.random() * 0.3D - Math.random() * 0.3D)
                            .up(0.5D + Math.random() * 0.3D - Math.random() * 0.3D))
                            .scale(0, 3, 0).alpha(0.7F, 2).color(0xac7fbd)
                            .maxAge(20).blend().spawn();
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

    public int getEnergy() {
        return this.energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
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
