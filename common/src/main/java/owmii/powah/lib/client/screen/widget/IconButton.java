package owmii.powah.lib.client.screen.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import owmii.powah.lib.client.screen.Texture;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class IconButton extends Button {
    protected final Minecraft mc = Minecraft.getInstance();
    @Nullable
    private Supplier<List<Component>> tooltipSupplier;
    private Screen screen;
    private Texture texture;
    private Texture hovering;
    private ItemStack stack;
    private float xOffset;
    private float yOffset;

    @Nullable
    private SoundEvent sound;

    public IconButton(int x, int y, Component text, Texture texture, OnPress onPress, Screen screen) {
        this(x, y, ItemStack.EMPTY, texture, Texture.EMPTY, text, onPress, screen);
    }

    public IconButton(int x, int y, Component text, Texture texture, OnPress onPress, Texture hovering, Screen screen) {
        this(x, y, ItemStack.EMPTY, texture, hovering, text, onPress, screen);
    }

    public IconButton(int x, int y, Texture texture, OnPress onPress, Screen screen) {
        this(x, y, ItemStack.EMPTY, texture, Texture.EMPTY, Component.empty(), onPress, screen);
    }

    public IconButton(int x, int y, Texture texture, Texture hovering, OnPress onPress, Screen screen) {
        this(x, y, ItemStack.EMPTY, texture, hovering, Component.empty(), onPress, screen);
    }

    public IconButton(int x, int y, ItemStack stack, Texture texture, OnPress onPress, Screen screen) {
        this(x, y, stack, texture, Texture.EMPTY, Component.empty(), onPress, screen);
    }

    public IconButton(int x, int y, ItemStack stack, Texture texture, Texture hovering, OnPress onPress, Screen screen) {
        this(x, y, stack, texture, hovering, Component.empty(), onPress, screen);
    }

    public IconButton(int x, int y, ItemStack stack, Texture texture, Texture hovering, Component text, OnPress onPress, Screen screen) {
        super(x, y, texture.getWidth(), texture.getHeight(), text, onPress, DEFAULT_NARRATION);
        this.texture = texture;
        this.screen = screen;
        this.hovering = hovering;
        this.stack = stack;
    }

    @Override
    protected void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float pt) {
        if (isHovered) {
            setTooltip(createTooltip());
        }

        if (this.isHovered && !this.hovering.isEmpty()) {
            this.hovering.draw(gui, this.getX(), this.getY());
        } else {
            this.texture.draw(gui, this.getX(), this.getY());
        }
        Font f = this.mc.font;
        String s = getMessage().getString();
        if (!s.isEmpty()) {
            int width = f.width(s);
            TextColor c = getMessage().getStyle().getColor();
            int color = c == null ? 0x555555 : c.getValue();
            gui.drawString(f, s,
                    Math.round(this.xOffset + this.getX() + 0.5F + this.width / 2.0F - width / 2.0F),
                    Math.round(this.yOffset + this.getY() + this.height / 2.0F - 4),
                    color,
                    false
            );
        }
        if (!this.stack.isEmpty()) {
            int x = (int) Math.round(this.xOffset + this.getX() - 8.0D + this.width / 2.0F);
            int y = (int) Math.round(this.yOffset + this.getY() - 8.0D + this.height / 2.0F);
            gui.renderFakeItem(stack, x, y);
            gui.renderItemDecorations(f, stack, x, y);
        }
    }

    @Nullable
    private Tooltip createTooltip() {
        if (tooltipSupplier == null) {
            return null;
        }

        return tooltipSupplier.get().stream()
                .reduce(
                        (component, component2) -> component.copy()
                                .append("\n")
                                .append(component2)
                ).map(Tooltip::create).orElse(null);
    }

    public void blit(GuiGraphics gui, Texture texture, int x, int y) {
        gui.blit(texture.getLocation(), x, y, texture.getU(), texture.getV(), texture.getWidth(), texture.getHeight());
    }

    public Screen getScreen() {
        return this.screen;
    }

    public IconButton setScreen(Screen screen) {
        this.screen = screen;
        return this;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public IconButton setTexture(Texture texture) {
        this.texture = texture;
        return this;
    }

    public Texture getHovering() {
        return this.hovering;
    }

    public IconButton setHovering(Texture hovering) {
        this.hovering = hovering;
        return this;
    }

    public IconButton setTooltip(Component component) {
        super.setTooltip(Tooltip.create(component));
        return this;
    }

    public IconButton setTooltipSupplier(Supplier<List<Component>> supplier) {
        this.tooltipSupplier = supplier;
        return this;
    }

    public IconButton setStackInSlot(ItemStack stack) {
        this.stack = stack;
        return this;
    }

    public IconButton xOffset(float xOffset) {
        this.xOffset = xOffset;
        return this;
    }

    public IconButton yOffset(float yOffset) {
        this.yOffset = yOffset;
        return this;
    }

    @Override
    public void playDownSound(SoundManager handler) {
        if (this.sound != null) {
            handler.play(SimpleSoundInstance.forUI(this.sound, 1.0F));
        }
    }

    public IconButton setClickSound() {
        this.sound = SoundEvents.UI_BUTTON_CLICK.value();
        return this;
    }

    public IconButton setSound(@Nullable SoundEvent sound) {
        this.sound = sound;
        return this;
    }

    public static final IconButton EMPTY = new IconButton(0, 0, Texture.EMPTY, b -> {
    }, new ChatScreen(""));
}
