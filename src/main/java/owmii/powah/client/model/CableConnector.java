package owmii.powah.client.model;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.util.Direction;

public class CableConnector extends Model {
    private final RendererModel up;
    private final RendererModel down;
    private final RendererModel east;
    private final RendererModel west;
    private final RendererModel north;
    private final RendererModel south;

    public CableConnector() {
        this.textureWidth = 32;
        this.textureHeight = 32;

        this.north = new RendererModel(this);
        this.north.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.north.cubeList.add(new ModelBox(this.north, 0, 0, -1.0F, -9.0F, -8.0F, 2, 2, 7, 0.0F, false));
        this.south = new RendererModel(this);
        this.south.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.south.cubeList.add(new ModelBox(this.south, 0, 0, -1.0F, -9.0F, 1.0F, 2, 2, 7, 0.0F, false));
        this.west = new RendererModel(this);
        this.west.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.west.cubeList.add(new ModelBox(this.west, 0, 16, -8.0F, -9.0F, -1.0F, 7, 2, 2, 0.0F, false));
        this.east = new RendererModel(this);
        this.east.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.east.cubeList.add(new ModelBox(this.east, 0, 16, 1.0F, -9.0F, -1.0F, 7, 2, 2, 0.0F, false));
        this.down = new RendererModel(this);
        this.down.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.down.cubeList.add(new ModelBox(this.down, 5, 7, -1.0F, -16.0F, -1.0F, 2, 7, 2, 0.0F, false));
        this.up = new RendererModel(this);
        this.up.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.up.cubeList.add(new ModelBox(this.up, 5, 7, -1.0F, -7.0F, -1.0F, 2, 7, 2, 0.0F, false));
    }

    public void render(Direction side, float scale) {
        if (side == Direction.UP) {
            this.up.render(scale);
        } else if (side == Direction.DOWN) {
            this.down.render(scale);
        } else if (side == Direction.EAST) {
            this.east.render(scale);
        } else if (side == Direction.WEST) {
            this.west.render(scale);
        } else if (side == Direction.NORTH) {
            this.north.render(scale);
        } else if (side == Direction.SOUTH) {
            this.south.render(scale);
        }
    }

    public void setRotationAngle(RendererModel modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
