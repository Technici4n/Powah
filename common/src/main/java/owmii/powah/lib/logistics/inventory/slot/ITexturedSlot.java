package owmii.powah.lib.logistics.inventory.slot;

import owmii.powah.lib.client.screen.Texture;

public interface ITexturedSlot<T extends ITexturedSlot> {
    Texture getOverlay();

    T setOverlay(Texture overlay);

    Texture getBackground2();

    T setBackground(Texture background);
}
