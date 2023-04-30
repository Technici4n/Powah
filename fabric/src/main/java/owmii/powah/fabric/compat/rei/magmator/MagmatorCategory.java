package owmii.powah.fabric.compat.rei.magmator;

import java.util.ArrayList;
import java.util.List;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import owmii.powah.Powah;
import owmii.powah.block.Blcks;
import owmii.powah.block.Tier;

public class MagmatorCategory implements DisplayCategory<MagmatorDisplay> {
    public static final ResourceLocation GUI_BACK = new ResourceLocation(Powah.MOD_ID, "textures/gui/jei/misc.png");
    public static final CategoryIdentifier<MagmatorDisplay> ID = CategoryIdentifier.of(new ResourceLocation(Powah.MOD_ID, "magmatic"));
    private final Renderer icon;

    public MagmatorCategory() {
        this.icon = EntryStacks.of(Blcks.MAGMATOR.get(Tier.BASIC));

    }

    @Override
    public CategoryIdentifier<MagmatorDisplay> getCategoryIdentifier() {
        return ID;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("gui.powah.jei.category.magmatic");
    }

    @Override
    public int getDisplayWidth(MagmatorDisplay display) {
        return 168;
    }

    @Override
    public int getDisplayHeight() {
        return 36;
    }

    @Override
    public Renderer getIcon() {
        return this.icon;
    }

    @Override
    public List<Widget> setupDisplay(MagmatorDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        Point origin = new Point(bounds.getX() + 5, bounds.getY() + 5);
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createTexturedWidget(GUI_BACK, origin.x, origin.y + 1, 0, 0, 160, 24));
        widgets.add(Widgets.createSlot(new Point(origin.x + 4, origin.y + 5))
                .markInput()
                .entries(display.getInputEntries().get(0)));
        widgets.add(Widgets.createDrawableWidget((helper, matrices, mouseX, mouseY, delta) -> {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.font.draw(matrices, display.getHeat() + " FE/100 mb", origin.x + 27.0F, origin.y + 9.0F, 0x444444);
        }));
        return widgets;
    }
}
