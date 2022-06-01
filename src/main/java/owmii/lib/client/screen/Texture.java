package owmii.lib.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import owmii.lib.Lollipop;
import owmii.lib.logistics.Redstone;
import owmii.lib.logistics.Transfer;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.HashMap;
import java.util.Map;

public class Texture extends GuiComponent {
    private static final Builder BUILDER = new Builder(Lollipop.MOD_ID);
    public static final Texture EMPTY = BUILDER.make("empty", 0, 0, 0, 0);

    // Wiki
    public static final Texture WIKI_BG_0 = BUILDER.make("wiki/bg_0", 256, 230, 0, 0);
    public static final Texture WIKI_BG_1 = BUILDER.make("wiki/bg_1", 126, 230, 0, 0);
    public static final Texture WIKI_TAB_ON = BUILDER.make("wiki/bg_1", 31, 26, 168, 0);
    public static final Texture WIKI_TAB_OFF = BUILDER.make("wiki/bg_1", 26, 26, 168, 26);
    public static final Map<Boolean, Texture> WIKI_TABS = new HashMap<>();
    public static final Texture WIKI_FRM = BUILDER.make("wiki/bg_1", 24, 24, 126, 42);
    public static final Texture WIKI_BIG_FRM = BUILDER.make("wiki/bg_1", 42, 42, 126, 0);
    public static final Texture WIKI_RCP_FRM = BUILDER.make("wiki/bg_1", 32, 32, 199, 0);
    public static final Texture WIKI_NEXT = BUILDER.make("wiki/bg_1", 15, 10, 150, 42);
    public static final Texture WIKI_PREV = BUILDER.make("wiki/bg_1", 15, 10, 150, 52);
    public static final Texture WIKI_ITM_NEXT = BUILDER.make("wiki/bg_1", 10, 18, 136, 66);
    public static final Texture WIKI_ITM_PREV = BUILDER.make("wiki/bg_1", 10, 18, 126, 66);
    public static final Texture WIKI_INF_LN_BG = BUILDER.make("wiki/bg_1", 143, 19, 0, 230);
    public static final Texture WIKI_TWITTER = BUILDER.make("wiki/social/twitter", 12, 12);
    public static final Texture WIKI_PATREON = BUILDER.make("wiki/social/patreon", 12, 12);

    // Misc
    public static final Texture SLOT_HIGHLIGHT_BG = BUILDER.make("container/misc", 16, 16, 0, 0);

    // Side config
    public static final Map<Transfer, Texture> CONFIG = new HashMap<>();
    public static final Texture CONFIG_BTN_BG = BUILDER.make("container/button_ov", 23, 25, 0, 0);
    public static final Texture CONFIG_BTN = BUILDER.make("container/button_ov", 5, 5, 23, 16);
    public static final Texture CONFIG_BTN_ALL = BUILDER.make("container/button_ov", 5, 5, 28, 16);
    public static final Texture CONFIG_BTN_OUT = BUILDER.make("container/button_ov", 5, 5, 33, 16);
    public static final Texture CONFIG_BTN_IN = BUILDER.make("container/button_ov", 5, 5, 38, 16);
    public static final Texture CONFIG_BTN_OFF = BUILDER.make("container/button_ov", 5, 5, 43, 16);

    // Redstone mode
    public static final Map<Redstone, Texture> REDSTONE = new HashMap<>();
    public static final Texture REDSTONE_BTN_BG = BUILDER.make("container/button_ov", 15, 16, 23, 0);
    public static final Texture REDSTONE_BTN_IGNORE = BUILDER.make("container/button_ov", 9, 8, 38, 0);
    public static final Texture REDSTONE_BTN_OFF = BUILDER.make("container/button_ov", 9, 8, 47, 0);
    public static final Texture REDSTONE_BTN_ON = BUILDER.make("container/button_ov", 9, 8, 38, 8);

    private final ResourceLocation location;
    private final int width, height;
    private final int u, v;
    private final int tw, th;

    public Texture(ResourceLocation location, int width, int height) {
        this(location, width, height, 0, 0, width, height);
    }

    public Texture(ResourceLocation location, int width, int height, int u, int v) {
        this(location, width, height, u, v, 256, 256);
    }

    public Texture(ResourceLocation location, int width, int height, int u, int v, int dim) {
        this(location, width, height, u, v, dim, dim);
    }

    public Texture(ResourceLocation location, int width, int height, int u, int v, int tw, int th) {
        this.location = location;
        this.width = width;
        this.height = height;
        this.u = u;
        this.v = v;
        this.tw = tw;
        this.th = th;
    }

    public void drawScalableW(PoseStack matrix, float size, int x, int y) {
        scaleW((int) (size * this.width)).draw(matrix, x, y);
    }

    public void drawScalableH(PoseStack matrix, float size, int x, int y) {
        int i = (int) (size * this.height);
        scaleH(i).moveV(this.height - i).draw(matrix, x, y + this.height - i);
    }

    public void draw(PoseStack matrix, int x, int y) {
        if (!isEmpty()) {
            bindTexture(getLocation());
            blit(matrix, x, y, getU(), getV(), getWidth(), getHeight(), this.tw, this.th);
        }
    }

    public void bindTexture(ResourceLocation guiTexture) {
        RenderSystem.setShaderTexture(0, guiTexture);
    }

    public Texture addW(int width) {
        return scaleW(this.width + width);
    }

    public Texture addH(int height) {
        return scaleH(this.height + height);
    }

    public Texture remW(int width) {
        return scaleW(this.width - width);
    }

    public Texture remH(int height) {
        return scaleH(this.height - height);
    }

    public Texture scaleW(int width) {
        return scale(width, this.height);
    }

    public Texture scaleH(int height) {
        return scale(this.width, height);
    }

    public Texture scale(int width, int height) {
        return new Texture(this.location, width, height, this.u, this.v);
    }

    public Texture moveU(int u) {
        return move(u, 0);
    }

    public Texture moveV(int v) {
        return move(0, v);
    }

    public Texture move(int u, int v) {
        return new Texture(this.location, this.width, this.height, this.u + u, this.v + v);
    }

    public ResourceLocation getLocation() {
        return this.location;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getU(int i) {
        return this.u + i;
    }

    public int getV(int i) {
        return this.v + i;
    }

    public int getU() {
        return this.u;
    }

    public int getV() {
        return this.v;
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    public boolean isMouseOver(int x, int y, double mouseX, double mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + this.width && mouseY < y + this.height;
    }

    static {
        CONFIG.put(Transfer.ALL, CONFIG_BTN_ALL);
        CONFIG.put(Transfer.EXTRACT, CONFIG_BTN_OUT);
        CONFIG.put(Transfer.RECEIVE, CONFIG_BTN_IN);
        CONFIG.put(Transfer.NONE, CONFIG_BTN_OFF);
        REDSTONE.put(Redstone.IGNORE, REDSTONE_BTN_IGNORE);
        REDSTONE.put(Redstone.ON, REDSTONE_BTN_ON);
        REDSTONE.put(Redstone.OFF, REDSTONE_BTN_OFF);
        WIKI_TABS.put(true, WIKI_TAB_ON);
        WIKI_TABS.put(false, WIKI_TAB_OFF);
    }

    public static class Builder {
        private final String id;

        public Builder(String id) {
            this.id = id;
        }

        public Texture make(String path, int width, int height) {
            return new Texture(new ResourceLocation(this.id, "textures/gui/" + path + ".png"), width, height);
        }

        public Texture make(String path, int width, int height, int u, int v, int w, int h) {
            return new Texture(new ResourceLocation(this.id, "textures/gui/" + path + ".png"), width, height, u, v, w, h);
        }

        public Texture make(String path, int width, int height, int u, int v, int d) {
            return new Texture(new ResourceLocation(this.id, "textures/gui/" + path + ".png"), width, height, u, v, d);
        }

        public Texture make(String path, int width, int height, int u, int v) {
            return new Texture(new ResourceLocation(this.id, "textures/gui/" + path + ".png"), width, height, u, v);
        }
    }
}
