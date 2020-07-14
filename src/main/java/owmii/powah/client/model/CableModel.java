package owmii.powah.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import owmii.lib.client.model.AbstractModel;
import owmii.lib.logistics.TransferType;
import owmii.lib.logistics.energy.Energy;
import owmii.powah.Powah;
import owmii.powah.block.cable.CableTile;
import owmii.powah.client.render.tile.CableRenderer;

import java.util.HashMap;
import java.util.Map;

public class CableModel extends AbstractModel<CableTile, CableRenderer> {
    private final ModelRenderer north;
    private final ModelRenderer northPlate;
    private final ModelRenderer south;
    private final ModelRenderer southPlate;
    private final ModelRenderer west;
    private final ModelRenderer westPlate;
    private final ModelRenderer east;
    private final ModelRenderer eastPlate;
    private final ModelRenderer down;
    private final ModelRenderer upPlate;
    private final ModelRenderer up;
    private final ModelRenderer downPlate;

    public CableModel() {
        super(RenderType::getEntitySolid);
        this.textureWidth = 64;
        this.textureHeight = 32;

        this.north = new ModelRenderer(this, 0, 10);
        this.north.addBox(-1.5F, -1.5F, -7.75F, 3, 3, 6);
        this.north.setRotationPoint(0F, 14F, 0F);
        this.north.setTextureSize(64, 32);
        this.north.mirror = true;
        setRotation(this.north, 0F, 0F, 0F);
        this.northPlate = new ModelRenderer(this, 0, 20);
        this.northPlate.addBox(-2.5F, -2.5F, -8.2F, 5, 5, 1);
        this.northPlate.setRotationPoint(0F, 14F, 0F);
        this.northPlate.setTextureSize(64, 32);
        this.northPlate.mirror = true;
        setRotation(this.northPlate, 0F, 0F, 0F);
        this.south = new ModelRenderer(this, 0, 0);
        this.south.addBox(-1.5F, -1.5F, 1.75F, 3, 3, 6);
        this.south.setRotationPoint(0F, 14F, 0F);
        this.south.setTextureSize(64, 32);
        this.south.mirror = true;
        setRotation(this.south, 0F, 0F, 0F);
        this.southPlate = new ModelRenderer(this, 0, 20);
        this.southPlate.addBox(-2.5F, -2.5F, 7.2F, 5, 5, 1);
        this.southPlate.setRotationPoint(0F, 14F, 0F);
        this.southPlate.setTextureSize(64, 32);
        this.southPlate.mirror = true;
        setRotation(this.southPlate, 0F, 0F, 0F);
        this.west = new ModelRenderer(this, 19, 0);
        this.west.addBox(-7.75F, -1.5F, -1.5F, 6, 3, 3);
        this.west.setRotationPoint(0F, 14F, 0F);
        this.west.setTextureSize(64, 32);
        this.west.mirror = true;
        setRotation(this.west, 0F, 0F, 0F);
        this.westPlate = new ModelRenderer(this, 13, 20);
        this.westPlate.addBox(-8.2F, -2.5F, -2.5F, 1, 5, 5);
        this.westPlate.setRotationPoint(0F, 14F, 0F);
        this.westPlate.setTextureSize(64, 32);
        this.westPlate.mirror = true;
        setRotation(this.westPlate, 0F, 0F, 0F);
        this.east = new ModelRenderer(this, 19, 7);
        this.east.addBox(1.75F, -1.5F, -1.5F, 6, 3, 3);
        this.east.setRotationPoint(0F, 14F, 0F);
        this.east.setTextureSize(64, 32);
        this.east.mirror = true;
        setRotation(this.east, 0F, 0F, 0F);
        this.eastPlate = new ModelRenderer(this, 13, 20);
        this.eastPlate.addBox(7.2F, -2.5F, -2.5F, 1, 5, 5);
        this.eastPlate.setRotationPoint(0F, 14F, 0F);
        this.eastPlate.setTextureSize(64, 32);
        this.eastPlate.mirror = true;
        setRotation(this.eastPlate, 0F, 0F, 0F);
        this.down = new ModelRenderer(this, 38, 0);
        this.down.addBox(-1.5F, -7.75F, -1.5F, 3, 6, 3);
        this.down.setRotationPoint(0F, 14F, 0F);
        this.down.setTextureSize(64, 32);
        this.down.mirror = true;
        setRotation(this.down, 0F, 0F, 0F);
        this.upPlate = new ModelRenderer(this, 26, 20);
        this.upPlate.addBox(-2.5F, 7.2F, -2.5F, 5, 1, 5);
        this.upPlate.setRotationPoint(0F, 14F, 0F);
        this.upPlate.setTextureSize(64, 32);
        this.upPlate.mirror = true;
        setRotation(this.upPlate, 0F, 0F, 0F);
        this.up = new ModelRenderer(this, 38, 10);
        this.up.addBox(-1.5F, 1.75F, -1.5F, 3, 6, 3);
        this.up.setRotationPoint(0F, 14F, 0F);
        this.up.setTextureSize(64, 32);
        this.up.mirror = true;
        setRotation(this.up, 0F, 0F, 0F);
        this.downPlate = new ModelRenderer(this, 26, 20);
        this.downPlate.addBox(-2.5F, -8.2F, -2.5F, 5, 1, 5);
        this.downPlate.setRotationPoint(0F, 14F, 0F);
        this.downPlate.setTextureSize(64, 32);
        this.downPlate.mirror = true;
        setRotation(this.downPlate, 0F, 0F, 0F);
    }

    private static final Map<TransferType, ResourceLocation> TEXTURES = new HashMap<>();

    static {
        TEXTURES.put(TransferType.ALL, new ResourceLocation(Powah.MOD_ID, "textures/model/tile/energy_cable_all.png"));
        TEXTURES.put(TransferType.EXTRACT, new ResourceLocation(Powah.MOD_ID, "textures/model/tile/energy_cable_out.png"));
        TEXTURES.put(TransferType.RECEIVE, new ResourceLocation(Powah.MOD_ID, "textures/model/tile/energy_cable_in.png"));
    }

    @Override
    public void render(CableTile te, CableRenderer renderer, MatrixStack matrix, IRenderTypeBuffer rtb, int light, int ov) {
        if (te.getWorld() == null) return;
        final Direction[] flags = new Direction[6];
        for (Direction side : te.energySides) {
            final BlockPos pos = te.getPos().offset(side);
            final TileEntity tile = te.getWorld().getTileEntity(pos);
            final TransferType config = te.getSideConfig().getType(side);
            if (!(tile instanceof CableTile) && Energy.isPresent(tile, side) && (config.canExtract || config.canReceive)) {
                flags[side.getIndex()] = side;
            }
        }

        if (flags[0] != null) {
            TransferType type = te.getSideConfig().getType(flags[0]);
            if (!type.equals(TransferType.NONE)) {
                IVertexBuilder buffer = rtb.getBuffer(getRenderType(TEXTURES.get(type)));
                this.up.render(matrix, buffer, light, ov);
                this.upPlate.render(matrix, buffer, light, ov);
            }
        }

        if (flags[1] != null) {
            TransferType type = te.getSideConfig().getType(flags[1]);
            if (!type.equals(TransferType.NONE)) {
                IVertexBuilder buffer = rtb.getBuffer(getRenderType(TEXTURES.get(type)));
                this.down.render(matrix, buffer, light, ov);
                this.downPlate.render(matrix, buffer, light, ov);
            }
        }

        if (flags[2] != null) {
            TransferType type = te.getSideConfig().getType(flags[2]);
            if (!type.equals(TransferType.NONE)) {
                IVertexBuilder buffer = rtb.getBuffer(getRenderType(TEXTURES.get(type)));
                this.south.render(matrix, buffer, light, ov);
                this.southPlate.render(matrix, buffer, light, ov);
            }
        }
        if (flags[3] != null) {
            TransferType type = te.getSideConfig().getType(flags[3]);
            if (!type.equals(TransferType.NONE)) {
                IVertexBuilder buffer = rtb.getBuffer(getRenderType(TEXTURES.get(type)));
                this.north.render(matrix, buffer, light, ov);
                this.northPlate.render(matrix, buffer, light, ov);
            }
        }

        if (flags[4] != null) {
            TransferType type = te.getSideConfig().getType(flags[4]);
            if (!type.equals(TransferType.NONE)) {
                IVertexBuilder buffer = rtb.getBuffer(getRenderType(TEXTURES.get(type)));
                this.west.render(matrix, buffer, light, ov);
                this.westPlate.render(matrix, buffer, light, ov);
            }
        }

        if (flags[5] != null) {
            TransferType type = te.getSideConfig().getType(flags[5]);
            if (!type.equals(TransferType.NONE)) {
                IVertexBuilder buffer = rtb.getBuffer(getRenderType(TEXTURES.get(type)));
                this.east.render(matrix, buffer, light, ov);
                this.eastPlate.render(matrix, buffer, light, ov);
            }
        }
    }
}
