package owmii.powah.compat.rei;

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
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import owmii.powah.Powah;

public class HeatSourceCategory implements DisplayCategory<HeatSourceDisplay> {
    public static final ResourceLocation GUI_BACK = new ResourceLocation(Powah.MOD_ID, "textures/gui/jei/misc.png");
    public static final CategoryIdentifier<HeatSourceDisplay> ID = CategoryIdentifier.of(new ResourceLocation(Powah.MOD_ID, "heat.sources"));
    private final Renderer icon;

    public HeatSourceCategory() {
        this.icon = EntryStacks.of(Blocks.MAGMA_BLOCK);
    }

    @Override
    public CategoryIdentifier<HeatSourceDisplay> getCategoryIdentifier() {
        return ID;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("gui.powah.jei.category.heat.sources");
    }

    @Override
    public Renderer getIcon() {
        return this.icon;
    }

    @Override
    public int getDisplayWidth(HeatSourceDisplay display) {
        return 160;
    }

    @Override
    public int getDisplayHeight() {
        return 36;
    }

    @Override
    public List<Widget> setupDisplay(HeatSourceDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        Point origin = new Point(bounds.getX() + 5, bounds.getY() + 5);
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createTexturedWidget(GUI_BACK, origin.x, origin.y + 1, 0, 0, 160, 24));
        widgets.add(Widgets.createSlot(new Point(3, 4))
                .disableBackground()
                .markInput()
                .entries(display.getInputEntries().get(0)));
        widgets.add(Widgets.createDrawableWidget((gui, mouseX, mouseY, delta) -> {
            Minecraft minecraft = Minecraft.getInstance();
            gui.drawString(minecraft.font, ChatFormatting.DARK_GRAY + I18n.get("info.lollipop.temperature") + ": " + ChatFormatting.RESET
                    + I18n.get("info.lollipop.temperature.c", display.getHeat()), origin.x + 30, origin.y + 9, 0xc43400, false);
        }));
        return widgets;
    }
}
