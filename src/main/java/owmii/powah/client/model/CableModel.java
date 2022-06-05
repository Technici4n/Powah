package owmii.powah.client.model;

import net.minecraft.client.model.geom.ModelPart;
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
    // TODO PORT fix
    /*
    private final ModelPart north;
    private final ModelPart northPlate;
    private final ModelPart south;
    private final ModelPart southPlate;
    private final ModelPart west;
    private final ModelPart westPlate;
    private final ModelPart east;
    private final ModelPart eastPlate;
    private final ModelPart down;
    private final ModelPart upPlate;
    private final ModelPart up;
    private final ModelPart downPlate;
     */

    public CableModel() {
        super(RenderType::entitySolid);
        /*
        this.texWidth = 64;
        this.texHeight = 32;

        this.north = new ModelPart(this, 0, 10);
        this.north.addBox(-1.5F, -1.5F, -7.75F, 3, 3, 6);
        this.north.setPos(0F, 14F, 0F);
        this.north.setTexSize(64, 32);
        this.north.mirror = true;
        setRotation(this.north, 0F, 0F, 0F);
        this.northPlate = new ModelPart(this, 0, 20);
        this.northPlate.addBox(-2.5F, -2.5F, -8.2F, 5, 5, 1);
        this.northPlate.setPos(0F, 14F, 0F);
        this.northPlate.setTexSize(64, 32);
        this.northPlate.mirror = true;
        setRotation(this.northPlate, 0F, 0F, 0F);
        this.south = new ModelPart(this, 0, 0);
        this.south.addBox(-1.5F, -1.5F, 1.75F, 3, 3, 6);
        this.south.setPos(0F, 14F, 0F);
        this.south.setTexSize(64, 32);
        this.south.mirror = true;
        setRotation(this.south, 0F, 0F, 0F);
        this.southPlate = new ModelPart(this, 0, 20);
        this.southPlate.addBox(-2.5F, -2.5F, 7.2F, 5, 5, 1);
        this.southPlate.setPos(0F, 14F, 0F);
        this.southPlate.setTexSize(64, 32);
        this.southPlate.mirror = true;
        setRotation(this.southPlate, 0F, 0F, 0F);
        this.west = new ModelPart(this, 19, 0);
        this.west.addBox(-7.75F, -1.5F, -1.5F, 6, 3, 3);
        this.west.setPos(0F, 14F, 0F);
        this.west.setTexSize(64, 32);
        this.west.mirror = true;
        setRotation(this.west, 0F, 0F, 0F);
        this.westPlate = new ModelPart(this, 13, 20);
        this.westPlate.addBox(-8.2F, -2.5F, -2.5F, 1, 5, 5);
        this.westPlate.setPos(0F, 14F, 0F);
        this.westPlate.setTexSize(64, 32);
        this.westPlate.mirror = true;
        setRotation(this.westPlate, 0F, 0F, 0F);
        this.east = new ModelPart(this, 19, 7);
        this.east.addBox(1.75F, -1.5F, -1.5F, 6, 3, 3);
        this.east.setPos(0F, 14F, 0F);
        this.east.setTexSize(64, 32);
        this.east.mirror = true;
        setRotation(this.east, 0F, 0F, 0F);
        this.eastPlate = new ModelPart(this, 13, 20);
        this.eastPlate.addBox(7.2F, -2.5F, -2.5F, 1, 5, 5);
        this.eastPlate.setPos(0F, 14F, 0F);
        this.eastPlate.setTexSize(64, 32);
        this.eastPlate.mirror = true;
        setRotation(this.eastPlate, 0F, 0F, 0F);
        this.down = new ModelPart(this, 38, 0);
        this.down.addBox(-1.5F, -7.75F, -1.5F, 3, 6, 3);
        this.down.setPos(0F, 14F, 0F);
        this.down.setTexSize(64, 32);
        this.down.mirror = true;
        setRotation(this.down, 0F, 0F, 0F);
        this.upPlate = new ModelPart(this, 26, 20);
        this.upPlate.addBox(-2.5F, 7.2F, -2.5F, 5, 1, 5);
        this.upPlate.setPos(0F, 14F, 0F);
        this.upPlate.setTexSize(64, 32);
        this.upPlate.mirror = true;
        setRotation(this.upPlate, 0F, 0F, 0F);
        this.up = new ModelPart(this, 38, 10);
        this.up.addBox(-1.5F, 1.75F, -1.5F, 3, 6, 3);
        this.up.setPos(0F, 14F, 0F);
        this.up.setTexSize(64, 32);
        this.up.mirror = true;
        setRotation(this.up, 0F, 0F, 0F);
        this.downPlate = new ModelPart(this, 26, 20);
        this.downPlate.addBox(-2.5F, -8.2F, -2.5F, 5, 1, 5);
        this.downPlate.setPos(0F, 14F, 0F);
        this.downPlate.setTexSize(64, 32);
        this.downPlate.mirror = true;
        setRotation(this.downPlate, 0F, 0F, 0F);
         */
    }

    private static final Map<Transfer, ResourceLocation> TEXTURES = new HashMap<>();

    static {
        TEXTURES.put(Transfer.ALL, new ResourceLocation(Powah.MOD_ID, "textures/model/tile/energy_cable_all.png"));
        TEXTURES.put(Transfer.RECEIVE, new ResourceLocation(Powah.MOD_ID, "textures/model/tile/energy_cable_out.png"));
        TEXTURES.put(Transfer.EXTRACT, new ResourceLocation(Powah.MOD_ID, "textures/model/tile/energy_cable_in.png"));
    }

    @Override
    public void render(CableTile te, CableRenderer renderer, PoseStack matrix, MultiBufferSource rtb, int light, int ov) {
        // TODO PORT fix
        /*
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
         */
    }
}
