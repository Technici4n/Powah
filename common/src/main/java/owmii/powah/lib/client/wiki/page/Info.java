package owmii.powah.lib.client.wiki.page;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import owmii.powah.lib.client.screen.Texture;
import owmii.powah.lib.client.screen.wiki.WikiScreen;
import owmii.powah.lib.client.util.Text;
import owmii.powah.lib.client.wiki.Entry;
import owmii.powah.lib.client.wiki.Page;
import owmii.powah.lib.client.wiki.Section;

public class Info extends Page {
    private final List<Component> cache = new ArrayList<>();
    private final Texture img;
    private final int paragraphs;
    private final Object[][] args;

    public Info(String name, Object[][] args, Section parent) {
        this(name, 1, args, parent);
    }

    public Info(String name, Section parent) {
        this(name, 1, parent);
    }

    public Info(String name, int paragraphs, Object[][] args, Section parent) {
        this(name, Texture.EMPTY, paragraphs, args, parent);
    }

    public Info(String name, int paragraphs, Section parent) {
        this(name, Texture.EMPTY, paragraphs, parent);
    }

    public Info(int paragraphs, Object[][] args, Section parent) {
        this(Texture.EMPTY, paragraphs, args, parent);

    }

    public Info(Section parent) {
        this(Texture.EMPTY, 1, parent);
    }

    public Info(int paragraphs, Section parent) {
        this(Texture.EMPTY, paragraphs, parent);
    }

    public Info(Texture img, Object[][] args, Section parent) {
        this(img, 1, args, parent);
    }

    public Info(Texture img, Section parent) {
        this(img, 1, parent);
    }

    public Info(Texture img, int paragraphs, Object[][] args, Section parent) {
        this(parent.getEntry().getTransKey(), img, paragraphs, args, parent);
    }

    public Info(Texture img, int paragraphs, Section parent) {
        this(parent.getEntry().getTransKey(), img, paragraphs, parent);
    }

    public Info(String name, Texture img, int paragraphs, Section parent) {
        this(name, img, paragraphs, new Object[paragraphs][0], parent);
    }

    public Info(String name, Texture img, int paragraphs, Object[][] args, Section parent) {
        super(name, parent);
        this.img = img;
        this.paragraphs = paragraphs;
        this.args = args;
    }

    @Override
    public void init(int x, int y, WikiScreen screen) {
        super.init(x, y, screen);
        this.cache.clear();
        int pp = 0;
        Page page = this;
        while (page != null && page.prev() != null) {
            Page prev = page.prev();
            if (prev instanceof Info) {
                pp += ((Info) prev).paragraphs;
            }
            page = prev;
        }
        Entry e = getSection().getEntry();
        for (int i = 0; i < this.paragraphs; i++) {
            Component text = Component.translatable("wiki." + e.getWiki().getModId() + "." + e.getName() + "_" + (i + pp), this.args[i]);
            MutableComponent ft = Component.translatable("");
            String[] words = text.getString().split("\\s+");
            for (int j = 0; j < words.length; j++) {
                String w = words[j];
                if (w.startsWith("<") && w.contains(":") && w.endsWith(">")) {
                    Item item = Registry.ITEM.get(new ResourceLocation(w.substring(1, w.length() - 1)));
                    ft = ft.append(new ItemStack(item).getHoverName().plainCopy().withStyle(ChatFormatting.BLUE)).append(" ");
                } else {
                    ft = ft.append(w).append(" ");
                }
            }
            ft.withStyle(Text.color(0x1F373F));
            this.cache.add(ft);
        }
    }

    @Override
    public void render(PoseStack matrix, int x, int y, int mx, int my, float pt, Font font, WikiScreen screen) {
        if (!this.img.isEmpty()) {
            this.img.draw(matrix, x + 3, y + 3);
            y += this.img.getHeight() + 2;
        }
        for (int i = 0; i < this.cache.size(); i++) {
            Component text = this.cache.get(i);
            Text.drawString(text, x + 6, y + 7, screen.w / 2 - 5, 10, 0x38453c);
            y += (i + 1 == this.cache.size() ? 0 : 3) + font.split(text, screen.w / 2 - 5).size() * 10;
        }
    }
}
