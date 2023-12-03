package owmii.powah.forge;

import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import owmii.powah.Powah;
import owmii.powah.forge.compat.curios.CuriosCompat;
import owmii.powah.forge.data.DataEvents;
import owmii.powah.lib.util.Wrench;

@Mod(Powah.MOD_ID)
public class PowahForge {
    public PowahForge() {
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        Powah.init();
        modEventBus.addListener(DataEvents::gatherData);
        NeoForge.EVENT_BUS.addListener((PlayerInteractEvent.RightClickBlock event) -> {
            if (event.getUseBlock() == Event.Result.DENY) {
                return;
            }
            if (Wrench.removeWithWrench(event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec())) {
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.sidedSuccess(event.getLevel().isClientSide));
            }
        });

        if (FMLEnvironment.dist.isClient()) {
            try {
                Class.forName("owmii.powah.forge.client.PowahForgeClient").getMethod("init").invoke(null);
            } catch (Exception exception) {
                throw new RuntimeException("Failed to run powah forge client init", exception);
            }
        }

        if (ModList.get().isLoaded("curios")) {
            CuriosCompat.init();
        }
    }
}
