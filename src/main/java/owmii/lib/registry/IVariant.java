package owmii.lib.registry;

import java.util.Locale;
import net.minecraft.nbt.CompoundTag;

public interface IVariant<V extends Enum<V> & IVariant<V>> {
    V[] getVariants();

    default String getName() {
        return ((Enum<?>) this).name().toLowerCase(Locale.ENGLISH);
    }

    default V read(CompoundTag nbt, String key) {
        return getVariants()[nbt.getInt(key)];
    }

    default CompoundTag write(CompoundTag nbt, V v, String key) {
        nbt.putInt(key, ((Enum<?>) this).ordinal());
        return nbt;
    }

    default boolean isEmpty() {
        return this instanceof IVariant.Single || getVariants().length == 0;
    }

    @SuppressWarnings("unchecked")
    static <T extends IVariant> T getEmpty() {
        return (T) Single.SINGLE;
    }

    int ordinal();

    enum Single implements IVariant<Single> {
        SINGLE;

        @Override
        public Single[] getVariants() {
            return new Single[0];
        }
    }
}
