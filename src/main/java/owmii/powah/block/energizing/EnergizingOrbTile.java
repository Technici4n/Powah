package owmii.powah.block.energizing;

import java.util.Comparator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import owmii.powah.block.Tiles;
import owmii.powah.lib.block.AbstractTickableTile;
import owmii.powah.lib.block.IInventoryHolder;
import owmii.powah.lib.logistics.energy.Energy;
import owmii.powah.lib.logistics.inventory.Inventory;
import owmii.powah.lib.registry.IVariant;
import owmii.powah.recipe.Recipes;
import owmii.powah.util.Stack;

public class EnergizingOrbTile extends AbstractTickableTile<IVariant.Single, EnergizingOrbBlock> implements IInventoryHolder {
    private static final Comparator<RecipeHolder<EnergizingRecipe>> RECIPE_ORDER = Comparator
            .<RecipeHolder<EnergizingRecipe>>comparingInt(holder -> holder.value().getIngredients().size()).reversed()
            .thenComparing(holder -> holder.id().getNamespace())
            .thenComparing(holder -> holder.id().getPath());

    private final Energy buffer = Energy.create(0);
    private boolean containRecipe;
    @Nullable
    private int[] activeCounts;
    private boolean outputFull;
    private ItemStack outputFilter = ItemStack.EMPTY;
    @Nullable
    private ResourceLocation recipeId;
    @Nullable
    private RecipeManager recipeManager;

    @Nullable
    private RecipeHolder<EnergizingRecipe> recipe;

    public EnergizingOrbTile(BlockPos pos, BlockState state) {
        super(Tiles.ENERGIZING_ORB.get(), pos, state);
        this.inv.set(7);
    }

    @Override
    public void readSync(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readSync(nbt, registries);
        this.buffer.read(nbt, "buffer", true, false);
        this.buffer.setTransfer(this.buffer.getCapacity());
        this.containRecipe = nbt.getBoolean("contain_recipe");
        recipeId = nbt.contains("active_recipe") ? ResourceLocation.tryParse(nbt.getString("active_recipe")) : null;
        int[] counts = nbt.getIntArray("active_counts");
        activeCounts = counts.length == inv.getSlots() ? counts : null;
        // Earlier batching saves reserved one item per set bit.
        int slots = nbt.getInt("active_slots");
        if (activeCounts == null && slots != 0) {
            activeCounts = new int[inv.getSlots()];
            for (int i = 1; i < activeCounts.length; i++) {
                activeCounts[i] = (slots >> i) & 1;
            }
        }
        outputFull = nbt.getBoolean("output_full");
        outputFilter = ItemStack.parseOptional(registries, nbt.getCompound("output_filter"));
        recipe = null;
        recipeManager = null;
    }

    @Override
    public CompoundTag writeSync(CompoundTag nbt, HolderLookup.Provider registries) {
        this.buffer.write(nbt, "buffer", true, false);
        if (recipeId != null) {
            nbt.putString("active_recipe", recipeId.toString());
        }
        if (activeCounts != null) {
            nbt.putIntArray("active_counts", activeCounts);
        }
        nbt.putBoolean("contain_recipe", this.containRecipe);
        nbt.putBoolean("output_full", outputFull);
        if (!outputFilter.isEmpty()) {
            nbt.put("output_filter", outputFilter.save(registries));
        }
        return super.writeSync(nbt, registries);
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
    protected int postTick(Level world) {
        if (!world.isClientSide) {
            // Also picks up new, changed or removed datapack recipes after /reload.
            if (recipeManager != world.getRecipeManager()) {
                checkRecipe();
            }
            if (recipe != null && containRecipe() && !outputFull && buffer.isFull()) {
                finishBatch();
            }
        }
        return -1;
    }

    @Override
    public void onSlotChanged(int index) {
        checkRecipe();
    }

    record OrbInput(Inventory inventory, int[] counts) implements RecipeInput {
        @Override
        public ItemStack getItem(int index) {
            return counts[index] == 0 ? ItemStack.EMPTY : inventory.getStackInSlot(index).copyWithCount(counts[index]);
        }

        @Override
        public int size() {
            return inventory.getSlots();
        }
    }

    private void checkRecipe() {
        if (level == null || isRemote()) {
            return;
        }
        var manager = level.getRecipeManager();
        if (recipeId != null && (recipe == null || manager != recipeManager)) {
            recipe = manager.getAllRecipesFor(Recipes.ENERGIZING.get()).stream().filter(holder -> holder.id().equals(recipeId)).findFirst()
                    .orElse(null);
        }
        // Old saves have the buffer but no recipe ID. Their recipe used every occupied input.
        boolean legacy = recipeManager == null && recipeId == null && inv.isSlotEmpty(0) && buffer.hasEnergy();
        recipeManager = manager;
        if (recipe == null || !hasReservedInputs() || !recipe.value().matches(new OrbInput(inv, activeCounts), level)) {
            var candidates = manager.getAllRecipesFor(Recipes.ENERGIZING.get());
            if (legacy) {
                int inputCount = inv.getNonEmptyStacks().size();
                selectBatch(candidates.stream().filter(holder -> holder.value().getScaledEnergy() == buffer.getCapacity()
                        && holder.value().getIngredients().size() == inputCount).toList());
            }
            if (!legacy || recipe == null) {
                buffer.setStored(0);
                selectBatch(candidates);
            }
        }
        recipeId = recipe == null ? null : recipe.id();
        buffer.setAll(recipe == null ? 0 : recipe.value().getScaledEnergy());
        outputFull = recipe != null && !canFit(recipe.value().getResultItem(level.registryAccess()));
        setContainRecipe(recipe != null && !outputFull);
        setChanged();
        sync(1);
    }

    private void selectBatch(Iterable<RecipeHolder<EnergizingRecipe>> recipes) {
        recipe = null;
        activeCounts = null;
        for (var candidate : recipes) {
            if (recipe != null && RECIPE_ORDER.compare(recipe, candidate) <= 0) {
                continue;
            }
            if (!outputFilter.isEmpty() && !Stack.equals(outputFilter, candidate.value().getResultItem(level.registryAccess()))) {
                continue;
            }
            int count = candidate.value().getIngredients().size();
            if (count == 0 || count >= inv.getSlots()) {
                continue;
            }
            int[] counts = new int[inv.getSlots()];
            if (matchInputs(candidate.value(), counts, 1, count)) {
                recipe = candidate;
                activeCounts = counts;
            }
        }
    }

    private boolean hasReservedInputs() {
        if (activeCounts == null) {
            return false;
        }
        for (int slot = 1; slot < inv.getSlots(); slot++) {
            if (activeCounts[slot] < 0 || activeCounts[slot] > inv.getStackInSlot(slot).getCount()) {
                return false;
            }
        }
        return true;
    }

    private boolean matchInputs(EnergizingRecipe candidate, int[] counts, int slot, int remaining) {
        if (remaining == 0) {
            return candidate.matches(new OrbInput(inv, counts), level);
        }
        if (slot == inv.getSlots()) {
            return false;
        }
        // Present just one batch to the recipe, keeping the input slots in their original positions.
        int max = Math.min(remaining, inv.getStackInSlot(slot).getCount());
        for (int count = max; count >= 0; count--) {
            counts[slot] = count;
            if (matchInputs(candidate, counts, slot + 1, remaining - count)) {
                return true;
            }
        }
        return false;
    }

    private boolean canFit(ItemStack result) {
        ItemStack output = inv.getStackInSlot(0);
        int limit = Math.min(getSlotLimit(0), result.getMaxStackSize());
        return (output.isEmpty() || Stack.equals(output, result))
                && result.getCount() <= limit - output.getCount();
    }

    public long fillEnergy(long amount) {
        if (level == null || level.isClientSide || amount <= 0) {
            return 0;
        }
        if (recipeManager != level.getRecipeManager()) {
            checkRecipe();
        }
        if (recipe == null || !containRecipe() || outputFull) {
            return 0;
        }
        long filled = buffer.produce(amount);
        if (buffer.isFull()) {
            finishBatch();
        } else {
            setChanged();
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
        return filled;
    }

    private void finishBatch() {
        ItemStack result = recipe.value().getResultItem(level.registryAccess());
        ItemStack merged = result.copyWithCount(inv.getStackInSlot(0).getCount() + result.getCount());
        for (int slot = 1; slot < inv.getSlots(); slot++) {
            if (activeCounts[slot] > 0) {
                shrink(slot, activeCounts[slot]);
            }
        }
        recipe = null;
        recipeId = null;
        activeCounts = null;
        buffer.setStored(0);
        inv.setStackInSlot(0, merged); // Recheck the next batch after all inputs have been consumed.
    }

    public boolean containRecipe() {
        return this.containRecipe;
    }

    public void setContainRecipe(boolean containRecipe) {
        this.containRecipe = containRecipe;
    }

    public int getActiveCount(int slot) {
        return activeCounts == null ? 0 : activeCounts[slot];
    }

    public boolean hasActiveBatch() {
        return activeCounts != null;
    }

    public boolean isOutputFull() {
        return outputFull;
    }

    public ItemStack getOutputFilter() {
        return outputFilter;
    }

    public void setOutputFilter(ItemStack sample) {
        outputFilter = sample.copyWithCount(1);
        checkRecipe();
    }

    public Energy getBuffer() {
        return this.buffer;
    }

    @Override
    public int getSlotLimit(int index) {
        return index == 0 ? 64 : Item.ABSOLUTE_MAX_STACK_SIZE;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack) {
        return slot == 0;
    }

    @Override
    public boolean canInsert(int index, ItemStack stack) {
        if (index == 0 || stack.isEmpty()) {
            return false;
        }
        // Leave the other input slots available for different items, even when this stack is full.
        for (int slot = 1; slot < inv.getSlots(); slot++) {
            if (slot != index && inv.getStackInSlot(slot).is(stack.getItem())) {
                return false;
            }
        }
        return true;
    }
}
