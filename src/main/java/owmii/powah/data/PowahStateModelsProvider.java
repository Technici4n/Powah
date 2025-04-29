package owmii.powah.data;

import java.util.Set;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import owmii.powah.Powah;
import owmii.powah.block.Blcks;

public class PowahStateModelsProvider extends BlockStateProvider {

    public PowahStateModelsProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Powah.MOD_ID, exFileHelper);
    }

    private static Set<Block> cubeAllBlocks = Set.of(
        Blcks.DRY_ICE.get(),
        Blcks.ENERGIZED_STEEL.get(),
        Blcks.BLAZING_CRYSTAL.get(),
        Blcks.NIOTIC_CRYSTAL.get(),
        Blcks.NITRO_CRYSTAL.get(),
        Blcks.SPIRITED_CRYSTAL.get(),
        Blcks.URANINITE.get(),
        Blcks.URANINITE_ORE.get(),
        Blcks.URANINITE_ORE_DENSE.get(),
        Blcks.URANINITE_ORE_POOR.get(),
        Blcks.DEEPSLATE_URANINITE_ORE.get(),
        Blcks.DEEPSLATE_URANINITE_ORE_DENSE.get(),
        Blcks.DEEPSLATE_URANINITE_ORE_POOR.get()
    );

    @Override
    protected void registerStatesAndModels() {
        cubeAllBlocks.forEach(b->cubeBlock(b));

    }

    private void cubeBlock(Block b) {
        simpleBlock(b,
                models().cubeAll(b.getDescriptionId()
                        .replace("block." + Powah.MOD_ID + ".", ""), blockTexture(b)));
    }

    private void existingParent(Block b, String parentMod, String parentName, String texName, String texture) {
        simpleBlock(b, models().withExistingParent(parentMod, parentName).texture(texName, texture));
    }

}
