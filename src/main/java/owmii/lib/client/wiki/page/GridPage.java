package owmii.lib.client.wiki.page;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.lib.client.screen.Texture;
import owmii.lib.client.screen.widget.IconButton;
import owmii.lib.client.screen.wiki.WikiScreen;
import owmii.lib.client.util.MC;
import owmii.lib.client.wiki.Entry;
import owmii.lib.client.wiki.Section;

public class GridPage extends EntriesPage {
    public GridPage(Section parent) {
        this("", parent);
    }

    public GridPage(String name, Section parent) {
        super(name, parent);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void init(int x, int y, WikiScreen screen) {
        super.init(x, y, screen);
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 6; ++j) {
                int index = j + i * 6;
                if (index < this.entries.size()) {
                    Entry e = this.entries.get(index);
                    screen.addButton2(new IconButton(27 + x + 13 + j * 29, 17 + y + 13 + i * 29, e.getStack(), Texture.WIKI_FRM, button -> {
                        MC.open(new WikiScreen(e.getSections(0)));
                    }, screen).setTooltip(tooltip -> tooltip.add(new TranslationTextComponent(e.getTransKey()))));
                } else break;
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(MatrixStack matrix, int x, int y, int mx, int my, float pt, FontRenderer font, WikiScreen screen) {
        font.func_243248_b(matrix, new TranslationTextComponent(getSection().getEntry().getTransKey()), x + 10, y + 10, 0x444444);
    }
}
