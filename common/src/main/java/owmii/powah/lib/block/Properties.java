package owmii.powah.lib.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class Properties {

    public static Block.Properties rock(float hardness, float resistance) {
        return Block.Properties.of(Material.STONE)
                .strength(hardness, resistance).requiresCorrectToolForDrops();
    }

    public static Block.Properties deepslate() {
        return BlockBehaviour.Properties.of(Material.STONE).color(MaterialColor.DEEPSLATE).strength(4.5f, 3.0f).sound(SoundType.DEEPSLATE)
                .requiresCorrectToolForDrops();
    }

    public static Block.Properties wood(float hardness, float resistance) {
        return Block.Properties.of(Material.WOOD)
                .strength(hardness, resistance)
                .sound(SoundType.WOOD);
    }

    public static Block.Properties metal(float hardness, float resistance) {
        return Block.Properties.of(Material.METAL)
                .strength(hardness, resistance)
                .sound(SoundType.METAL).requiresCorrectToolForDrops();
    }

    public static Block.Properties metalNoSolid(float hardness, float resistance) {
        return metal(hardness, resistance).noOcclusion();
    }
}
