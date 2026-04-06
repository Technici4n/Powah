package owmii.powah.data;

import com.mojang.math.Quadrant;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.blockstates.ConditionBuilder;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;
import net.neoforged.neoforge.client.model.generators.blockstate.CustomBlockStateModelBuilder;
import owmii.powah.Powah;

public abstract class ModelSubProvider {
    public static final ModelTemplate EMPTY_MODEL = new ModelTemplate(Optional.empty(), Optional.empty(),
            TextureSlot.PARTICLE);

    protected final BlockModelGenerators blockModels;
    protected final Consumer<BlockModelDefinitionGenerator> blockStateOutput;
    protected final ItemModelGenerators itemModels;
    protected final BiConsumer<Identifier, ModelInstance> modelOutput;

    public ModelSubProvider(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.blockModels = blockModels;
        this.itemModels = itemModels;
        this.modelOutput = blockModels.modelOutput;
        this.blockStateOutput = blockModels.blockStateOutput;
    }

    protected abstract void register();

    /**
     * Define a block model that is a simple textured cube, and uses the same model for its item. The texture path is
     * derived from the block's id.
     */
    protected void simpleBlockAndItem(Block block) {
        blockModels.createTrivialCube(block);
        // item falls back automatically to the block model
    }

    protected void simpleBlockAndItem(Block block, TexturedModel.Provider model) {
        blockModels.createTrivialBlock(block, model);
        // item falls back automatically to the block model
    }

    /**
     * Define a block model that is a simple textured cube, and uses the same model for its item.
     */
    protected void simpleBlockAndItem(Block block, String textureName) {
        blockModels.createTrivialBlock(
                block,
                TexturedModel.CUBE.updateTexture(mapping -> mapping.put(TextureSlot.ALL, new Material(Powah.id(textureName)))));
        // item falls back automatically to the block model
    }

    protected static VariantMutator applyRotation(int angleX, int angleY) {
        angleX = normalizeAngle(angleX);
        angleY = normalizeAngle(angleY);

        VariantMutator mutator = variant -> variant;
        if (angleX != 0) {
            mutator = mutator.then(VariantMutator.X_ROT.withValue(rotationByAngle(angleX)));
        }
        if (angleY != 0) {
            mutator = mutator.then(VariantMutator.Y_ROT.withValue(rotationByAngle(angleY)));
        }
        return mutator;
    }

    private static int normalizeAngle(int angle) {
        return angle - (angle / 360) * 360;
    }

    private static Quadrant rotationByAngle(int angle) {
        return switch (angle) {
        case 0 -> Quadrant.R0;
        case 90 -> Quadrant.R90;
        case 180 -> Quadrant.R180;
        case 270 -> Quadrant.R270;
        default -> throw new IllegalArgumentException("Invalid angle: " + angle);
        };
    }

    protected final MultiPartGenerator multiPartGenerator(Block blockDef) {
        var multipart = MultiPartGenerator.multiPart(blockDef);
        blockModels.blockStateOutput.accept(multipart);
        return multipart;
    }

    private static <T extends Comparable<T>> ConditionBuilder addConditionTerm(ConditionBuilder conditionBuilder,
            BlockState blockState,
            Property<T> property) {
        return conditionBuilder.term(property, blockState.getValue(property));
    }

    protected static MultiVariant customBlockStateModel(CustomUnbakedBlockStateModel model) {
        return MultiVariant.of(new CustomBlockStateModelBuilder.Simple(model));
    }

    public static MultiVariant variant(Variant variant) {
        return new MultiVariant(WeightedList.of(variant));
    }

    public static MultiVariantGenerator createSimpleBlock(Block block, MultiVariant variant) {
        return BlockModelGenerators.createSimpleBlock(block, variant);
    }
}
