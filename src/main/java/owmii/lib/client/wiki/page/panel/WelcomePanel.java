package owmii.lib.client.wiki.page.panel;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.ConfirmOpenLinkScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import owmii.lib.client.screen.Texture;
import owmii.lib.client.screen.widget.IconButton;
import owmii.lib.client.screen.wiki.WikiScreen;
import owmii.lib.client.util.MC;
import owmii.lib.client.wiki.Section;

public class WelcomePanel extends Panel {
    private IconButton twitter = IconButton.EMPTY;
    private IconButton patreon = IconButton.EMPTY;

    public WelcomePanel(Section parent) {
        super(parent);
    }

    public WelcomePanel(String name, Section parent) {
        super(name, parent);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void init(int x, int y, WikiScreen screen) {
        super.init(x, y, screen);
        this.twitter = screen.addButton2(new IconButton(x + 8, y - 22 + screen.h, Texture.WIKI_TWITTER, button -> {
            MC.get().displayGuiScreen(new ConfirmOpenLinkScreen((b) -> {
                if (b) {
                    Util.getOSType().openURI("https://twitter.com/_owmii");
                }
                MC.get().displayGuiScreen(screen);
            }, "https://twitter.com/_owmii", true));
        }, screen).setTooltip(tooltip -> tooltip.add(new StringTextComponent("Follow me on Twitter!"))));
        this.twitter = screen.addButton2(new IconButton(x + 24, y - 22 + screen.h, Texture.WIKI_PATREON, button -> {
            MC.get().displayGuiScreen(new ConfirmOpenLinkScreen((b) -> {
                if (b) {
                    Util.getOSType().openURI("https://www.patreon.com/owmii");
                }
                MC.get().displayGuiScreen(screen);
            }, "https://www.patreon.com/owmii", true));
        }, screen).setTooltip(tooltip -> tooltip.add(new StringTextComponent("Support me on Patreon! <3"))));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(MatrixStack matrix, int x, int y, int mx, int my, float pt, FontRenderer font, WikiScreen screen) {
        RenderSystem.pushMatrix();
        RenderSystem.translatef(x, y, 0.0F);
        RenderSystem.scalef(2.0F, 2.0F, 1.0F);
        String s = getWiki().getModName();
        font.drawString(matrix, s, (161 / 4.0F) - font.getStringWidth(s) / 2.0F, 10, 0x444444);
        RenderSystem.popMatrix();

        RenderSystem.pushMatrix();
        String s2 = "v" + getWiki().getModVersion();
        font.drawString(matrix, s2, x + 161 / 2.0F - font.getStringWidth(s2) / 2.0F, y + 45, 0x999999);

        if (screen.mc.player != null) {
            String s3 = I18n.format("wiki.lollipop.welcome_back", ":)");
            font.drawString(matrix, s3, x + 161 / 2.0F - font.getStringWidth(s3) / 2.0F, y + 100, 0x777777);
            String s4 = screen.mc.player.getName().getString();
            font.drawString(matrix, s4, x + 161 / 2.0F - font.getStringWidth(s4) / 2.0F, y + 115, 0x777777);
        }
        RenderSystem.popMatrix();
    }
}
