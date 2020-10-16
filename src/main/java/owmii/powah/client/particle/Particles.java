package owmii.powah.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Particles {
    public static BasicParticleType ENERGIZING = new BasicParticleType(false);


    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<ParticleType<?>> registry) {
        registry.getRegistry().registerAll(
                ENERGIZING.setRegistryName("glow_small")
        );
    }

    @SubscribeEvent
    public static void registerParticleTypes(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particles.registerFactory(ENERGIZING, EnergizingParticle.Factory::new);
    }
}
