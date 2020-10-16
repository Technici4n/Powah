package owmii.powah.block;

import owmii.lib.registry.IVariant;

public enum Tier implements IVariant<Tier> {
    STARTER, BASIC, HARDENED, BLAZING, NIOTIC, SPIRITED, NITRO, CREATIVE;

    @Override
    public Tier[] getVariants() {
        return values();
    }

    public static Tier[] getNormalVariants() {
        return new Tier[]{STARTER, BASIC, HARDENED, BLAZING, NIOTIC, SPIRITED, NITRO};
    }
}
