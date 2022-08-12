package owmii.powah.fabric.compat.rei;

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
import owmii.powah.Powah;
import owmii.powah.block.Blcks;

import java.util.ArrayList;
import java.util.List;

public class SolidCoolantCategory implements DisplayCategory<SolidCoolantDisplay> {
    public static final ResourceLocation GUI_BACK = new ResourceLocation(Powah.MOD_ID, "textures/gui/jei/misc.png");
    public static final CategoryIdentifier<SolidCoolantDisplay> ID = CategoryIdentifier.of(new ResourceLocation(Powah.MOD_ID, "solid.coolants"));
    private final Renderer icon;

    public SolidCoolantCategory() {
        this.icon = EntryStacks.of(Blcks.DRY_ICE.get());
    }

    @Override
    public CategoryIdentifier<SolidCoolantDisplay> getCategoryIdentifier() {
        return ID;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("gui.powah.jei.category.solid.coolant");
    }

    @Override
    public Renderer getIcon() {
        return this.icon;
    }

    @Override
    public int getDisplayWidth(SolidCoolantDisplay display) {
        return 168;
    }

    @Override
    public int getDisplayHeight() {
        return 36;
    }

    @Override
    public List<Widget> setupDisplay(SolidCoolantDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        Point origin = new Point(bounds.getX() + 5, bounds.getY() + 5);
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createTexturedWidget(GUI_BACK, origin.x, origin.y + 1, 0, 0, 160, 24));
        widgets.add(Widgets.createSlot(new Point(origin.x + 4, origin.y + 5))
                .disableBackground()
                .markInput()
                .entries(display.getInputEntries().get(0)));
        widgets.add(Widgets.createDrawableWidget((helper, matrices, mouseX, mouseY, delta) -> {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.font.draw(matrices, I18n.get("info.lollipop.amount") + ": " + I18n.get("info.lollipop.mb", display.getAmount()), origin.x + 30.0F, origin.y + 3.0F, 0x444444);
            minecraft.font.draw(matrices, I18n.get("info.lollipop.temperature") + ": " + I18n.get("info.lollipop.temperature.c", "" + ChatFormatting.DARK_AQUA + display.getColdness()), origin.x + 30.0F, origin.y + 15.0F, 0x444444);
        }));

        return widgets;
    }
}