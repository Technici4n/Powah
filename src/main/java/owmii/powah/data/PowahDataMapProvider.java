package owmii.powah.data;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import owmii.powah.item.Itms;
import owmii.powah.recipe.ReactorFuel;

class PowahDataMapProvider extends DataMapProvider {
    public PowahDataMapProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void gather() {
        builder(ReactorFuel.DATA_MAP_TYPE)
                .add(Itms.URANINITE, new ReactorFuel(100, 700), false)
                .build();
    }
}
