package owmii.powah.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import owmii.powah.Powah;

public record SolidCoolantConfig(int amount, int temperature) {
    public static final Codec<SolidCoolantConfig> CODEC = RecordCodecBuilder.create(builder -> builder
            .group(
                    Codec.INT.fieldOf("amount").forGetter(SolidCoolantConfig::amount),
                    Codec.INT.fieldOf("temperature").forGetter(SolidCoolantConfig::temperature))
            .apply(builder, SolidCoolantConfig::new));
    public static final DataMapType<Item, SolidCoolantConfig> DATA_MAP_TYPE = DataMapType.builder(Powah.id("solid_coolant"), Registries.ITEM, CODEC)
            .synced(CODEC, true)
            .build();
}
