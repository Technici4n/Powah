package owmii.powah.client.screen.book;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.GuiUtils;
import owmii.lib.client.screen.ScreenBase;
import owmii.lib.client.screen.widget.IconButton;
import owmii.powah.book.PowahBook;
import owmii.powah.book.content.BookEntry;
import owmii.powah.book.content.BookIcon;
import owmii.powah.book.content.page.BookPage;
import owmii.powah.client.screen.Textures;

import javax.annotation.Nullable;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class BookScreen extends ScreenBase {
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
            this.catButtons[i] = addButton(new IconButton(this.x - 24, this.y + 20 + (i * 25), Textures.BOOK_CAT, button -> {
                this.mc.displayGuiScreen(new BookScreen(entry.setMain(main), 0));
            }, this).setTooltip(tooltip -> tooltip.add(new TranslationTextComponent(entry.getTitle()))));
        }
        this.mainButtons[0] = addButton(new IconButton(this.x + this.w + 2, this.y + 32 + 18, Textures.BOOK_BACK, button -> {
            if (this.prevScreen != null) {
                this.mc.displayGuiScreen(this.prevScreen);
            }
        }, this));
        this.mainButtons[1] = addButton(new IconButton(this.x + this.w + 2, this.y + this.h - 32, Textures.BOOK_NAV_R, button -> {
            this.curPage = Math.min(this.entry.size() - 1, this.curPage + 1);
            this.mc.displayGuiScreen(new BookScreen(this.entry, this.curPage).setPrevScreen(this.prevScreen));
        }, this));
        this.mainButtons[2] = addButton(new IconButton(this.x - 14, this.y + this.h - 32, Textures.BOOK_NAV_L, button -> {
            this.curPage = Math.max(0, this.curPage - 1);
            this.mc.displayGuiScreen(new BookScreen(this.entry, this.curPage).setPrevScreen(this.prevScreen));
        }, this));

        this.page.init(this);
        instance = this;
    }

    @Override
    public void render(MatrixStack matrix, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        renderBackground(matrix);
        Textures.BOOK.draw(matrix, this.x, this.y);


        RenderSystem.pushMatrix();
        RenderSystem.translated(this.x, this.y, 0.0D);
        this.page.render(matrix, this, p_230430_2_, p_230430_3_, p_230430_4_);

        if (this.entry.size() > 1) {
            String s = "" + (this.curPage + 1) + "/" + this.entry.size();
            this.font.drawString(matrix, s, (this.w - this.font.getStringWidth(s)) / 2, this.h - 16, 0x323b37);
        }
        RenderSystem.popMatrix();

        super.render(matrix, p_230430_2_, p_230430_3_, p_230430_4_);

        RenderSystem.pushMatrix();
        RenderSystem.translated(this.x, this.y, 0.0D);
        this.page.postRender(matrix, this, p_230430_2_, p_230430_3_, p_230430_4_);
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
                AbstractGui.blit(matrix, this.x - 24 + 5, this.y + 24 + (i * 25), 0, 0, 16, 16, 16, 16);
            }
        }

        for (Widget widget : this.buttons) {
            if (widget.isHovered()) {
                widget.renderToolTip(matrix, p_230430_2_, p_230430_3_);
                return;
            }
        }

        if (!this.hoveredStack.isEmpty()) {
            FontRenderer font = this.hoveredStack.getItem().getFontRenderer(this.hoveredStack);
            GuiUtils.preItemToolTip(this.hoveredStack);
            List<ITextComponent> tooltip = getTooltipFromItem(this.hoveredStack);
//
//            tooltip.add("");
//            tooltip.add(TextFormatting.YELLOW + "Click " + TextFormatting.GRAY + "or press" + TextFormatting.YELLOW + " R " + TextFormatting.GRAY + "for recipes.");
//            tooltip.add(TextFormatting.YELLOW + "Shift + Click " + TextFormatting.GRAY + "or press" + TextFormatting.YELLOW + " U " + TextFormatting.GRAY + "for usages.");

            renderToolTip(matrix, Lists.transform(tooltip, ITextComponent::func_241878_f), p_230430_2_, p_230430_3_, (font == null ? this.font : font));
            GuiUtils.postItemToolTip();
        }


    }

    public static void open() {
        Minecraft.getInstance().displayGuiScreen(instance);
    }

    @Nullable
    public BookScreen getPrevScreen() {
        return this.prevScreen;
    }

    public BookScreen setPrevScreen(@Nullable BookScreen prevGui) {
        this.prevScreen = prevGui;
        return this;
    }
}
