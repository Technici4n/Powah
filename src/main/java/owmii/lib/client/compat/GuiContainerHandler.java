package owmii.lib.client.compat;

import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.renderer.Rectangle2d;
import owmii.lib.client.screen.container.AbstractContainerScreen;
import owmii.lib.logistics.inventory.AbstractContainer;

import java.util.List;

public class GuiContainerHandler<C extends AbstractContainer> implements IGuiContainerHandler<AbstractContainerScreen<C>> {
    @Override
    @SuppressWarnings("unchecked")
    public List<Rectangle2d> getGuiExtraAreas(AbstractContainerScreen containerScreen) {
        return containerScreen.getExtraAreas();
    }
}
