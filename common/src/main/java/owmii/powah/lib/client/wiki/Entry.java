package owmii.powah.lib.client.wiki;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import owmii.powah.Powah;
import owmii.powah.lib.registry.VarReg;

public class Entry {
    private final List<Section> sections = new ArrayList<>();
    private final String name;
    private final Wiki wiki;
    private boolean isMain;
    private boolean stackName;

    @Nullable
    private final Icon icon;

    @Nullable
    private Entry parent;

    public Entry(String name, Wiki wiki) {
        this(name, null, wiki);
    }

    public Entry(String name, @Nullable Icon icon, Wiki wiki) {
        this.wiki = wiki;
        if (icon == null) {
            List<Item> items = VarReg.getSiblingIds(name).stream().map(s -> BuiltInRegistries.ITEM.get(Powah.id(s))).toList();
            this.icon = items.isEmpty() ? null : new Icon(items.get(0));
            this.name = name;
        } else {
            this.icon = icon;
            ItemStack stack = icon.getStack();
            if (name.isEmpty() && !stack.isEmpty()) {
                this.name = stack.getDescriptionId();
                this.stackName = true;
            } else
                this.name = name;
        }
    }

    public Entry s(Consumer<Section> consumer) {
        Section section = new Section(this);
        consumer.accept(section);
        this.sections.add(section);
        return this;
    }

    public Wiki getWiki() {
        return this.wiki;
    }

    @Nullable
    public Icon getIcon() {
        return this.icon;
    }

    public ItemStack getStack() {
        if (this.icon != null) {
            return this.icon.getStack();
        }
        return ItemStack.EMPTY;
    }

    public Section getSections(int index) {
        return this.sections.get(index);
    }

    public List<Section> getSections() {
        return this.sections;
    }

    public String getName() {
        return this.name;
    }

    public boolean isMain() {
        return this.isMain;
    }

    public Entry setMain(boolean main) {
        this.isMain = main;
        return this;
    }

    @Nullable
    public Entry getParent() {
        return this.parent;
    }

    public Entry setParent(@Nullable Entry parent) {
        this.parent = parent;
        return this;
    }

    public String getTransKey() {
        return this.stackName ? this.name : "wiki." + getWiki().getModId() + "." + this.name;
    }
}
