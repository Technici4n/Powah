package owmii.powah.client.model;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelBox;

public class ReactorModel extends Model {
    private final RendererModel bb_main;
    private final RendererModel core;

    public ReactorModel() {
        this.textureWidth = 328;
        this.textureHeight = 200;

        this.bb_main = new RendererModel(this);
        this.bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.bb_main.cubeList.add(new ModelBox(this.bb_main, 136, 50, -24.0F, -64.0F, -24.0F, 48, 6, 48, 0.0F, false));
        this.bb_main.cubeList.add(new ModelBox(this.bb_main, 128, 146, -24.0F, -6.0F, -24.0F, 48, 6, 48, 0.0F, false));
        this.bb_main.cubeList.add(new ModelBox(this.bb_main, 0, 98, -24.0F, -16.0F, -8.0F, 1, 10, 16, 0.0F, false));
        this.bb_main.cubeList.add(new ModelBox(this.bb_main, 0, 0, 23.0F, -16.0F, -8.0F, 1, 10, 16, 0.0F, false));
        this.bb_main.cubeList.add(new ModelBox(this.bb_main, 0, 124, -8.0F, -16.0F, -24.0F, 16, 10, 1, 0.0F, false));
        this.bb_main.cubeList.add(new ModelBox(this.bb_main, 0, 26, -8.0F, -16.0F, 23.0F, 16, 10, 1, 0.0F, false));
        this.core = new RendererModel(this);
        this.core.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.core.cubeList.add(new ModelBox(this.core, 0, 98, -22.0F, -58.0F, -22.0F, 44, 52, 44, 0.0F, false));

    }

    public void render() {
        this.bb_main.render(0.0625f);

        GlStateManager.pushMatrix();
        GlStateManager.scalef(1.05F, 1.0F, 1.05F);
        this.core.render(0.0625f);
        GlStateManager.popMatrix();
    }

    public void setRotationAngle(RendererModel modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
