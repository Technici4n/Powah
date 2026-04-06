package owmii.powah.data;

import static net.minecraft.client.data.models.BlockModelGenerators.NOP;
import static net.minecraft.client.data.models.BlockModelGenerators.ROTATION_FACING;
import static net.minecraft.client.data.models.BlockModelGenerators.ROTATION_HORIZONTAL_FACING;
import static net.minecraft.client.data.models.BlockModelGenerators.X_ROT_180;
import static net.minecraft.client.data.models.BlockModelGenerators.X_ROT_270;
import static net.minecraft.client.data.models.BlockModelGenerators.X_ROT_90;
import static net.minecraft.client.data.models.BlockModelGenerators.Y_ROT_180;
import static net.minecraft.client.data.models.BlockModelGenerators.Y_ROT_270;
import static net.minecraft.client.data.models.BlockModelGenerators.Y_ROT_90;
import static net.minecraft.client.data.models.BlockModelGenerators.createBooleanModelDispatch;
import static net.minecraft.client.data.models.BlockModelGenerators.plainVariant;
import static net.minecraft.client.data.models.model.TextureSlot.ALL;
import static net.minecraft.client.data.models.model.TextureSlot.PARTICLE;

import java.util.Optional;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.ConditionBuilder;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import owmii.powah.Powah;
import owmii.powah.block.Blcks;
import owmii.powah.block.Tier;
import owmii.powah.block.cable.CableBlock;
import owmii.powah.block.solar.SolarBlock;
import owmii.powah.block.transmitter.PlayerTransmitterBlock;
import owmii.powah.client.render.tile.ReactorItemRenderer;
import owmii.powah.lib.block.PowahBaseEnergyBlock;

public class BlockModelProvider extends ModelSubProvider {

    static final TextureSlot SLOT_OVERLAY = TextureSlot.create("ov");
    private static final PropertyDispatch.C1<VariantMutator, Direction> ROTATION_FACING_DOWN = PropertyDispatch.modify(BlockStateProperties.FACING)
            .select(Direction.DOWN, NOP)
            .select(Direction.UP, X_ROT_180)
            .select(Direction.NORTH, X_ROT_90.then(Y_ROT_180))
            .select(Direction.SOUTH, X_ROT_90)
            .select(Direction.WEST, X_ROT_90.then(Y_ROT_90))
            .select(Direction.EAST, X_ROT_90.then(Y_ROT_270));

    public BlockModelProvider(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        super(blockModels, itemModels);
    }

    @Override
    protected void register() {

        Blcks.ENERGY_CELL.getAll().forEach(this::energyCell);
        Blcks.ENDER_CELL.getAll().forEach(this::enderCell);
        Blcks.ENERGY_CABLE.getAll().forEach(this::energyCable);
        Blcks.ENDER_GATE.getAll().forEach(this::enderGate);
        Blcks.ENERGIZING_ROD.getAll().forEach(this::energizingRod);
        Blcks.FURNATOR.getAll().forEach(this::furnator);
        Blcks.MAGMATOR.getAll().forEach(this::magmator);
        Blcks.THERMO_GENERATOR.getAll().forEach(this::thermoGenerator);
        Blcks.SOLAR_PANEL.getAll().forEach(this::solarPanel);
        Blcks.REACTOR.getAll().forEach(this::reactor);
        Blcks.PLAYER_TRANSMITTER.getAll().forEach(this::playerTransmitter);
        Blcks.ENERGY_HOPPER.getAll().forEach(this::energyHopper);
        Blcks.ENERGY_DISCHARGER.getAll().forEach(this::energyDischarger);

        simpleDownFacingBlockAndItem(Blcks.ENERGIZING_ORB.get());
        simpleBlockAndItem(Blcks.ENERGIZED_STEEL.get());
        simpleBlockAndItem(Blcks.BLAZING_CRYSTAL.get());
        simpleBlockAndItem(Blcks.NIOTIC_CRYSTAL.get());
        simpleBlockAndItem(Blcks.SPIRITED_CRYSTAL.get());
        simpleBlockAndItem(Blcks.NITRO_CRYSTAL.get());
        simpleBlockAndItem(Blcks.URANINITE.get());
        simpleBlockAndItem(Blcks.DEEPSLATE_URANINITE_ORE_POOR.get());
        simpleBlockAndItem(Blcks.DEEPSLATE_URANINITE_ORE.get());
        simpleBlockAndItem(Blcks.DEEPSLATE_URANINITE_ORE_DENSE.get());
        simpleBlockAndItem(Blcks.URANINITE_ORE_POOR.get());
        simpleBlockAndItem(Blcks.URANINITE_ORE.get());
        simpleBlockAndItem(Blcks.URANINITE_ORE_DENSE.get());
        simpleBlockAndItem(Blcks.DRY_ICE.get());
    }

    private void simpleDownFacingBlockAndItem(Block block) {
        var base = plainVariant(ModelLocationUtils.getModelLocation(block));
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, base)
                .with(ROTATION_FACING_DOWN));
    }

    private void energyCell(PowahBaseEnergyBlock<?> block) {
        var model = ModelTemplates.create("powah:energy_cell", ALL).create(block, TextureMapping.cube(block), modelOutput);

        blockModels.blockStateOutput.accept(createSimpleBlock(block, plainVariant(model)));
    }

    private void enderCell(PowahBaseEnergyBlock<?> block) {
        var model = ModelTemplates.create("powah:ender_cell", ALL).create(block, TextureMapping.cube(block), modelOutput);

        blockModels.blockStateOutput.accept(createSimpleBlock(block, plainVariant(model)));
    }

    private void enderGate(PowahBaseEnergyBlock<?> block) {
        var tier = block.getTier().getSerializedName();

        var textureMapping = TextureMapping.singleSlot(SLOT_OVERLAY, new Material(overlayTexture(tier)));
        var model = ModelTemplates.create("powah:ender_gate", SLOT_OVERLAY).create(block, textureMapping, modelOutput);
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, plainVariant(model)).with(ROTATION_FACING));
    }

    private void energizingRod(PowahBaseEnergyBlock<?> block) {
        var tier = block.getTier().getSerializedName();

        var slotGem = TextureSlot.create("gem");
        var textureMapping = TextureMapping.singleSlot(slotGem, new Material(Powah.id("block/energizing_rod_" + tier + "_gem")));

        var model = ModelTemplates.create("powah:energizing_rod", slotGem).create(block, textureMapping, modelOutput);
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, plainVariant(model)).with(ROTATION_FACING_DOWN));
    }

    private void energyDischarger(PowahBaseEnergyBlock<?> block) {
        var tier = block.getTier().getSerializedName();

        var textureMapping = TextureMapping.singleSlot(SLOT_OVERLAY, new Material(overlayTexture(tier)));
        var model = ModelTemplates.create("powah:energy_discharger", SLOT_OVERLAY).create(block, textureMapping, modelOutput);
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, plainVariant(model)).with(ROTATION_HORIZONTAL_FACING));
    }

    private void energyHopper(PowahBaseEnergyBlock<?> block) {
        var tier = block.getTier().getSerializedName();

        var textureMapping = TextureMapping.singleSlot(SLOT_OVERLAY, new Material(overlayTexture(tier)));
        var model = ModelTemplates.create("powah:energy_hopper", SLOT_OVERLAY).create(block, textureMapping, modelOutput);
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, plainVariant(model)).with(ROTATION_FACING));
    }

    private void furnator(PowahBaseEnergyBlock<?> block) {
        var tier = block.getTier().getSerializedName();

        var textureMapping = TextureMapping.singleSlot(SLOT_OVERLAY, new Material(overlayTexture(tier)));

        var model = ModelTemplates.create("powah:furnator", SLOT_OVERLAY)
                .create(block, textureMapping, modelOutput);
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, plainVariant(model)).with(ROTATION_HORIZONTAL_FACING));
    }

    private void magmator(PowahBaseEnergyBlock<?> block) {
        var tier = block.getTier().getSerializedName();

        var slotFace = TextureSlot.create("face");
        var texturesUnlit = TextureMapping.singleSlot(SLOT_OVERLAY, new Material(overlayTexture(tier)));
        var texturesLit = texturesUnlit.copyAndUpdate(slotFace, new Material(Powah.id("block/magmator_face_lit")));

        var unlitModel = ModelTemplates.create("powah:magmator", SLOT_OVERLAY).create(block, texturesUnlit, modelOutput);
        var litModel = ModelTemplates.create("powah:magmator", slotFace, SLOT_OVERLAY).createWithOverride(block, "_on", texturesLit, modelOutput);

        blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(createBooleanModelDispatch(BlockStateProperties.LIT, plainVariant(litModel), plainVariant(unlitModel)))
                        .with(ROTATION_HORIZONTAL_FACING));
    }

    private void thermoGenerator(PowahBaseEnergyBlock<?> block) {
        var slotCore = TextureSlot.create("core");
        var slotHeater = TextureSlot.create("heater");
        var slotTop = TextureSlot.create("top");

        var textureMapping = new TextureMapping()
                .put(slotCore, TextureMapping.getBlockTexture(block, "_core"))
                .put(slotHeater, new Material(Powah.id("block/thermo_generator_heater")))
                .put(slotTop, TextureMapping.getBlockTexture(block, "_top"));

        var model = ModelTemplates.create("powah:thermo_generator", slotCore, slotHeater, slotTop)
                .create(block, textureMapping, modelOutput);
        blockModels.blockStateOutput.accept(createSimpleBlock(block, plainVariant(model)));
    }

    private void energyCable(Block block) {
        var baseModel = ModelTemplates.create("powah:cable", ALL).create(block, TextureMapping.cube(block), modelOutput);
        var multipartModel = new ModelTemplate(Optional.of(Powah.id("block/cable_multipart")), Optional.of("_multipart"), ALL).create(block,
                TextureMapping.cube(block), modelOutput);

        blockStateOutput.accept(
                MultiPartGenerator.multiPart(block)
                        .with(plainVariant(baseModel))
                        .with(new ConditionBuilder().term(CableBlock.NORTH, true), plainVariant(multipartModel))
                        .with(new ConditionBuilder().term(CableBlock.EAST, true), plainVariant(multipartModel).with(Y_ROT_90))
                        .with(new ConditionBuilder().term(CableBlock.SOUTH, true), plainVariant(multipartModel).with(X_ROT_180))
                        .with(new ConditionBuilder().term(CableBlock.WEST, true), plainVariant(multipartModel).with(Y_ROT_270))
                        .with(new ConditionBuilder().term(CableBlock.UP, true), plainVariant(multipartModel).with(X_ROT_270))
                        .with(new ConditionBuilder().term(CableBlock.DOWN, true), plainVariant(multipartModel).with(X_ROT_90)));
    }

    private void playerTransmitter(Block block) {
        // There's only two types of panels: starter, and the rest
        var tierTexture = TextureMapping.getBlockTexture(block, "_var");
        var tierSlot = TextureSlot.create("var");
        var textureMapping = TextureMapping.singleSlot(tierSlot, tierTexture);

        // Create item model
        ModelTemplates.createItem("powah:player_transmitter", tierSlot).create(block.asItem(), textureMapping, modelOutput);
        itemModels.declareCustomModelItem(block.asItem());

        // Create block models
        var normalModel = ModelTemplates.create("powah:player_transmitter", tierSlot).create(block, textureMapping, modelOutput);
        var topOn = ModelTemplates.create("powah:player_transmitter_top", tierSlot).createWithSuffix(block, "_top", textureMapping, modelOutput);

        blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(createBooleanModelDispatch(PlayerTransmitterBlock.TOP, plainVariant(topOn), plainVariant(normalModel))));
    }

    private void solarPanel(Block block) {
        var tier = ((SolarBlock) block).getTier();
        // There's only two types of panels: starter, and the rest
        var panelTexture = (tier == Tier.STARTER) ? Powah.id("block/solar_panel_starter_top") : Powah.id("block/solar_panel_top");
        var frameTexture = TextureMapping.getBlockTexture(block, "_frame");
        var slotPanel = TextureSlot.create("panel");
        var slotFrame = TextureSlot.create("frame");
        var textureMapping = new TextureMapping().put(slotPanel, new Material(panelTexture)).put(slotFrame, frameTexture);

        // Generate main block model (without frame)
        var baseModel = ModelTemplates.create("powah:solar_panel", slotPanel, slotFrame)
                .create(block, textureMapping, modelOutput);
        var frameModel = ModelTemplates.create("powah:solar_panel_frame", slotFrame)
                .createWithSuffix(block, "_frame", textureMapping, modelOutput);

        // Generate item model
        new ModelTemplate(Optional.of(Powah.id("item/solar_panel")), Optional.empty(), slotPanel, slotFrame)
                .create(block.asItem(), textureMapping, modelOutput);
        itemModels.declareCustomModelItem(block.asItem());

        blockStateOutput.accept(
                MultiPartGenerator.multiPart(block)
                        .with(plainVariant(baseModel))
                        .with(new ConditionBuilder().term(SolarBlock.NORTH, true), plainVariant(frameModel))
                        .with(new ConditionBuilder().term(SolarBlock.EAST, true), plainVariant(frameModel).with(Y_ROT_90))
                        .with(new ConditionBuilder().term(SolarBlock.SOUTH, true), plainVariant(frameModel).with(Y_ROT_180))
                        .with(new ConditionBuilder().term(SolarBlock.WEST, true), plainVariant(frameModel).with(Y_ROT_270)));
    }

    private void reactor(PowahBaseEnergyBlock<?> block) {
        var tier = block.getTier();

        // Essentially an empty block, but particle is required now, and we inherit the item transform from cube this way
        var textures = TextureMapping.particle(new Material(Powah.id("block/furnator_face")));
        var model = ModelTemplates.create("block", PARTICLE).create(block, textures, modelOutput);

        // The item model uses a custom renderer too, but has to use the block-model as its base due to the particle texture being required
        blockModels.itemModelOutput.accept(
                block.asItem(),
                ItemModelUtils.specialModel(model, new ReactorItemRenderer.Unbaked(tier)));

        blockStateOutput.accept(createSimpleBlock(block, plainVariant(model)));
    }

    private static Identifier overlayTexture(String tier) {
        return Powah.id("block/" + tier + "_ov");
    }
}
