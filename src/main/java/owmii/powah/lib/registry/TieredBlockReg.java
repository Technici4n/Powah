package owmii.powah.lib.registry;

import java.util.LinkedHashMap;
import java.util.List;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import owmii.powah.block.Tier;
import owmii.powah.lib.block.PowahBaseEnergyBlock;

public class TieredBlockReg {
    private final LinkedHashMap<Tier, DeferredBlock<PowahBaseEnergyBlock<?>>> all = new LinkedHashMap<>();

    public TieredBlockReg(DeferredRegister.Blocks dr, String name, Factory factory, Tier[] variants) {
        for (Tier variant : variants) {
            var entryName = name + "_" + variant.getSerializedName();
            this.all.put(variant, dr.registerBlock(entryName, props -> factory.get(variant, props)));
        }
    }

    public PowahBaseEnergyBlock<?>[] getArr() {
        return getAll().toArray(PowahBaseEnergyBlock<?>[]::new);
    }

    public List<? extends PowahBaseEnergyBlock<?>> getAll() {
        return all.values().stream().map(DeferredBlock::get).toList();
    }

    public List<ResourceKey<Block>> getAllResourceKeys() {
        return all.values().stream().map(DeferredBlock::getKey).toList();
    }

    public PowahBaseEnergyBlock<?> get(Tier variant) {
        return this.all.get(variant).get();
    }

    @FunctionalInterface
    public interface Factory {
        PowahBaseEnergyBlock<?> get(Tier variant, BlockBehaviour.Properties properties);
    }
}
