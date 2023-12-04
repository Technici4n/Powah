package owmii.powah.compat.rei;

import java.util.List;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.registry.screen.ExclusionZonesProvider;
import net.minecraft.client.renderer.Rect2i;
import owmii.powah.lib.client.screen.container.AbstractContainerScreen;
import owmii.powah.lib.logistics.inventory.AbstractContainer;

public class GuiContainerHandler<C extends AbstractContainer> implements ExclusionZonesProvider<AbstractContainerScreen<C>> {
    @Override
    @SuppressWarnings("unchecked")
    public List<Rectangle> provide(AbstractContainerScreen containerScreen) {
        return containerScreen.getExtraAreas().stream().map(o -> {
            Rect2i rec = (Rect2i) o;
            return new Rectangle(rec.getX(), rec.getY(), rec.getWidth(), rec.getWidth());
        }).toList();
    }
}
