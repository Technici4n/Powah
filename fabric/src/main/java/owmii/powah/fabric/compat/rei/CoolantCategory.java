package owmii.powah.fabric.compat.rei;

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
import net.minecraft.world.item.Items;
import owmii.powah.Powah;

public class CoolantCategory implements DisplayCategory<CoolantDisplay> {
    public static final ResourceLocation GUI_BACK = new ResourceLocation(Powah.MOD_ID, "textures/gui/jei/misc.png");
    public static final CategoryIdentifier<CoolantDisplay> ID = CategoryIdentifier.of(new ResourceLocation(Powah.MOD_ID, ".coolant"));
    private final Renderer icon;

    public CoolantCategory() {
        this.icon = EntryStacks.of(Items.WATER_BUCKET);
    }

    @Override
    public CategoryIdentifier<CoolantDisplay> getCategoryIdentifier() {
        return ID;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("gui.powah.jei.category.coolant");
    }

    @Override
    public int getDisplayWidth(CoolantDisplay display) {
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
    public List<Widget> setupDisplay(CoolantDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        Point origin = new Point(bounds.getX() + 5, bounds.getY() + 5);
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createTexturedWidget(GUI_BACK, origin.x, origin.y + 1, 0, 0, 160, 24));
        widgets.add(Widgets.createSlot(new Point(origin.x + 4, origin.y + 5))
                .disableBackground()
                .markInput()
                .entries(display.getInputEntries().get(0)));
        widgets.add(Widgets.createDrawableWidget((gui, mouseX, mouseY, delta) -> {
            Minecraft minecraft = Minecraft.getInstance();
            gui.drawString(minecraft.font,
                    I18n.get("info.lollipop.temperature") + ": "
                            + I18n.get("info.lollipop.temperature.c", "" + ChatFormatting.DARK_AQUA + display.getColdness()),
                    origin.x + 30, origin.y + 9, 0x444444, false);
        }));
        return widgets;
    }

}
