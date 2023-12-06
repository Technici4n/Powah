package owmii.powah.lib.client.wiki.page;

import java.util.function.Consumer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;
import owmii.powah.lib.client.screen.Texture;
import owmii.powah.lib.client.wiki.Entry;
import owmii.powah.lib.client.wiki.Icon;
import owmii.powah.lib.client.wiki.Page;
import owmii.powah.lib.client.wiki.Section;

public class CatPage extends Page {
    public CatPage(String name, Section parent) {
        super(name, parent);
    }

    public CatPage e(ItemLike provider, Consumer<Entry> consumer) {
        return e("", provider, consumer);
    }

    public CatPage e(String name, ItemLike icon, Consumer<Entry> consumer) {
        return e(name, new Icon(icon), consumer);
    }

    public CatPage e(String name, Texture icon, Consumer<Entry> consumer) {
        return e(name, new Icon(icon), consumer);
    }

    public CatPage e(String name, Consumer<Entry> consumer) {
        return e(name, (Icon) null, consumer);
    }

    public CatPage e(String name, @Nullable Icon icon, Consumer<Entry> consumer) {
        Entry entry = new Entry(name, icon, getWiki());
        consumer.accept(entry);
        getWiki().register(entry);
        return this;
    }
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void init(int x, int y, WikiScreen screen) {
//        for (int i = 0; i < this.entries.size(); i++) {
//            Entry e = this.entries.get(i);
//            screen.addButton2(new IconButton(x, 10 + y + (i * 28), Texture.WIKI_TAB, button -> {
//                MC.open(new WikiScreen(e.getSections(0)));
//            }, screen).setTooltip(tooltip -> tooltip.add(new TranslationTextComponent(e.getTransKey()))));
//        }
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void render(MatrixStack matrix, int x, int y, int mx, int my, float pt, FontRenderer font, WikiScreen screen) {
//
//
//        ITextComponent t = new TranslationTextComponent(getSection().getEntry().getTransKey()).copyRaw().withStyle(TextFormatting.DARK_AQUA).modifyStyle(style -> {
//            style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemHover(new ItemStack(Blocks.COBBLESTONE))));
//            return style;
//        }).append(new TranslationTextComponent(" Test").withStyle(TextFormatting.GRAY));
//        Minecraft.getInstance().fontRenderer.func_243248_b(matrix, t, x + 10, y + 10, 0x444444);
//    }
}
