package owmii.powah.lib.client.screen.wiki;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import owmii.powah.lib.client.screen.ScreenBase;
import owmii.powah.lib.client.screen.Texture;
import owmii.powah.lib.client.util.MC;
import owmii.powah.lib.client.util.Text;
import owmii.powah.lib.client.wiki.Entry;
import owmii.powah.lib.client.wiki.Page;
import owmii.powah.lib.client.wiki.Section;
import owmii.powah.lib.client.wiki.page.panel.Panel;

public class WikiScreen extends ScreenBase {
    private final Entry entry;
    private Page page;
    private Panel panel;
    public ItemStack hoveredStack = ItemStack.EMPTY;

    @Nullable
    public static WikiScreen last;

    public WikiScreen(Section section) {
        super(Text.EMPTY);
        this.w = Texture.WIKI_BG_0.getWidth() + Texture.WIKI_BG_1.getWidth() + 28;
        this.h = Texture.WIKI_BG_0.getHeight();
        this.entry = section.getEntry();
        this.page = section.getPage();
        this.panel = (Panel) section.getPanel();
    }

    @Override
    protected void init() {
        super.init();
        this.page.init(this.x, this.y, this);
        this.panel.init(this.x + 246, this.y, this);
        last = this;
    }

    @Override
    public void tick() {
        super.tick();
        this.page.refresh();
        this.panel.refresh();
    }

    @Override
    public void render(GuiGraphics gui, int mx, int my, float pt) {
        renderBackground(gui, mx, my, pt);
        this.hoveredStack = ItemStack.EMPTY;
        Texture.WIKI_BG_0.draw(gui, this.x + 28, this.y);
        Texture.WIKI_BG_1.draw(gui, this.x + 28 + Texture.WIKI_BG_0.getWidth(), this.y);
        this.page.render(gui, this.x + 31, this.y + 3, mx, my, pt, this.font, this);
        this.panel.render(gui, this.x + 246, this.y + 3, mx, my, pt, this.font, this);
        super.render(gui, mx, my, pt);
        if (!this.hoveredStack.isEmpty()) {
            gui.renderTooltip(font, this.hoveredStack, mx, my);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
        if (isMouseOver(this.x + 31, this.y + 3, 212, 224, mouseX, mouseY)) {
            if (this.page.mouseScrolled(mouseX, mouseY, deltaX, deltaY)) {
                return true;
            }
        } else if (isMouseOver(this.x + 246, this.y + 3, 161, 224, mouseX, mouseY)) {
            if (this.panel.mouseScrolled(mouseX, mouseY, deltaX, deltaY)) {
                return true;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, deltaX, deltaY);
    }

    @Override
    public void onClose() {
        this.page.onClose();
        this.panel.onClose();
        super.onClose();
    }

    public static void open(Entry entry) {
        if (last != null && false) {
            MC.open(last);
        } else
            MC.open(new WikiScreen(entry.getSections().get(0)));
    }

    public Page getPage() {
        return this.page;
    }

    public WikiScreen setPage(Page page) {
        this.page = page;
        return this;
    }

    public Page getPanel() {
        return this.panel;
    }

    public WikiScreen setPanel(Panel panel) {
        this.panel = panel;
        return this;
    }

    @Nullable
    public static WikiScreen getLast() {
        return last;
    }

    public Entry getEntry() {
        return this.entry;
    }
}
