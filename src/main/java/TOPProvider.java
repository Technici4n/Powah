//package owmii.lib.compat.top;
//
//import mcjty.theoneprobe.api.*;
//import net.minecraft.block.BlockState;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.world.World;
//
//import java.util.function.Function;
//
//public class TOPProvider implements IProbeInfoProvider, Function<ITheOneProbe, Void> {
//    @Override
//    public Void apply(ITheOneProbe iTheOneProbe) {
//        iTheOneProbe.registerProvider(this);
//        return null;
//    }
//
//    @Override
//    public String getID() {
//        return "lollipop:default";
//    }
//
//    @Override
//    public void addProbeInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, PlayerEntity playerEntity, World world, BlockState blockState, IProbeHitData iProbeHitData) {
//        if (blockState.getBlock() instanceof ITOPInfoProvider) {
//            ITOPInfoProvider provider = (ITOPInfoProvider) blockState.getBlock();
//            provider.addProbeInfo(probeMode, iProbeInfo, playerEntity, world, iProbeHitData.getPos(), blockState, iProbeHitData);
//        }
//    }
//}
