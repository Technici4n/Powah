package zeroneye.powah.block.generator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import zeroneye.powah.block.PowahBlock;

public class GeneratorBlock extends PowahBlock {
    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;
    protected final int perTick;

    public GeneratorBlock(Properties properties, int capacity, int transfer, int perTick) {
        super(properties, capacity, transfer);
        this.perTick = perTick;
    }

    public int perTick() {
        return perTick;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        if (hasLitProp()) {
            builder.add(LIT);
        }
    }

    protected boolean hasLitProp() {
        return false;
    }
}
