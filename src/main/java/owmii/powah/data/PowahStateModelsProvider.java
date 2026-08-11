package owmii.powah.data;

import java.util.List;
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
import owmii.powah.block.cable.CableBlock;
import owmii.powah.block.solar.SolarBlock;
import owmii.powah.block.transmitter.PlayerTransmitterBlock;

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
        directionalBlockCustom(Blcks.ENERGIZING_ORB.get(),
                models().getExistingFile(getResource("block/energizing_orb")),
                List.of(180, 0, 90, 90, 90, 90),
                List.of(0, 0, 0, 180, 270, 90));

        // tiered blocks (starter->nitro)
        for (Tier tier : Tier.getNormalVariants()) {
            enderCell(tier);
            enderGate(tier);
            thermoGen(tier);
            energizedRod(tier);
            energyCable(tier);
            reactorCasing(tier);
            playerTransmitter(tier);
            solarPanel(tier);

            // added up and down model
            energyDischarger(tier);
            energyHopper(tier);
            furnatorGen(tier);
            magmatorGem(tier);

        }

        // tiered blocks (starter->creative)
        for (Tier tier : new Tier[] { Tier.STARTER, Tier.BASIC, Tier.HARDENED, Tier.BLAZING, Tier.NIOTIC,
                Tier.SPIRITED,
                Tier.NITRO, Tier.CREATIVE }) {
            energyCell(tier);
        }

    }

    private void enderCell(Tier tier) {
        var BLOCK = "ender_cell";
        simpleBlock(
                getBlock(BLOCK, tier),
                tierParent(BLOCK, tier)
                        .texture("1", Powah.MOD_ID + ":block/" + BLOCK + "_" + tier.getName()));
    }

    private void enderGate(Tier tier) {
        var BLOCK = "ender_gate";
        directionalBlockCustom(getBlock(BLOCK, tier),
                tierParent(BLOCK, tier)
                        .texture("gate", Powah.MOD_ID + ":block/" + BLOCK)
                        .texture("ov", Powah.MOD_ID + ":block/" + tier.getName() + "_ov"),
                List.of(270, 90, 0, 0, 0, 0),
                List.of(0, 0, 180, 0, 90, 270));
    }

    private void energizedRod(Tier tier) {
        var BLOCK = "energizing_rod";
        directionalBlockCustom(getBlock(BLOCK, tier),
                tierParent(BLOCK, tier)
                        .texture("gem", Powah.MOD_ID + ":block/" + BLOCK + "_" + tier.getName()
                                + "_gem")
                        .texture("rod", Powah.MOD_ID + ":block/" + BLOCK),
                List.of(180, 0, 90, 90, 90, 90),
                List.of(0, 0, 0, 180, 270, 90));

    }

    private void energyCell(Tier tier) {
        var BLOCK = "energy_cell";
        simpleBlock(
                getBlock(BLOCK, tier),
                tierParent(BLOCK, tier)
                        .texture("0", Powah.MOD_ID + ":block/" + BLOCK + "_" + tier.getName()));
    }

    private void energyCable(Tier tier) {
        var BLOCK = "energy_cable";
        var modelstatic = tierParent(BLOCK, tier).texture("cable", Powah.MOD_ID + ":block/" +
                BLOCK + "_" + tier.getName());
        var multipart = tierParent(BLOCK, tier, "_multipart", "_multipart").texture("mp",
                Powah.MOD_ID + ":block/" + BLOCK + "_" + tier.getName());

        var model = getMultipartBuilder(getBlock(BLOCK, tier));
        model.part().modelFile(modelstatic).addModel();
        model.part().modelFile(multipart).addModel().condition(CableBlock.NORTH, true);
        model.part().modelFile(multipart).rotationY(90).addModel().condition(CableBlock.EAST, true);
        model.part().modelFile(multipart).rotationX(180).addModel().condition(CableBlock.SOUTH, true);
        model.part().modelFile(multipart).rotationY(270).addModel().condition(CableBlock.WEST, true);
        model.part().modelFile(multipart).rotationX(270).addModel().condition(CableBlock.UP, true);
        model.part().modelFile(multipart).rotationX(90).addModel().condition(CableBlock.DOWN, true);
    }

    private void solarPanel(Tier tier) {
        var BLOCK = "solar_panel";
        var base = tierParent(BLOCK, tier)
                .texture("panel", Powah.MOD_ID + ":block/" + BLOCK + "_top")
                .texture("frame", Powah.MOD_ID + ":block/" + BLOCK + "_" + tier.getName() + "_frame");
        var frame = tierParent(BLOCK, tier, "_frame", "_frame").texture("frame",
                Powah.MOD_ID + ":block/" + BLOCK + "_" + tier.getName() + "_frame");

        var model = getMultipartBuilder(getBlock(BLOCK, tier));
        model.part().modelFile(base).addModel();
        model.part().modelFile(frame).addModel().condition(SolarBlock.NORTH, true);
        model.part().modelFile(frame).rotationY(90).addModel().condition(SolarBlock.EAST, true);
        model.part().modelFile(frame).rotationY(180).addModel().condition(SolarBlock.SOUTH, true);
        model.part().modelFile(frame).rotationY(270).addModel().condition(SolarBlock.WEST, true);
    }

    private void energyDischarger(Tier tier) {
        var BLOCK = "energy_discharger";
        directionalBlockCustom(
                getBlock(BLOCK, tier),
                tierParent(BLOCK, tier)
                        .texture("side", Powah.MOD_ID + ":block/" + BLOCK + "_side")
                        .texture("comps", Powah.MOD_ID + ":block/" + BLOCK + "_comps")
                        .texture("ov", Powah.MOD_ID + ":block/" + tier.getName() + "_ov"),
                List.of(270, 90, 0, 0, 0, 0),
                List.of(0, 0, 180, 0, 90, 270));
    }

    private void energyHopper(Tier tier) {
        var BLOCK = "energy_hopper";
        directionalBlockCustom(
                getBlock(BLOCK, tier),
                tierParent(BLOCK, tier)
                        .texture("side", Powah.MOD_ID + ":block/" + BLOCK + "_side")
                        .texture("back", Powah.MOD_ID + ":block/" + BLOCK + "_back")
                        .texture("pointer", Powah.MOD_ID + ":block/" + BLOCK + "_pointer")
                        .texture("ov", Powah.MOD_ID + ":block/" + tier.getName() + "_ov"),
                List.of(270, 90, 0, 0, 0, 0),
                List.of(0, 0, 180, 0, 90, 270));

    }

    private void furnatorGen(Tier tier) {
        var BLOCK = "furnator";
        directionalBlockCustom(
                getBlock(BLOCK, tier),
                tierParent(BLOCK, tier)
                        .texture("face", Powah.MOD_ID + ":block/" + BLOCK + "_face")
                        .texture("side", Powah.MOD_ID + ":block/" + BLOCK + "_side")
                        .texture("top", Powah.MOD_ID + ":block/" + BLOCK + "_top")
                        .texture("lit", Powah.MOD_ID + ":block/" + BLOCK + "_unlit")
                        .texture("ov", Powah.MOD_ID + ":block/" + tier.getName() + "_ov"),
                List.of(90, 270, 0, 0, 0, 0),
                List.of(0, 0, 180, 0, 90, 270));
    }

    private void reactorCasing(Tier tier) {
        var BLOCK = "reactor";

        simpleBlock(getBlock(BLOCK, tier),
                tierParent(BLOCK, tier)

                        .texture("particle",
                                Powah.MOD_ID + ":block/furnator_face"));

    }

    private void playerTransmitter(Tier tier) {
        var BLOCK = "player_transmitter";

        getVariantBuilder(getBlock(BLOCK, tier))
                .partialState().with(PlayerTransmitterBlock.TOP, false).modelForState()
                .modelFile(tierParent(BLOCK, tier)
                        .texture("var", Powah.MOD_ID + ":block/" + BLOCK + "_" + tier.getName()
                                + "_var")
                        .texture("core", Powah.MOD_ID + ":block/" + BLOCK + "_core"))
                .addModel()
                .partialState().with(PlayerTransmitterBlock.TOP, true).modelForState()
                .modelFile(tierParent(BLOCK, tier, "_top", "_top")
                        .texture("var", Powah.MOD_ID + ":block/" + BLOCK + "_" + tier.getName()
                                + "_var"))
                .addModel();

    }

    private void magmatorGem(Tier tier) {

        var BLOCK = "magmator";

        var Xrot = List.of(90, 270, 0, 0, 0, 0);
        var Yrot = List.of(0, 0, 180, 0, 90, 270);

        getVariantBuilder(getBlock(BLOCK, tier)).forAllStates(s -> {
            Direction dir = s.getValue(BlockStateProperties.FACING);
            boolean lit = s.getValue(BlockStateProperties.LIT);
            return ConfiguredModel.builder().modelFile(
                    tierParent(BLOCK, tier, lit ? "_on" : "")
                            .texture("face", Powah.MOD_ID + ":block/" + BLOCK + "_face"
                                    + (lit ? "_lit" : "_unlit"))
                            .texture("side", Powah.MOD_ID + ":block/" + BLOCK + "_side")
                            .texture("inside", Powah.MOD_ID + ":block/" + BLOCK + "_inside")
                            .texture("top", Powah.MOD_ID + ":block/" + BLOCK + "_top")
                            .texture("ov", Powah.MOD_ID + ":block/" + tier.getName()
                                    + "_ov"))
                    .rotationX(Xrot.get(DIRECTIONS.indexOf(dir)))
                    .rotationY(Yrot.get(DIRECTIONS.indexOf(dir)))
                    .build();
        });
    }

    private void thermoGen(Tier tier) {
        var BLOCK = "thermo_generator";
        simpleBlock(
                getBlock(BLOCK, tier),
                tierParent(BLOCK, tier)
                        .texture("core", Powah.MOD_ID + ":block/" + BLOCK + "_" + tier.getName()
                                + "_core")
                        .texture("heater", Powah.MOD_ID + ":block/" + BLOCK + "_heater")
                        .texture("top", Powah.MOD_ID + ":block/" + BLOCK + "_" + tier.getName()
                                + "_top"));
    }

    private BlockModelBuilder tierParent(String b, Tier t) {
        return tierParent(b, t, "");
    }

    private BlockModelBuilder tierParent(String b, Tier t, String nameExtra) {
        return tierParent(b, t, nameExtra, "");
    }

    private BlockModelBuilder tierParent(String b, Tier t, String nameExtra, String parentExtra) {
        return models().withExistingParent(getPath(getBlock(b, t)) + nameExtra,
                Powah.MOD_ID + ":block/" + b + parentExtra);
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

    private List<Direction> DIRECTIONS = List.of(
            Direction.UP, Direction.DOWN, Direction.SOUTH,
            Direction.NORTH, Direction.EAST, Direction.WEST);
    private List<List<Integer>> DEFAULT_ROTATIONS = List.of(
            List.of(0, 0, 0, 0, 0, 0),
            List.of(0, 0, 0, 0, 0, 0));

    public void directionalBlockCustom(Block block, ModelFile modelFunc) {
        directionalBlockCustom(block, modelFunc, DEFAULT_ROTATIONS.get(0), DEFAULT_ROTATIONS.get(1));
    }

    public void directionalBlockCustom(Block block, ModelFile modelFunc, List<Integer> Xrot, List<Integer> Yrot) {
        getVariantBuilder(block).forAllStates(s -> {
            Direction dir = s.getValue(BlockStateProperties.FACING);
            return ConfiguredModel.builder().modelFile(modelFunc)
                    .rotationX(Xrot.get(DIRECTIONS.indexOf(dir)))
                    .rotationY(Yrot.get(DIRECTIONS.indexOf(dir)))
                    .build();
        });
    }

}
