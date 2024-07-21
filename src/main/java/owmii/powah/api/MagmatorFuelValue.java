package owmii.powah.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import owmii.powah.Powah;

/**
 * Fuel for the {@link owmii.powah.block.magmator.MagmatorTile}.
 *
 * @param energyProduced FE per 100mb of fluid
 */
public record MagmatorFuelValue(int energyProduced) {
    public static final Codec<MagmatorFuelValue> CODEC = RecordCodecBuilder.create(builder -> builder
            .group(
                    Codec.INT.fieldOf("energy_produced").forGetter(MagmatorFuelValue::energyProduced))
            .apply(builder, MagmatorFuelValue::new));

    public static final DataMapType<Fluid, MagmatorFuelValue> DATA_MAP_TYPE = DataMapType.builder(Powah.id("magmator_fuel"), Registries.FLUID, CODEC)
            .synced(CODEC, true)
            .build();
}
