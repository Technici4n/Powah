package owmii.powah.data;

import java.util.Set;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import owmii.powah.Powah;
import owmii.powah.block.Blcks;
import owmii.powah.block.Tier;

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
            Blcks.DEEPSLATE_URANINITE_ORE_POOR.get());

    @Override
    protected void registerStatesAndModels() {

        // cubeAll blocks
        cubeAllBlocks.forEach(b -> cubeBlock(b));

        // special blocks
        directionalBlockInverse(Blcks.ENERGIZING_ORB.get(),
                models().getExistingFile(getResource("block/energizing_orb")));

        // tiered blocks (starter->nitro)
        for (Tier tier : Tier.getNormalVariants()) {
            enderCell(tier);
            enderGate(tier);
            thermoGen(tier);
            energizedRod(tier);
            //energyCable(tier);
           
        }

        // tiered blocks (starter->creative)
        for (Tier tier : new Tier[] { Tier.STARTER, Tier.BASIC, Tier.HARDENED, Tier.BLAZING, Tier.NIOTIC, Tier.SPIRITED,
                Tier.NITRO, Tier.CREATIVE }) {
            energyCell(tier);
        }

    }

    // TODO "powah: -> Powah.MOD_ID+"

    private void enderCell(Tier tier) {
        var BLOCK = "ender_cell";
        simpleBlock(
                getBlock(BLOCK, tier),
                tierParent(BLOCK, tier)
                        .texture("1", "powah:block/" + BLOCK + "_" + tier.getName()));
    }

    private void enderGate(Tier tier) {
        var BLOCK = "ender_gate";
        directionalBlockInverse(getBlock(BLOCK, tier),
                tierParent(BLOCK, tier)
                        .texture("gate", "powah:block/" + BLOCK)
                        .texture("ov", "powah:block/" + tier.getName() + "_ov"));
    }

    private void energizedRod(Tier tier) {
        var BLOCK = "energizing_rod";
        directionalBlockInverse(getBlock(BLOCK, tier),
                tierParent(BLOCK, tier)
                        .texture("gem", "powah:block/" + BLOCK + "_" + tier.getName() + "_gem")
                        .texture("rod", "powah:block/" + BLOCK));

    }

    private void energyCell(Tier tier) {
        var BLOCK = "energy_cell";
        simpleBlock(
                getBlock(BLOCK, tier),
                tierParent(BLOCK, tier)
                        .texture("0", "powah:block/" + BLOCK + "_" + tier.getName()));
    }

    // private void energyCable(Tier tier) {
    // var BLOCK = "energy_cable";
    // var modelstatic = tierParent(BLOCK, tier).texture("cable", "powah:block/" +
    // BLOCK + "_" + tier.getName());
    // var multipart = tierParent(BLOCK, tier, "_multipart").texture("mp",
    // "powah:block/" + BLOCK + "_" + tier.getName());
    //
    // getMultipartBuilder(getBlock(BLOCK, tier))
    // .part().modelFile(modelstatic).nextModel()
    //
    // .addModel().condition(CableBlock.NORTH, true).end()
    // .part().modelFile(multipart).nextModel()
    //
    // .addModel().condition(CableBlock.DOWN, true).end()
    // .part().modelFile(multipart).rotationX(90).nextModel()
    //
    // .addModel().condition(CableBlock.SOUTH, true).end()
    // .part().modelFile(multipart).rotationX(180).nextModel()
    //
    // .addModel().condition(CableBlock.UP, true).end()
    // .part().modelFile(multipart).rotationX(270).nextModel()
    //
    // .addModel().condition(CableBlock.EAST, true).end()
    // .part().modelFile(multipart).rotationY(90).nextModel()
    //
    // .addModel().condition(CableBlock.WEST, true).end()
    // .part().modelFile(multipart).rotationY(270).nextModel()
    //
    // .build();
    // }




    private void thermoGen(Tier tier) {
        var BLOCK = "thermo_generator";
        simpleBlock(
                getBlock(BLOCK, tier),
                tierParent(BLOCK, tier)
                        .texture("core", "powah:block/" + BLOCK + "_" + tier.getName() + "_core")
                        .texture("heater", "powah:block/" + BLOCK + "_heater")
                        .texture("top", "powah:block/" + BLOCK + "_" + tier.getName() + "_top"));
    }

    private BlockModelBuilder tierParent(String b, Tier t) {
        return tierParent(b, t, "");
    }

    private BlockModelBuilder tierParent(String b, Tier t, String extra) {
        return models().withExistingParent(getPath(getBlock(b, t)) + extra, Powah.MOD_ID + ":block/" + b);
    }

    private Block getBlock(String block, Tier t) {
        return BuiltInRegistries.BLOCK
                .get(ResourceLocation.fromNamespaceAndPath(Powah.MOD_ID, block + "_" + t.getName()));
    }

    private String getPath(Block b) {
        return b.getDescriptionId()
                .replace("block." + Powah.MOD_ID + ".", "");
    }

    private void cubeBlock(Block b) {
        simpleBlock(b,
                models().cubeAll(getPath(b), blockTexture(b)));
    }

    private ResourceLocation getResource(String s) {
        return ResourceLocation.fromNamespaceAndPath(Powah.MOD_ID, s);
    }

    public void directionalBlockInverse(Block block, ModelFile modelFunc) {
        getVariantBuilder(block).forAllStates(s -> {
            Direction dir = s.getValue(BlockStateProperties.FACING);
            return ConfiguredModel.builder().modelFile(modelFunc)
                    .rotationX(dir == Direction.UP ? 180 : (dir.getAxis().isHorizontal() ? 90 : 0))
                    .rotationY(dir.getAxis().isVertical() ? 0 : (int) dir.toYRot() % 360).build();
        });
    }

}
