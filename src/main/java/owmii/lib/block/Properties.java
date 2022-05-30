package owmii.lib.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class Properties {
    public static Block.Properties rock(float hardnessAndResistance) {
        return rock(hardnessAndResistance, hardnessAndResistance);

    }

    public static Block.Properties rock(float hardness, float resistance) {
        return Block.Properties.create(Material.ROCK)
                .hardnessAndResistance(hardness, resistance).setRequiresTool();
    }

    public static Block.Properties wood(float hardnessAndResistance) {
        return wood(hardnessAndResistance, hardnessAndResistance);

    }

    public static Block.Properties wood(float hardness, float resistance) {
        return Block.Properties.create(Material.WOOD)
                .hardnessAndResistance(hardness, resistance)
                .sound(SoundType.WOOD);
    }

    public static Block.Properties metal(float hardnessAndResistance) {
        return metal(hardnessAndResistance, hardnessAndResistance);

    }

    public static Block.Properties metal(float hardness, float resistance) {
        return Block.Properties.create(Material.IRON)
                .hardnessAndResistance(hardness, resistance)
                .sound(SoundType.METAL).setRequiresTool();
    }

    public static Block.Properties rockNoSolid(float hardnessAndResistance) {
        return rockNoSolid(hardnessAndResistance, hardnessAndResistance);

    }

    public static Block.Properties rockNoSolid(float hardness, float resistance) {
        return rock(hardness, resistance).notSolid();
    }

    public static Block.Properties woodNoSolid(float hardnessAndResistance) {
        return woodNoSolid(hardnessAndResistance, hardnessAndResistance);

    }

    public static Block.Properties woodNoSolid(float hardness, float resistance) {
        return wood(hardness, resistance).notSolid();
    }

    public static Block.Properties metalNoSolid(float hardnessAndResistance) {
        return metalNoSolid(hardnessAndResistance, hardnessAndResistance);

    }

    public static Block.Properties metalNoSolid(float hardness, float resistance) {
        return metal(hardness, resistance).notSolid();
    }
}
