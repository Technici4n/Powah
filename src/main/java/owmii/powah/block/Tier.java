package owmii.powah.block;

import owmii.lib.util.IVariant;

public enum Tier implements IVariant<Tier> {
    STARTER(0),
    BASIC(0),
    HARDENED(0),
    BLAZING(0),
    NIOTIC(0),
    SPIRITED(0),
    NITRO(0),
    CREATIVE(0);

    private final int[] colors;

    Tier(int... colors) {
        this.colors = colors;
    }

    public int[] getColors() {
        return this.colors;
    }

    @Override
    public Tier[] getAll() {
        return values();
    }
}
