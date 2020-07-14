package owmii.powah.block.reactor;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import owmii.lib.util.NBT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Builder {
    private final ReactorTile reactor;
    private List<BlockPos> queue = new ArrayList<>();
    public boolean built;

    private int delay = 5;

    public Builder(ReactorTile reactor) {
        this.reactor = reactor;
    }


    public void read(CompoundNBT nbt) {
        this.built = nbt.getBoolean("built");
        if (!this.built) {
            this.queue = NBT.readPosList(nbt, "queue_pos", new ArrayList<>());
        }
    }

    public void write(CompoundNBT nbt) {
        nbt.putBoolean("built", this.built);
        if (!this.built) {
            NBT.writePosList(nbt, this.queue, "queue_pos");
        }
    }

    public boolean isDone(World world) {
        if (this.built) return true;
        else if (!this.queue.isEmpty()) {
            if (this.delay-- <= 0) {
                Iterator<BlockPos> itr = this.queue.iterator();
                while (itr.hasNext()) {
                    BlockPos pos = itr.next();
                    BlockState state = this.reactor.getBlock().getDefaultState();
                    world.setBlockState(pos, state.with(ReactorBlock.CORE, false), 3);
                    TileEntity tileEntity = world.getTileEntity(pos);
                    if (tileEntity instanceof ReactorPartTile) {
                        ReactorPartTile part = (ReactorPartTile) tileEntity;
                        part.setCorePos(this.reactor.getPos());
                        world.playEvent(2001, pos, Block.getStateId(this.reactor.getBlockState()));
                        itr.remove();
                        this.delay = 5;
                        return false;
                    }
                }
            }
        } else {
            for (Direction side : Direction.values()) {
                if (side.equals(Direction.DOWN)) continue;
                BlockPos pos = this.reactor.getPos().offset(side).up(side.equals(Direction.UP) ? 2 : 0);
                TileEntity tileEntity = world.getTileEntity(pos);
                if (tileEntity instanceof ReactorPartTile) {
                    ReactorPartTile part = (ReactorPartTile) tileEntity;
                    part.setExtractor(true);
                }
            }
            for (BlockPos pos : getPosList()) {
                TileEntity tileEntity = world.getTileEntity(pos);
                if (tileEntity instanceof ReactorPartTile) {
                    ReactorPartTile part = (ReactorPartTile) tileEntity;
                    part.setBuilt(true);
                    part.sync();
                }
            }
            this.built = true;
            this.reactor.sync();
        }
        return false;
    }

    public void shuffle() {
        this.queue.addAll(getPosList());
        Collections.shuffle(this.queue);
    }

    public void demolish(World world) {
        List<BlockPos> list = getPosList();
        list.add(this.reactor.getPos());
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            BlockPos blockPos = list.get(i);
            if (world.getBlockState(blockPos).getBlock().equals(this.reactor.getBlock())) {
                count++;
                world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 3);
            }
        }
        Block.spawnAsEntity(world, this.reactor.getPos(), new ItemStack(this.reactor.getBlock(), count + this.queue.size()));
        world.setBlockState(this.reactor.getPos(), Blocks.AIR.getDefaultState(), 3);
        this.queue.clear();
    }

    public List<BlockPos> getPosList() {
        final BlockPos pos = this.reactor.getPos();
        return BlockPos.getAllInBox(pos.add(-1, 0, -1), pos.add(1, 3, 1))
                .map(BlockPos::toImmutable)
                .filter(pos1 -> !pos1.equals(pos))
                .collect(Collectors.toList());
    }
}
