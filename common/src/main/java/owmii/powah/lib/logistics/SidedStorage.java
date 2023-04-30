package owmii.powah.lib.logistics;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;

/**
 * Used for storing a value for null and all the directions.
 */
public final class SidedStorage<T> {
    private final T nullValue;
    private final EnumMap<Direction, T> sidedValues;

    private SidedStorage(T nullValue, EnumMap<Direction, T> sidedValues) {
        this.nullValue = nullValue;
        this.sidedValues = sidedValues;
    }

    /**
     * Calls the supplier for null and all the directions.
     */
    public static <T> SidedStorage<T> create(SideSupplier<T> supplier) {
        return new SidedStorage<>(
                supplier.get(null),
                Arrays.stream(Direction.values())
                        .collect(Collectors.toMap(side -> side, supplier::get, (a, b) -> b, () -> new EnumMap<>(Direction.class))));
    }

    public T get(@Nullable Direction side) {
        if (side == null)
            return nullValue;
        return sidedValues.get(side);
    }

    public Stream<T> stream() {
        return Stream.concat(Stream.of(nullValue), sidedValues.values().stream());
    }

    @FunctionalInterface
    public interface SideSupplier<T> {
        T get(@Nullable Direction side);
    }
}
