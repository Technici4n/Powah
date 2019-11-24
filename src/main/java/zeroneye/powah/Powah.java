package zeroneye.powah;

import net.minecraft.fluid.Fluids;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import zeroneye.powah.api.PowahAPI;
import zeroneye.powah.block.generator.magmatic.MagmaticGenTile;
import zeroneye.powah.client.gui.IScreens;
import zeroneye.powah.client.renderer.tile.MagmaticGenRenderer;
import zeroneye.powah.network.Packets;

import static zeroneye.lib.Lollipop.addModListener;

@Mod(Powah.MOD_ID)
public class Powah {
    public static final String MOD_ID = "powah";

    public Powah() {
        addModListener(this::commonSetup);
        addModListener(this::clientSetup);
    }

    void commonSetup(FMLCommonSetupEvent event) {
        PowahAPI.registerMagmaticFluid(Fluids.LAVA, 10000);
        Packets.register();
    }

    void clientSetup(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntitySpecialRenderer(MagmaticGenTile.class, new MagmaticGenRenderer());
        IScreens.register();
    }
}