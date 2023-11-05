package owmii.powah.lib.client.wiki.page;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;
import owmii.powah.lib.client.screen.Texture;
import owmii.powah.lib.client.wiki.Entry;
import owmii.powah.lib.client.wiki.Icon;
import owmii.powah.lib.client.wiki.Page;
import owmii.powah.lib.client.wiki.Section;

public class EntriesPage extends Page {
    protected final List<Entry> entries = new ArrayList<>();

    public EntriesPage(String name, Section parent) {
        super(name, parent);
    }

    public EntriesPage e(ItemLike provider, Consumer<Entry> consumer) {
        return e("", provider, consumer);
    }

    public EntriesPage e(String name, ItemLike icon, Consumer<Entry> consumer) {
        return e(name, new Icon(icon), consumer);
    }

    public EntriesPage e(String name, Texture icon, Consumer<Entry> consumer) {
        return e(name, new Icon(icon), consumer);
    }

    public EntriesPage e(String name, Consumer<Entry> consumer) {
        return e(name, (Icon) null, consumer);
    }

    public EntriesPage e(String name, @Nullable Icon icon, Consumer<Entry> consumer) {
        Entry entry = new Entry(name, icon, getWiki());
        consumer.accept(entry);
        entry.setParent(getSection().getEntry().getParent());
        this.entries.add(entry);
        return this;
    }
}
