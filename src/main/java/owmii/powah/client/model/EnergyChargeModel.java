package owmii.powah.client.model;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;

public class EnergyChargeModel extends Model {
    final RendererModel Shape1;

    public EnergyChargeModel() {
        this.textureWidth = 20;
        this.textureHeight = 10;

        this.Shape1 = new RendererModel(this, 0, 0);
        this.Shape1.addBox(-2.5F, -2.5F, -2.5F, 5, 5, 5);
        this.Shape1.setRotationPoint(0F, 0F, 0F);
        this.Shape1.setTextureSize(20, 10);
        this.Shape1.mirror = true;
        setRotation(this.Shape1, 0F, 0F, 0F);
    }

    public void render(float scale) {
        this.Shape1.render(scale);
    }

    private void setRotation(RendererModel model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
