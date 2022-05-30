package owmii.lib.registry;

import net.minecraft.nbt.CompoundNBT;

import java.util.Locale;

public interface IVariant<V extends Enum<V> & IVariant<V>> {
    V[] getVariants();

    default String getName() {
        return ((Enum<?>) this).name().toLowerCase(Locale.ENGLISH);
    }

    default V read(CompoundNBT nbt, String key) {
        return getVariants()[nbt.getInt(key)];
    }

    default CompoundNBT write(CompoundNBT nbt, V v, String key) {
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
