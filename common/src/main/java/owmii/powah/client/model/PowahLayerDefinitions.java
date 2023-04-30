package owmii.powah.client.model;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import owmii.powah.Powah;

public class PowahLayerDefinitions {
    public static final ModelLayerLocation CABLE = new ModelLayerLocation(Powah.id("models"), "cable");
    public static final ModelLayerLocation ORB = new ModelLayerLocation(Powah.id("models"), "orb");
    public static final ModelLayerLocation REACTOR_PART = new ModelLayerLocation(Powah.id("models"), "reactor_part");
    public static final ModelLayerLocation REACTOR = new ModelLayerLocation(Powah.id("models"), "reactor");

    public static void register() {
        EntityModelLayerRegistry.register(CABLE, CableModel::createDefinition);
        EntityModelLayerRegistry.register(ORB, OrbModel::createDefinition);
        EntityModelLayerRegistry.register(REACTOR_PART, () -> CubeModel.createDefinition(16));
        EntityModelLayerRegistry.register(REACTOR, ReactorModel::createDefinition);
    }
}
