package owmii.powah.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import org.jetbrains.annotations.Nullable;
import owmii.powah.Powah;

public record ReactorFuel(int fuelAmount, int temperature) {
    private static final Codec<ReactorFuel> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.INT.fieldOf("fuelAmount").forGetter(ReactorFuel::fuelAmount),
            Codec.INT.fieldOf("temperature").forGetter(ReactorFuel::temperature)).apply(builder, ReactorFuel::new));

    public static final DataMapType<Item, ReactorFuel> DATA_MAP_TYPE = DataMapType.builder(
            Powah.id("reactor_fuel"),
            Registries.ITEM,
            CODEC).synced(CODEC, true).build();

    @Nullable
    public static ReactorFuel getFuel(ItemLike item) {
        return item.asItem().builtInRegistryHolder().getData(DATA_MAP_TYPE);
    }
}
