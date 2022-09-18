package owmii.powah.lib.logistics.fluid;

import dev.architectury.fluid.FluidStack;
import net.minecraft.nbt.CompoundTag;
import org.checkerframework.checker.units.qual.K;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class FluidTank {

	protected Predicate<FluidStack> validator;
	@NotNull
	protected FluidStack fluid = FluidStack.empty();
	protected long capacity;

	public FluidTank(long capacity)
	{
		this(capacity, e -> true);
	}

	public FluidTank(long capacity, Predicate<FluidStack> validator)
	{
		this.capacity = capacity;
		this.validator = validator;
	}

	public FluidTank setCapacity(long capacity)
	{
		this.capacity = capacity;
		return this;
	}

	public FluidTank setValidator(Predicate<FluidStack> validator)
	{
		if (validator != null) {
			this.validator = validator;
		}
		return this;
	}

	public boolean isFluidValid(FluidStack stack)
	{
		return validator.test(stack);
	}

	public long getCapacity()
	{
		return capacity;
	}

	@NotNull
	public FluidStack getFluid()
	{
		return fluid;
	}

	public long getFluidAmount()
	{
		return fluid.getAmount();
	}

	public FluidTank readFromNBT(CompoundTag nbt) {
		FluidStack fluid = FluidStack.read(nbt.getCompound("tank"));
		setFluid(fluid);
		return this;
	}

	public CompoundTag writeToNBT(CompoundTag nbt) {
		if (!fluid.isEmpty()) {
			nbt.put("tank", fluid.write(new CompoundTag()));
		}

		return nbt;
	}

	public int getTanks() {

		return 1;
	}

	@NotNull
	public FluidStack getFluidInTank(int tank) {

		return getFluid();
	}

	public long getTankCapacity(int tank) {

		return getCapacity();
	}

	public boolean isFluidValid(int tank, @NotNull FluidStack stack) {

		return isFluidValid(stack);
	}

	public long fill(FluidStack resource, boolean simulate)
	{
		if (resource.isEmpty() || !isFluidValid(resource))
		{
			return 0;
		}
		if (simulate)
		{
			if (fluid.isEmpty())
			{
				return Math.min(capacity, resource.getAmount());
			}
			if (!fluid.isFluidEqual(resource))
			{
				return 0;
			}
			return Math.min(capacity - fluid.getAmount(), resource.getAmount());
		}
		if (fluid.isEmpty())
		{
			fluid = resource.copyWithAmount(Math.min(capacity, resource.getAmount()));
			onContentsChanged();
			return fluid.getAmount();
		}
		if (!fluid.isFluidEqual(resource))
		{
			return 0;
		}
		long filled = capacity - fluid.getAmount();

		if (resource.getAmount() < filled)
		{
			fluid.grow(resource.getAmount());
			filled = resource.getAmount();
		}
		else
		{
			fluid.setAmount(capacity);
		}
		if (filled > 0)
			onContentsChanged();
		return filled;
	}

	@NotNull
	public FluidStack drain(FluidStack resource, boolean simulate)
	{
		if (resource.isEmpty() || !resource.isFluidEqual(fluid))
		{
			return FluidStack.empty();
		}
		return drain(resource.getAmount(), simulate);
	}

	@NotNull
	public FluidStack drain(long maxDrain, boolean simulate)
	{
		long drained = maxDrain;
		if (fluid.getAmount() < drained)
		{
			drained = fluid.getAmount();
		}
		FluidStack stack = fluid.copyWithAmount(drained);
		if (!simulate && drained > 0)
		{
			fluid.shrink(drained);
			onContentsChanged();
		}
		return stack;
	}

	protected void onContentsChanged()
	{

	}

	public void setFluid(FluidStack stack)
	{
		this.fluid = stack;
	}

	public boolean isEmpty()
	{
		return fluid.isEmpty();
	}

	public long getSpace()
	{
		return Math.max(0, capacity - fluid.getAmount());
	}

}
