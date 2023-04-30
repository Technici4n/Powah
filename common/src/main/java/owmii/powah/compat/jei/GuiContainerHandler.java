package owmii.powah.compat.jei;

import java.util.List;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.renderer.Rect2i;
import owmii.powah.lib.client.screen.container.AbstractContainerScreen;
import owmii.powah.lib.logistics.inventory.AbstractContainer;

public class GuiContainerHandler<C extends AbstractContainer> implements IGuiContainerHandler<AbstractContainerScreen<C>> {
    @Override
    @SuppressWarnings("unchecked")
    public List<Rect2i> getGuiExtraAreas(AbstractContainerScreen containerScreen) {
        return containerScreen.getExtraAreas();
    }
}
