package owmii.lib.client.wiki.page.panel;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.lib.client.screen.Texture;
import owmii.lib.client.screen.wiki.WikiScreen;
import owmii.lib.client.util.Text;
import owmii.lib.client.wiki.Section;

import java.util.List;
import java.util.Map;

public class EnergyPanel<T extends IItemProvider> extends ItemPanel<T> {
    public EnergyPanel(Section parent) {
        super(parent);
    }

    public EnergyPanel(T item, Section parent) {
        super(item, parent);
    }

    public EnergyPanel(List<T> items, Section parent) {
        super(items, parent);
    }

    public EnergyPanel(IItemProvider[] items, Section parent) {
        super(items, parent);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(MatrixStack matrix, int x, int y, int mx, int my, float pt, FontRenderer font, WikiScreen screen) {
        super.render(matrix, x, y, mx, my, pt, font, screen);
        if (getItem() instanceof InfoBox.IInfoBoxHolder) {
            int i = 0;
            InfoBox.IInfoBoxHolder holder = (InfoBox.IInfoBoxHolder) getItem();
            InfoBox box = holder.getInfoBox(new ItemStack(getItem()), new InfoBox(0xff0000, 0x3d3d3d));
            for (Map.Entry<TextComponent, TextComponent> entry : box.getLines().entrySet()) {
                Texture.WIKI_INF_LN_BG.draw(matrix, x + 9, y + 80 + (i * 12));
                Texture.WIKI_INF_LN_BG.draw(matrix, x + 9, y + 102 + (i * 12));
                font.func_243248_b(matrix, entry.getKey().append(Text.COLON), x + 15, y + 86 + (i * 12), 0x7C898B);
                font.func_243248_b(matrix, entry.getValue().setStyle(Text.color(0x3F535B)), x + 15, y + 108 + (i * 12), 0x000000);
                y += 32;
                i++;
            }
        }
    }
}
