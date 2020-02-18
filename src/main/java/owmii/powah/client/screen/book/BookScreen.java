package owmii.powah.client.screen.book;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.GuiUtils;
import owmii.lib.client.screen.ScreenBase;
import owmii.lib.client.screen.widget.IconButton;
import owmii.powah.Powah;
import owmii.powah.book.PowahBook;
import owmii.powah.book.content.BookEntry;
import owmii.powah.book.content.BookIcon;
import owmii.powah.book.content.page.BookPage;

import javax.annotation.Nullable;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class BookScreen extends ScreenBase {
    public static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/gui/book/background.png");
    public static BookScreen instance = new BookScreen();

    public int x, y, w = 196, h = 230;

    @Nullable
    public BookScreen prevScreen;

    public final BookEntry entry;
    public final BookPage page;
    public int curPage;

    private IconButton[] mainButtons = new IconButton[3];
    private IconButton[] catButtons = new IconButton[PowahBook.getMainEntries().size()];

    public ItemStack hoveredStack = ItemStack.EMPTY;

    public BookScreen(BookEntry entry, int index) {
        super(new StringTextComponent(""));
        this.entry = entry;
        this.page = entry.get(index);
        this.curPage = index;
    }

    protected BookScreen() {
        this(PowahBook.get(0), 0);
    }

    @Override
    protected void init() {
        super.init();
        this.x = (this.width - this.w) / 2;
        this.y = (this.height - this.h) / 2;

        List<BookEntry> mainEntries = PowahBook.getMainEntries();
        for (int i = 0; i < mainEntries.size(); i++) {
            BookEntry entry = mainEntries.get(i);
            final int main = i;
            this.catButtons[i] = new IconButton(this.x - 24, this.y + 20 + (i * 25), this.entry.getMain() == i ? 30 : 24, 24, 220, 0, 0, GUI_TEXTURE, button -> {
                this.mc.displayGuiScreen(new BookScreen(entry.setMain(main), 0));
                refreshButtons();
            }, this).tooltip(I18n.format(entry.getTitle()));
            addButton(this.catButtons[i]);
        }
        this.mainButtons[0] = new IconButton(this.x + this.w + 2, this.y + 32 + 18, 10, 13, 196, 96, 0, GUI_TEXTURE, button -> {
            if (this.prevScreen != null) {
                this.mc.displayGuiScreen(this.prevScreen);
            }
            refreshButtons();
        }, this);
        addButton(this.mainButtons[0]);
        this.mainButtons[1] = new IconButton(this.x + this.w + 2, this.y + this.h - 32, 12, 23, 208, 58, 0, GUI_TEXTURE, button -> {
            this.curPage = Math.min(this.entry.size() - 1, this.curPage + 1);
            this.mc.displayGuiScreen(new BookScreen(this.entry, this.curPage).setPrevScreen(this.prevScreen));
            refreshButtons();
        }, this);
        addButton(this.mainButtons[1]);
        this.mainButtons[2] = new IconButton(this.x - 14, this.y + this.h - 32, 12, 23, 196, 58, 0, GUI_TEXTURE, button -> {
            this.curPage = Math.max(0, this.curPage - 1);
            this.mc.displayGuiScreen(new BookScreen(this.entry, this.curPage).setPrevScreen(this.prevScreen));
            refreshButtons();
        }, this);
        addButton(this.mainButtons[2]);
        refreshButtons();

        this.page.init(this);
        instance = this;
    }

    public void refreshButtons() {
        this.mainButtons[0].visible = (!PowahBook.get(0).equals(this.entry) || this.curPage > 0) && this.prevScreen != null;
        int i = this.entry.size();
        this.mainButtons[1].visible = i > 1 && this.curPage < i - 1;
        this.mainButtons[2].visible = i > 1 && this.curPage > 0;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();

        this.mc.textureManager.bindTexture(GUI_TEXTURE);
        blit(this.x, this.y, 0, 0, this.w, this.h);
        RenderSystem.pushMatrix();
        RenderSystem.translated(this.x, this.y, 0.0D);
        this.page.render(this, mouseX, mouseY, partialTicks);

        if (this.entry.size() > 1) {
            String s = "" + (this.curPage + 1) + "/" + this.entry.size();
            this.font.drawString(s, (this.w - this.font.getStringWidth(s)) / 2, this.h - 16, 0x323b37);
        }
        RenderSystem.popMatrix();

        super.render(mouseX, mouseY, partialTicks);

        RenderSystem.pushMatrix();
        RenderSystem.translated(this.x, this.y, 0.0D);
        this.page.postRender(this, mouseX, mouseY, partialTicks);
        RenderSystem.popMatrix();

        List<BookEntry> mainEntries = PowahBook.getMainEntries();
        for (int i = 0; i < mainEntries.size(); i++) {
            BookEntry entry = mainEntries.get(i);

            BookIcon icon = entry.getIcon();
            if (icon.getType().equals(BookIcon.Type.ITEM)) {
                RenderSystem.pushMatrix();
                RenderHelper.setupGui3DDiffuseLighting();
                this.mc.getItemRenderer().renderItemAndEffectIntoGUI(icon.getStack(), this.x - 24 + 5, this.y + 24 + (i * 25));
                RenderHelper.disableStandardItemLighting();
                RenderSystem.popMatrix();
            } else {
                this.mc.textureManager.bindTexture(icon.getLocation());
                AbstractGui.blit(this.x - 24 + 5, this.y + 24 + (i * 25), 0, 0, 16, 16, 16, 16);
            }

        }

        for (Widget widget : this.buttons) {
            if (widget.isHovered()) {
                widget.renderToolTip(mouseX, mouseY + 20);
                break;
            }
        }

        if (!this.hoveredStack.isEmpty()) {
            FontRenderer font = this.hoveredStack.getItem().getFontRenderer(this.hoveredStack);
            GuiUtils.preItemToolTip(this.hoveredStack);
            List<String> tooltip = this.getTooltipFromItem(this.hoveredStack);

            tooltip.add("");
            tooltip.add(TextFormatting.YELLOW + "Click " + TextFormatting.GRAY + "or press" + TextFormatting.YELLOW + " R " + TextFormatting.GRAY + "for recipes.");
            tooltip.add(TextFormatting.YELLOW + "Shift + Click " + TextFormatting.GRAY + "or press" + TextFormatting.YELLOW + " U " + TextFormatting.GRAY + "for usages.");

            renderTooltip(tooltip, mouseX, mouseY, (font == null ? this.font : font));
            GuiUtils.postItemToolTip();
        }
    }

    public static void open() {
        Minecraft.getInstance().displayGuiScreen(instance);
    }

    public void open(BookEntry entry, int i) {
        this.mc.displayGuiScreen(
                new BookScreen(entry, i).setPrevScreen(this));
    }

    @Nullable
    public BookScreen getPrevScreen() {
        return this.prevScreen;
    }

    public BookScreen setPrevScreen(@Nullable BookScreen prevGui) {
        this.prevScreen = prevGui;
        return this;
    }

    @Override
    public boolean keyPressed(int i, int i1, int i2) {
//        if (!this.hoveredStack.isEmpty() && ModList.get().isLoaded("jei")) {
//            if (i == KeyEvent.VK_U) {
//                JEI.showUsage(this.hoveredStack);
//            } else if (i == KeyEvent.VK_R) {
//                JEI.showRecipes(this.hoveredStack);
//            }
//            return true;
//        }

        return super.keyPressed(i, i1, i2);
    }

    @Override
    public boolean mouseClicked(double x, double y, int code) {
//        if (!this.hoveredStack.isEmpty() && code == 0 && ModList.get().isLoaded("jei")) {
//            if (hasShiftDown()) {
//                JEI.showUsage(this.hoveredStack);
//            } else {
//                JEI.showRecipes(this.hoveredStack);
//            }
//            return true;
//        }
        return super.mouseClicked(x, y, code);
    }

    @Override
    public boolean mouseScrolled(double x, double y, double code) {
        if (code < 0) {
            int prev = this.curPage;
            this.curPage = Math.min(this.entry.size() - 1, this.curPage + 1);
            if (prev != this.curPage) {
                this.mc.displayGuiScreen(new BookScreen(this.entry, this.curPage).setPrevScreen(this.prevScreen));
            }
        } else {
            int prev = this.curPage;
            this.curPage = Math.max(0, this.curPage - 1);
            if (prev != this.curPage) {
                this.mc.displayGuiScreen(new BookScreen(this.entry, this.curPage).setPrevScreen(this.prevScreen));
            }
        }
        return true;
    }
}
