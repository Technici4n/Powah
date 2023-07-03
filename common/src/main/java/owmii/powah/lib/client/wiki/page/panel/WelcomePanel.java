package owmii.powah.lib.client.wiki.page.panel;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import owmii.powah.lib.client.screen.Texture;
import owmii.powah.lib.client.screen.widget.IconButton;
import owmii.powah.lib.client.screen.wiki.WikiScreen;
import owmii.powah.lib.client.util.MC;
import owmii.powah.lib.client.wiki.Section;

import java.util.List;

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
    public void init(int x, int y, WikiScreen screen) {
        super.init(x, y, screen);
        this.twitter = screen.addButton2(new IconButton(x + 8, y - 22 + screen.h, Texture.WIKI_TWITTER, button -> {
            MC.get().setScreen(new ConfirmLinkScreen((b) -> {
                if (b) {
                    Util.getPlatform().openUri("https://twitter.com/_owmii");
                }
                MC.get().setScreen(screen);
            }, "https://twitter.com/_owmii", true));
        }, screen).setTooltipSupplier(() -> List.of(Component.literal("Follow me on Twitter!"))));
        this.twitter = screen.addButton2(new IconButton(x + 24, y - 22 + screen.h, Texture.WIKI_PATREON, button -> {
            MC.get().setScreen(new ConfirmLinkScreen((b) -> {
                if (b) {
                    Util.getPlatform().openUri("https://www.patreon.com/owmii");
                }
                MC.get().setScreen(screen);
            }, "https://www.patreon.com/owmii", true));
        }, screen).setTooltipSupplier(() -> List.of(Component.literal("Support me on Patreon! <3"))));
    }

    @Override
    public void render(GuiGraphics gui, int x, int y, int mx, int my, float pt, Font font, WikiScreen screen) {
        var globalStack = RenderSystem.getModelViewStack();
        globalStack.pushPose();
        globalStack.translate(x, y, 0.0F);
        globalStack.scale(2.0F, 2.0F, 1.0F);
        String s = getWiki().getModName();
        gui.drawString(font, s, Math.round((161 / 4.0F) - font.width(s) / 2.0F), 10, 0x444444, false);
        globalStack.popPose();

        globalStack.pushPose();
        String s2 = "v" + getWiki().getModVersion();
        gui.drawString(font, s2, Math.round(x + 161 / 2.0F - font.width(s2) / 2.0F), y + 45, 0x999999, false);

        if (screen.mc.player != null) {
            String s3 = I18n.get("wiki.lollipop.welcome_back", ":)");
            gui.drawString(font, s3, Math.round(x + 161 / 2.0F - font.width(s3) / 2.0F), y + 100, 0x777777, false);
            String s4 = screen.mc.player.getName().getString();
            gui.drawString(font, s4, Math.round(x + 161 / 2.0F - font.width(s4) / 2.0F), y + 115, 0x777777, false);
        }
        globalStack.popPose();
        RenderSystem.applyModelViewMatrix();
    }
}
