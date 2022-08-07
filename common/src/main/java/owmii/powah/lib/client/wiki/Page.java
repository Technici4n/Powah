package owmii.powah.lib.client.wiki;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import owmii.powah.lib.client.screen.Texture;
import owmii.powah.lib.client.screen.widget.IconButton;
import owmii.powah.lib.client.screen.wiki.WikiScreen;
import owmii.powah.lib.client.util.MC;
import owmii.powah.lib.client.wiki.page.panel.Panel;

import javax.annotation.Nullable;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import java.util.Objects;

public class Page {
    private final String name;
    private final Section parent;

    @Nullable
    private Page next, prev;

    protected IconButton navNext = IconButton.EMPTY;
    protected IconButton navPrev = IconButton.EMPTY;

    public Page(String name, Section parent) {
        this.name = name;
        this.parent = parent;
    }

    public void init(int x, int y, WikiScreen screen) {
        if (!isPanel()) {
            List<Entry> categories = getWiki().getCategories();
            for (int i = 0; i < categories.size(); i++) {
                Entry catEntry = categories.get(i).getParent();
                if (catEntry != null) {
                    Entry currEntry = screen.getEntry().getParent();
                    if (currEntry != null) {
                        boolean b = currEntry.equals(catEntry);
                        screen.addButton2(new IconButton(x + (b ? 0 : 2), 10 + y + (i * 28), catEntry.getStack(), Texture.WIKI_TABS.get(b), button -> {
                            MC.open(new WikiScreen(catEntry.getSections(0)));
                        }, screen).xOffset(b ? -2.0F : 0.5F).setTooltip(tooltip -> tooltip.add(Component.translatable(catEntry.getTransKey()))));
                    }
                }
            }
        }
        this.navNext = screen.addButton2(new IconButton(x + (isPanel() ? 140 : 224), y - 7 + screen.h, Texture.WIKI_NEXT, button -> {
            if (this.next != null) {
                if (isPanel()) {
                    screen.setPanel((Panel) this.next);
                } else {
                    screen.setPage(this.next);
                }
                MC.open(screen);
            }
        }, screen));
        this.navPrev = screen.addButton2(new IconButton(x + 7, y - 7 + screen.h, Texture.WIKI_PREV, button -> {
            if (this.prev != null) {
                if (isPanel()) {
                    screen.setPanel((Panel) this.prev);
                } else {
                    screen.setPage(this.prev);
                }
                MC.open(screen);
            }
        }, screen));
        refresh();
    }

    public void refresh() {
        this.navNext.visible = this.next != null;
        this.navPrev.visible = this.prev != null;
    }

    public void render(PoseStack matrix, int x, int y, int mx, int my, float pt, Font font, WikiScreen screen) {
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double i) {
        return false;
    }

    public void onClose() {
    }

    public boolean hasNext() {
        return this.next != null;
    }

    public boolean hasPrev() {
        return this.prev != null;
    }

    @Nullable
    public Page next() {
        return this.next;
    }

    @Nullable
    public Page prev() {
        return this.prev;
    }

    public Page next(@Nullable Page next) {
        this.next = next;
        if (next != null) {
            next.prev = this;
        }
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Wiki getWiki() {
        return getSection().getEntry().getWiki();
    }

    public Section getSection() {
        return Objects.requireNonNull(this.parent);
    }

    public boolean isPanel() {
        return this instanceof Panel;
    }
}
