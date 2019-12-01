package zeroneye.powah.block.cable;

import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import zeroneye.lib.util.Energy;
import zeroneye.powah.block.ITiles;
import zeroneye.powah.block.PowahTile;

import java.util.HashMap;
import java.util.Map;

public class CableTile extends PowahTile {
    public final Map<Direction, LinkedCables> linkedCables = new HashMap<>();
    public boolean[] flags = new boolean[6];

    public CableTile(int maxReceive, int maxExtract, boolean isCreative) {
        super(ITiles.CABLE, 0, maxReceive, maxExtract, isCreative);
        for (Direction side : Direction.values()) {
            this.linkedCables.put(side, LinkedCables.create());
        }
    }

    public CableTile() {
        this(0, 0, false);
    }

    @Override
    public void readSync(CompoundNBT compound) {
        super.readSync(compound);
        ListNBT list = compound.getList("LinkedCables", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundNBT nbt = list.getCompound(i);
            Direction direction = Direction.values()[nbt.getInt("Direction")];
            this.linkedCables.put(direction, LinkedCables.create().read(nbt));
        }
    }

    @Override
    public CompoundNBT writeSync(CompoundNBT compound) {
        ListNBT list = new ListNBT();
        this.linkedCables.forEach((direction, linkedCables) -> {
            CompoundNBT nbt = new CompoundNBT();
            linkedCables.write(nbt);
            nbt.putInt("Direction", direction.ordinal());
            list.add(nbt);
        });
        compound.put("LinkedCables", list);
        return super.writeSync(compound);
    }

    @Override
    public boolean isNBTStorable() {
        return false;
    }

    @Override
    protected boolean postTicks() {
        if (this.world == null) return false;
        final int[] extracted = {0};
        if (!this.world.isRemote) {
            for (Direction direction : Direction.values()) {
                BlockPos handlerPos = this.pos.offset(direction);
                TileEntity handlerTile = this.world.getTileEntity(handlerPos);
                Energy.getForgeEnergy(handlerTile, direction).ifPresent(storage -> {
                    if (storage.canExtract() && canReceive(direction)) {
                        LinkedCables cables = this.linkedCables.get(direction);
                        cables.cables().forEach(cablePos -> {
                            int amount = Math.min(this.internal.getMaxExtract() - extracted[0], storage.getEnergyStored());
                            TileEntity cableTile = this.world.getTileEntity(cablePos);
                            if (cableTile instanceof CableTile) {
                                CableTile cable = (CableTile) cableTile;
                                for (Direction side : Direction.values()) {
                                    if (amount > 0) {
                                        if (cable.canExtract(side)) {
                                            TileEntity receiver = this.world.getTileEntity(cablePos.offset(side));
                                            int received = Energy.receive(receiver, side.getOpposite(), amount, false);
                                            extracted[0] += storage.extractEnergy(received, false);
                                        }
                                    }
                                }
                            }
                        });

                        int amount = Math.min(this.internal.getMaxExtract() - extracted[0], storage.getEnergyStored());
                        for (Direction side : Direction.values()) {
                            if (amount > 0) {
                                if (canExtract(side)) {
                                    if (!side.equals(direction)) {
                                        int received = Energy.receive(this.world, this.pos.offset(side), side.getOpposite(), amount, false);
                                        extracted[0] += storage.extractEnergy(received, false);
                                    }
                                }
                            }
                        }
                    }
                });

            }
        }
        return extracted[0] > 0;
    }

    public void search(Block block, Direction side) {
        this.linkedCables.get(side).search(block, this, side).clear();
    }
}
