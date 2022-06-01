package owmii.lib.client.renderer.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;
import java.util.Collection;

public interface IBlockColorHolder {
    @OnlyIn(Dist.CLIENT)
    BlockColor getBlockColor();

    static void registerAll(Block... blocks) {
        registerAll(Arrays.asList(blocks));
    }

    static void registerAll(Collection<Block> blocks) {
        blocks.forEach(IBlockColorHolder::register);
    }

    static void register(Block block) {
        if (block instanceof IBlockColorHolder) {
            Minecraft.getInstance().getBlockColors().register(((IBlockColorHolder) block)
                    .getBlockColor(), block);
        }
    }
}
