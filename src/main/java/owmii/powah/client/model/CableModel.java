package owmii.powah.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import owmii.lib.client.model.AbstractModel;
import owmii.lib.logistics.Transfer;
import owmii.lib.logistics.energy.Energy;
import owmii.powah.Powah;
import owmii.powah.block.cable.CableTile;
import owmii.powah.client.render.tile.CableRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.HashMap;
import java.util.Map;

public class CableModel extends AbstractModel<CableTile, CableRenderer> {
    private static final String NORTH = "north";
    private static final String NORTH_PLATE = "north_plate";
    private static final String SOUTH = "south";
    private static final String SOUTH_PLATE = "south_plate";
    private static final String WEST = "west";
    private static final String WEST_PLATE = "west_plate";
    private static final String EAST = "east";
    private static final String EAST_PLATE = "east_plate";
    private static final String DOWN = "down";
    private static final String DOWN_PLATE = "down_plate";
    private static final String UP = "up";
    private static final String UP_PLATE = "up_plate";
    
    private final ModelPart north;
    private final ModelPart northPlate;
    private final ModelPart south;
    private final ModelPart southPlate;
    private final ModelPart west;
    private final ModelPart westPlate;
    private final ModelPart east;
    private final ModelPart eastPlate;
    private final ModelPart down;
    private final ModelPart downPlate;
    private final ModelPart upPlate;
    private final ModelPart up;

    public CableModel(ModelPart root) {
        super(RenderType::entitySolid);
        this.north = root.getChild(NORTH);
        this.northPlate = root.getChild(NORTH_PLATE);
        this.south = root.getChild(SOUTH);
        this.southPlate = root.getChild(SOUTH_PLATE);
        this.west = root.getChild(WEST);
        this.westPlate = root.getChild(WEST_PLATE);
        this.east = root.getChild(EAST);
        this.eastPlate = root.getChild(EAST_PLATE);
        this.down = root.getChild(DOWN);
        this.downPlate = root.getChild(DOWN_PLATE);
        this.up = root.getChild(UP);
        this.upPlate = root.getChild(UP_PLATE);
    }

    public static LayerDefinition createDefinition() {
        var meshDefinition = new MeshDefinition();
        var root = meshDefinition.getRoot();

        var pos = PartPose.offset(0F, 14F, 0F);

        root.addOrReplaceChild(NORTH, CubeListBuilder.create().texOffs(0, 10).mirror().addBox(-1.5F, -1.5F, -7.75F, 3, 3, 6), pos);
        root.addOrReplaceChild(NORTH_PLATE, CubeListBuilder.create().texOffs(0, 20).mirror().addBox(-2.5F, -2.5F, -8.2F, 5, 5, 1), pos);
        root.addOrReplaceChild(SOUTH, CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.5F, -1.5F, 1.75F, 3, 3, 6), pos);
        root.addOrReplaceChild(SOUTH_PLATE, CubeListBuilder.create().texOffs(0, 20).mirror().addBox(-2.5F, -2.5F, 7.2F, 5, 5, 1), pos);

        root.addOrReplaceChild(WEST, CubeListBuilder.create().texOffs(19, 0).mirror().addBox(-7.75F, -1.5F, -1.5F, 6, 3, 3), pos);
        root.addOrReplaceChild(WEST_PLATE, CubeListBuilder.create().texOffs(13, 20).mirror().addBox(-8.2F, -2.5F, -2.5F, 1, 5, 5), pos);
        root.addOrReplaceChild(EAST, CubeListBuilder.create().texOffs(19, 7).mirror().addBox(1.75F, -1.5F, -1.5F, 6, 3, 3), pos);
        root.addOrReplaceChild(EAST_PLATE, CubeListBuilder.create().texOffs(13, 20).mirror().addBox(7.2F, -2.5F, -2.5F, 1, 5, 5), pos);
        root.addOrReplaceChild(DOWN, CubeListBuilder.create().texOffs(38, 10).mirror().addBox(-1.5F, -7.75F, -1.5F, 3, 6, 3), pos);
        root.addOrReplaceChild(DOWN_PLATE, CubeListBuilder.create().texOffs(26, 20).mirror().addBox(-2.5F, -8.2F, -2.5F, 5, 1, 5), pos);
        root.addOrReplaceChild(UP, CubeListBuilder.create().texOffs(38, 0).mirror().addBox(-1.5F, 1.75F, -1.5F, 3, 6, 3), pos);
        root.addOrReplaceChild(UP_PLATE, CubeListBuilder.create().texOffs(26, 20).mirror().addBox(-2.5F, 7.2F, -2.5F, 5, 1, 5), pos);

        return LayerDefinition.create(meshDefinition, 64, 32);
    }

    private static final Map<Transfer, ResourceLocation> TEXTURES = new HashMap<>();

    static {
        TEXTURES.put(Transfer.ALL, new ResourceLocation(Powah.MOD_ID, "textures/model/tile/energy_cable_all.png"));
        TEXTURES.put(Transfer.RECEIVE, new ResourceLocation(Powah.MOD_ID, "textures/model/tile/energy_cable_out.png"));
        TEXTURES.put(Transfer.EXTRACT, new ResourceLocation(Powah.MOD_ID, "textures/model/tile/energy_cable_in.png"));
    }

    @Override
    public void render(CableTile te, CableRenderer renderer, PoseStack matrix, MultiBufferSource rtb, int light, int ov) {
        if (te.getLevel() == null) return;
        final Direction[] flags = new Direction[6];
        for (Direction side : te.energySides) {
            final BlockPos pos = te.getBlockPos().relative(side);
            final BlockEntity tile = te.getLevel().getBlockEntity(pos);
            final Transfer config = te.getSideConfig().getType(side);
            if (!(tile instanceof CableTile) && Energy.isPresent(tile, side) && (config.canExtract || config.canReceive)) {
                flags[side.get3DDataValue()] = side;
            }
        }

        if (flags[0] != null) {
            Transfer type = te.getSideConfig().getType(flags[0]);
            if (!type.equals(Transfer.NONE)) {
                VertexConsumer buffer = rtb.getBuffer(renderType(TEXTURES.get(type)));
                this.up.render(matrix, buffer, light, ov);
                this.upPlate.render(matrix, buffer, light, ov);
            }
        }

        if (flags[1] != null) {
            Transfer type = te.getSideConfig().getType(flags[1]);
            if (!type.equals(Transfer.NONE)) {
                VertexConsumer buffer = rtb.getBuffer(renderType(TEXTURES.get(type)));
                this.down.render(matrix, buffer, light, ov);
                this.downPlate.render(matrix, buffer, light, ov);
            }
        }

        if (flags[2] != null) {
            Transfer type = te.getSideConfig().getType(flags[2]);
            if (!type.equals(Transfer.NONE)) {
                VertexConsumer buffer = rtb.getBuffer(renderType(TEXTURES.get(type)));
                this.south.render(matrix, buffer, light, ov);
                this.southPlate.render(matrix, buffer, light, ov);
            }
        }
        if (flags[3] != null) {
            Transfer type = te.getSideConfig().getType(flags[3]);
            if (!type.equals(Transfer.NONE)) {
                VertexConsumer buffer = rtb.getBuffer(renderType(TEXTURES.get(type)));
                this.north.render(matrix, buffer, light, ov);
                this.northPlate.render(matrix, buffer, light, ov);
            }
        }

        if (flags[4] != null) {
            Transfer type = te.getSideConfig().getType(flags[4]);
            if (!type.equals(Transfer.NONE)) {
                VertexConsumer buffer = rtb.getBuffer(renderType(TEXTURES.get(type)));
                this.west.render(matrix, buffer, light, ov);
                this.westPlate.render(matrix, buffer, light, ov);
            }
        }

        if (flags[5] != null) {
            Transfer type = te.getSideConfig().getType(flags[5]);
            if (!type.equals(Transfer.NONE)) {
                VertexConsumer buffer = rtb.getBuffer(renderType(TEXTURES.get(type)));
                this.east.render(matrix, buffer, light, ov);
                this.eastPlate.render(matrix, buffer, light, ov);
            }
        }
    }
}
