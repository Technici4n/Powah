package owmii.powah.compat.rei;

import java.util.Collection;
import java.util.List;
import me.shedaniel.rei.api.common.plugins.REIPluginProvider;
import me.shedaniel.rei.forge.REIPluginLoaderClient;

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
