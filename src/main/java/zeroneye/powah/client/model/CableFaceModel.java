package zeroneye.powah.client.model;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CableFaceModel extends Model {
    final RendererModel south;
    final RendererModel north;
    final RendererModel east;
    final RendererModel west;
    final RendererModel up;
    final RendererModel down;

    public CableFaceModel() {
        this.textureWidth = 12;
        this.textureHeight = 6;

        this.south = new RendererModel(this, 0, 0);
        this.south.addBox(-2.5F, -2.5F, 7F, 5, 5, 1);
        this.south.setRotationPoint(0F, 16F, 0F);
        this.south.setTextureSize(64, 32);
        this.south.mirror = true;
        setRotation(this.south, 0F, 0F, 0F);
        this.north = new RendererModel(this, 0, 0);
        this.north.addBox(-2.5F, -2.5F, -8F, 5, 5, 1);
        this.north.setRotationPoint(0F, 16F, 0F);
        this.north.setTextureSize(64, 32);
        this.north.mirror = true;
        setRotation(this.north, 0F, 0F, 0F);
        this.east = new RendererModel(this, 0, 0);
        this.east.addBox(-2.5F, -2.5F, -8F, 5, 5, 1);
        this.east.setRotationPoint(0F, 16F, 0F);
        this.east.setTextureSize(64, 32);
        this.east.mirror = true;
        setRotation(this.east, 0F, -1.570796F, 0F);
        this.west = new RendererModel(this, 0, 0);
        this.west.addBox(-2.5F, -2.5F, 7F, 5, 5, 1);
        this.west.setRotationPoint(0F, 16F, 0F);
        this.west.setTextureSize(64, 32);
        this.west.mirror = true;
        setRotation(this.west, 0F, -1.570796F, 0F);
        this.up = new RendererModel(this, 0, 0);
        this.up.addBox(-2.5F, -2.5F, -8F, 5, 5, 1);
        this.up.setRotationPoint(0F, 16F, 0F);
        this.up.setTextureSize(64, 32);
        this.up.mirror = true;
        setRotation(this.up, 1.570796F, -1.570796F, 0F);
        this.down = new RendererModel(this, 0, 0);
        this.down.addBox(-2.5F, -2.5F, 7F, 5, 5, 1);
        this.down.setRotationPoint(0F, 16F, 0F);
        this.down.setTextureSize(64, 32);
        this.down.mirror = true;
        setRotation(this.down, 1.570796F, -1.570796F, 0F);
    }

    public void render(Direction side, float scale, float f) {
        float sc = 0.0310f;
        if (side == Direction.UP) {
            GlStateManager.pushMatrix();
            GlStateManager.translatef(0, sc, 0);
            this.up.render(scale);
            GlStateManager.popMatrix();
        } else if (side == Direction.DOWN) {
            GlStateManager.pushMatrix();
            GlStateManager.translatef(0, -sc, 0);
            this.down.render(scale);
            GlStateManager.popMatrix();
        } else if (side == Direction.EAST) {
            GlStateManager.pushMatrix();
            GlStateManager.translatef(sc, 0, 0);
            this.east.render(scale);
            GlStateManager.popMatrix();
        } else if (side == Direction.WEST) {
            GlStateManager.pushMatrix();
            GlStateManager.translatef(-sc, 0, 0);
            this.west.render(scale);
            GlStateManager.popMatrix();
        } else if (side == Direction.NORTH) {
            GlStateManager.pushMatrix();
            GlStateManager.translatef(0, 0, -sc);
            this.north.render(scale);
            GlStateManager.popMatrix();
        } else if (side == Direction.SOUTH) {
            GlStateManager.pushMatrix();
            GlStateManager.translatef(0, 0, sc);
            this.south.render(scale);
            GlStateManager.popMatrix();
        }
    }

    public void setRotation(RendererModel model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
