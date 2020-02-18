package owmii.powah.book.content.page;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.MavenVersionStringHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;
import owmii.lib.client.screen.widget.IconButton;
import owmii.lib.client.util.GUI;
import owmii.powah.Powah;
import owmii.powah.book.content.BookEntry;
import owmii.powah.book.content.BookIcon;
import owmii.powah.client.screen.book.BookScreen;

import java.util.*;

public class BookPage {
    private ItemStack stack = ItemStack.EMPTY;
    private Map<String, Object[]> infoBox = new HashMap<>();
    private List<BookImage> images = new ArrayList<>();
    private List<Paragraph> paragraphs = new ArrayList<>();
    private List<BookEntry> entries = new ArrayList<>();
    private boolean isHome;

    public static BookPage create() {
        return new BookPage();
    }

    public BookPage item(IItemProvider item) {
        return item(new ItemStack(item));
    }

    public BookPage item(ItemStack stack) {
        this.stack = stack;
        return this;
    }

    public BookPage text() {
        if (!this.stack.isEmpty()) {
            ResourceLocation rl = this.stack.getItem().getRegistryName();
            Objects.requireNonNull(rl);
            this.paragraphs.add(Paragraph.text(rl.getPath()));
        }
        return this;
    }

    public BookPage info() {
        if (!this.stack.isEmpty()) {
            if (this.stack.getItem() instanceof IBookInfo) {
                this.infoBox = ((IBookInfo) this.stack.getItem()).getBookInfo(this.stack, new HashMap<>());
            }
        }
        return this;
    }

    public BookPage title(String title) {
        this.paragraphs.add(Paragraph.title(title));
        return this;
    }

    public BookPage text(String text, Object... args) {
        this.paragraphs.add(Paragraph.text(text, args));
        return this;
    }

    public BookPage paragraph(String key, Object... args) {
        this.paragraphs.add(Paragraph.create(key, key, args));
        return this;
    }

    public BookPage image(String path, int width, int height) {
        this.images.add(BookImage.create(path, width, height));
        return this;
    }

    public BookPage add(BookEntry entry) {
        this.entries.add(entry);
        return this;
    }

    @OnlyIn(Dist.CLIENT)
    public void init(BookScreen screen) {
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 6; ++j) {
                int index = j + i * 6;
                if (index < this.entries.size()) {
                    BookEntry entry = this.entries.get(index);
                    ItemStack stack = entry.getIcon().getStack();
                    screen.addButton(new IconButton(screen.x + 13 + j * 29, screen.y + 13 + i * 29, 24, 24, 196, 0, 0, BookScreen.GUI_TEXTURE, b -> {
                        screen.mc.displayGuiScreen(new BookScreen(entry.setMain(screen.entry.getMain()), 0).setPrevScreen(screen));
                    }, screen).tooltip(I18n.format(entry.getTitle())));
                } else break;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void render(BookScreen screen, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        FontRenderer fr = mc.fontRenderer;

        if (this.isHome) {
            ModList.get().getModContainerById("powah").ifPresent(o -> {
                IModInfo info = o.getModInfo();

                GlStateManager.pushMatrix();
                GlStateManager.translatef(-(screen.w / 2.0F), -50.0F, 0.0F);
                GlStateManager.scalef(2.0F, 2.0F, 1.0F);
                String s = info.getDisplayName() + "!";
                fr.drawString(s, screen.w / 2.0F - fr.getStringWidth(s) / 2.0F, 50, 0x444444);
                GlStateManager.popMatrix();


                GlStateManager.pushMatrix();
                String s2 = "v" + MavenVersionStringHelper.artifactVersionToString(info.getVersion());
                fr.drawString(s2, screen.w / 2.0F - fr.getStringWidth(s2) / 2.0F, 72, 0x777777);

//                VersionChecker.CheckResult result = VersionChecker.getResult(info);
//                if (result.target != null && result.url != null) {
//                    String s3 = "New version: " + result.target;
//                    fr.drawString(s3, screen.w / 2.0F - fr.getStringWidth(s3) / 2.0F, 88, 0xc20020);
//                    String url = result.url;
//                }

                if (screen.mc.player != null) {
                    String s3 = I18n.format("book.powah.title.welcome.back", screen.mc.player.getName().getString());
                    fr.drawString(s3, screen.w / 2.0F - fr.getStringWidth(s3) / 2.0F, 140, 0x99495d);
                }


                GlStateManager.popMatrix();
            });
            return;
        }

        int i = 0;

        for (BookImage image : this.images) {
            mc.textureManager.bindTexture(image.getLocation());
            int w = image.getWidth();
            int h = image.getHeight();
            AbstractGui.blit(8, 8, 0, 0, w, h, w, h);
            i += h + 2;
        }

        if (!this.stack.isEmpty()) {
            GlStateManager.pushMatrix();

            float scale = 1.5F;
            int x = (int) (screen.w / 2.0F - 12);
            int y = 12;


            GlStateManager.scalef(scale, scale, 1.0F);
            mc.textureManager.bindTexture(new ResourceLocation(Powah.MOD_ID, "textures/gui/book/background.png"));
            screen.blit((int) (x / scale) - 4, y - 4, 196, 0, 24, 24);
            RenderHelper.setupGui3DDiffuseLighting();
            mc.getItemRenderer().renderItemAndEffectIntoGUI(this.stack, (int) (x / scale), y);
            if (GUI.isMouseOver(mouseX, mouseY, screen.x + x, (int) (screen.y + y * 1.5F), 16 * 1.5F, 16 * 1.5F)) {
                screen.hoveredStack = this.stack;
            } else if (screen.hoveredStack.isItemEqual(this.stack)) {
                screen.hoveredStack = ItemStack.EMPTY;
            }

            GlStateManager.popMatrix();
            String s = this.stack.getDisplayName().getString();
            fr.drawString(s, screen.w / 2.0F - fr.getStringWidth(s) / 2.0F, 56, 0x1e5b9c);
            i += 64;

        }

        boolean flag = false;
        for (Map.Entry<String, Object[]> entry : this.infoBox.entrySet()) {
            fr.drawString(I18n.format(entry.getKey(), entry.getValue()), 10, 10 + i, 0x38453c);
            i += 14;
            flag = true;
        }

        if (flag) {
            i += 4;
        }

        for (Paragraph paragraph : this.paragraphs) {
            String title = paragraph.getTitle();
            String text = paragraph.getText();
            Object[] args = paragraph.getArgs();
            if (!title.isEmpty()) {
                fr.drawString(I18n.format(title), 9, 12 + i, 0x1e5b9c);
                i += 15;
            }
            if (!text.isEmpty()) {
                fr.drawSplitString(I18n.format(text, args), 9, 10 + i, screen.w - 16, 0x38453c);
                i += paragraph.height(screen.w - 16);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void postRender(BookScreen screen, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        for (int ii = 0; ii < 8; ++ii) {
            for (int j = 0; j < 6; ++j) {
                int index = j + ii * 6;
                if (index < this.entries.size()) {
                    BookEntry entry = this.entries.get(index);

                    BookIcon icon = entry.getIcon();
                    if (icon.getType().equals(BookIcon.Type.ITEM)) {
                        RenderHelper.setupGui3DDiffuseLighting();
                        mc.getItemRenderer().renderItemAndEffectIntoGUI(icon.getStack(), 12 + j * 29 + 5, 17 + ii * 29);
                        RenderHelper.disableStandardItemLighting();
                    } else {
                        mc.textureManager.bindTexture(icon.getLocation());
                        AbstractGui.blit(12 + j * 29 + 5, 17 + ii * 29, 0, 0, 16, 16, 16, 16);
                    }
                }
            }
        }
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public BookPage setHome() {
        this.isHome = true;
        return this;
    }
}
