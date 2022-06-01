package owmii.lib.client.wiki.page.panel;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;
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
            MC.get().setScreen(new ConfirmLinkScreen((b) -> {
                if (b) {
                    Util.getPlatform().openUri("https://twitter.com/_owmii");
                }
                MC.get().setScreen(screen);
            }, "https://twitter.com/_owmii", true));
        }, screen).setTooltip(tooltip -> tooltip.add(new TextComponent("Follow me on Twitter!"))));
        this.twitter = screen.addButton2(new IconButton(x + 24, y - 22 + screen.h, Texture.WIKI_PATREON, button -> {
            MC.get().setScreen(new ConfirmLinkScreen((b) -> {
                if (b) {
                    Util.getPlatform().openUri("https://www.patreon.com/owmii");
                }
                MC.get().setScreen(screen);
            }, "https://www.patreon.com/owmii", true));
        }, screen).setTooltip(tooltip -> tooltip.add(new TextComponent("Support me on Patreon! <3"))));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(PoseStack matrix, int x, int y, int mx, int my, float pt, Font font, WikiScreen screen) {
        var globalStack = RenderSystem.getModelViewStack();
        globalStack.pushPose();
        globalStack.translate(x, y, 0.0F);
        globalStack.scale(2.0F, 2.0F, 1.0F);
        String s = getWiki().getModName();
        font.draw(matrix, s, (161 / 4.0F) - font.width(s) / 2.0F, 10, 0x444444);
        globalStack.popPose();

        globalStack.pushPose();
        String s2 = "v" + getWiki().getModVersion();
        font.draw(matrix, s2, x + 161 / 2.0F - font.width(s2) / 2.0F, y + 45, 0x999999);

        if (screen.mc.player != null) {
            String s3 = I18n.get("wiki.lollipop.welcome_back", ":)");
            font.draw(matrix, s3, x + 161 / 2.0F - font.width(s3) / 2.0F, y + 100, 0x777777);
            String s4 = screen.mc.player.getName().getString();
            font.draw(matrix, s4, x + 161 / 2.0F - font.width(s4) / 2.0F, y + 115, 0x777777);
        }
        globalStack.popPose();
    }
}
