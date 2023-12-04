package owmii.powah.forge.compat.rei;

import me.shedaniel.rei.api.common.plugins.REIPluginProvider;
import me.shedaniel.rei.forge.REIPluginLoaderClient;
import owmii.powah.compat.rei.PowahREIPlugin;

import java.util.Collection;
import java.util.List;

@REIPluginLoaderClient
public class PowahREIPluginProvider implements REIPluginProvider<PowahREIPlugin> {
    @Override
    public Collection<PowahREIPlugin> provide() {
        return List.of(new PowahREIPlugin());
    }

    @Override
    public Class<PowahREIPlugin> getPluginProviderClass() {
        return PowahREIPlugin.class;
    }
}
