package owmii.powah.fabric.compat.rei.energizing;

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
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import owmii.powah.Powah;
import owmii.powah.block.Blcks;
import owmii.powah.lib.util.Util;

public class EnergizingCategory implements DisplayCategory<EnergizingDisplay> {
    public static final ResourceLocation GUI_BACK = new ResourceLocation(Powah.MOD_ID, "textures/gui/jei/energizing.png");
    public static final CategoryIdentifier<EnergizingDisplay> ID = CategoryIdentifier.of(new ResourceLocation(Powah.MOD_ID, "energizing"));
    private final Renderer icon;

    public EnergizingCategory() {
        this.icon = EntryStacks.of(Blcks.ENERGIZING_ORB.get());
    }

    @Override
    public CategoryIdentifier<EnergizingDisplay> getCategoryIdentifier() {
        return ID;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("gui.powah.jei.category.energizing");
    }

    @Override
    public int getDisplayWidth(EnergizingDisplay display) {
        return 168;
    }

    @Override
    public int getDisplayHeight() {
        return 46;
    }

    @Override
    public Renderer getIcon() {
        return this.icon;
    }

    @Override
    public List<Widget> setupDisplay(EnergizingDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        Point origin = new Point(bounds.getX() + 5, bounds.getY() + 5);
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createTexturedWidget(GUI_BACK, origin.x, origin.y + 1, 0, 0, 160, 38));
        int size = display.getInputEntries().size();
        for (int i = 0; i < size; i++) {
            widgets.add(Widgets.createSlot(new Point(origin.x + (i * 20) + 4, origin.y + 5))
                    .disableBackground()
                    .markInput()
                    .entries(display.getInputEntries().get(i)));
        }
        widgets.add(Widgets.createSlot(new Point(origin.x + 137, origin.y + 5))
                .disableBackground()
                .markOutput()
                .entries(display.getOutputEntries().get(0)));
        widgets.add(Widgets.createDrawableWidget((gui, mouseX, mouseY, delta) -> {
            Minecraft minecraft = Minecraft.getInstance();
            gui.drawString(minecraft.font, I18n.get("info.lollipop.fe", Util.addCommas(display.getEnergy())), origin.x + 2, origin.y + 29,
                    0x444444, false);
        }));
        return widgets;
    }
}
