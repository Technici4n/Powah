package owmii.powah.client.model;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import owmii.powah.Powah;

public class PowahLayerDefinitions {
    public static final ModelLayerLocation CABLE = new ModelLayerLocation(Powah.id("models"), "cable");
    public static final ModelLayerLocation ORB = new ModelLayerLocation(Powah.id("models"), "orb");
    public static final ModelLayerLocation REACTOR_PART = new ModelLayerLocation(Powah.id("models"), "reactor_part");
    public static final ModelLayerLocation REACTOR = new ModelLayerLocation(Powah.id("models"), "reactor");

    public static void register(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CABLE, CableModel::createDefinition);
        event.registerLayerDefinition(ORB, OrbModel::createDefinition);
        event.registerLayerDefinition(REACTOR_PART, () -> CubeModel.createDefinition(16));
        event.registerLayerDefinition(REACTOR, ReactorModel::createDefinition);
    }
}
