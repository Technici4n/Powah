package owmii.powah.lib.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class Properties {

    public static Block.Properties rock(float hardness, float resistance) {
        return Block.Properties.of()
                .mapColor(MapColor.STONE)
                .sound(SoundType.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .strength(hardness, resistance).requiresCorrectToolForDrops();
    }

    public static Block.Properties deepslate() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.DEEPSLATE)
                .sound(SoundType.DEEPSLATE)
                .strength(4.5f, 3.0f)
                .requiresCorrectToolForDrops();
    }

    public static Block.Properties wood(float hardness, float resistance) {
        return Block.Properties.of()
                .mapColor(MapColor.WOOD)
                .sound(SoundType.WOOD)
                .ignitedByLava()
                .instrument(NoteBlockInstrument.BASS)
                .strength(hardness, resistance);
    }

    public static Block.Properties metal(float hardness, float resistance) {
        return Block.Properties.of()
                .mapColor(MapColor.METAL)
                .sound(SoundType.METAL)
                .strength(hardness, resistance)
                .requiresCorrectToolForDrops();
    }

    public static Block.Properties metalNoSolid(float hardness, float resistance) {
        return metal(hardness, resistance).noOcclusion();
    }
}
