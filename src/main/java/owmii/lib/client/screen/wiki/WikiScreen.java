package owmii.lib.client.screen.wiki;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.lib.client.screen.ScreenBase;
import owmii.lib.client.screen.Texture;
import owmii.lib.client.util.MC;
import owmii.lib.client.util.Text;
import owmii.lib.client.wiki.Entry;
import owmii.lib.client.wiki.Page;
import owmii.lib.client.wiki.Section;
import owmii.lib.client.wiki.page.panel.Panel;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
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
    public void render(PoseStack matrix, int mx, int my, float pt) {
        renderBackground(matrix);
        this.hoveredStack = ItemStack.EMPTY;
        Texture.WIKI_BG_0.draw(matrix, this.x + 28, this.y);
        Texture.WIKI_BG_1.draw(matrix, this.x + 28 + Texture.WIKI_BG_0.getWidth(), this.y);
        this.page.render(matrix, this.x + 31, this.y + 3, mx, my, pt, this.font, this);
        this.panel.render(matrix, this.x + 246, this.y + 3, mx, my, pt, this.font, this);
        super.render(matrix, mx, my, pt);
        if (!this.hoveredStack.isEmpty()) {
            renderTooltip(matrix, this.hoveredStack, mx, my);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double i) {
        if (isMouseOver(this.x + 31, this.y + 3, 212, 224, mouseX, mouseY)) {
            if (this.page.mouseScrolled(mouseX, mouseY, i)) {
                return true;
            }
        } else if (isMouseOver(this.x + 246, this.y + 3, 161, 224, mouseX, mouseY)) {
            if (this.panel.mouseScrolled(mouseX, mouseY, i)) {
                return true;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, i);
    }

    @Override
    public void onClose() {
        this.page.onClose();
        this.panel.onClose();
        super.onClose();
    }

    public static void open(Entry entry) {
        if (last != null) {
            MC.open(last);
        } else MC.open(new WikiScreen(entry.getSections().get(0)));
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
