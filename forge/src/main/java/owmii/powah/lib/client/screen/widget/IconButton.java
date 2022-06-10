package owmii.powah.lib.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import owmii.powah.lib.client.screen.Texture;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class IconButton extends Button {
    protected final Minecraft mc = Minecraft.getInstance();
    private Consumer<List<Component>> tooltipConsumer = stringList -> {
    };
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
        this(x, y, ItemStack.EMPTY, texture, Texture.EMPTY, new TextComponent(""), onPress, screen);
    }

    public IconButton(int x, int y, Texture texture, Texture hovering, OnPress onPress, Screen screen) {
        this(x, y, ItemStack.EMPTY, texture, hovering, new TextComponent(""), onPress, screen);
    }

    public IconButton(int x, int y, ItemStack stack, Texture texture, OnPress onPress, Screen screen) {
        this(x, y, stack, texture, Texture.EMPTY, new TextComponent(""), onPress, screen);
    }

    public IconButton(int x, int y, ItemStack stack, Texture texture, Texture hovering, OnPress onPress, Screen screen) {
        this(x, y, stack, texture, hovering, new TextComponent(""), onPress, screen);
    }

    public IconButton(int x, int y, ItemStack stack, Texture texture, Texture hovering, Component text, OnPress onPress, Screen screen) {
        super(x, y, texture.getWidth(), texture.getHeight(), text, onPress);
        this.texture = texture;
        this.screen = screen;
        this.hovering = hovering;
        this.stack = stack;
    }

    @Override
    public void render(PoseStack matrix, int mouseX, int mouseY, float pt) {
        if (this.visible) {
            this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            if (this.isHovered && !this.hovering.isEmpty()) {
                this.hovering.draw(matrix, this.x, this.y);
            } else {
                this.texture.draw(matrix, this.x, this.y);
            }
            Font f = this.mc.font;
            String s = getMessage().getString();
            if (!s.isEmpty()) {
                int width = f.width(s);
                TextColor c = getMessage().getStyle().getColor();
                int color = c == null ? 0x555555 : c.getValue();
                f.draw(matrix, s, this.xOffset + this.x + 0.5F + this.width / 2.0F - width / 2.0F, this.yOffset + this.y + this.height / 2.0F - 4, color);
            }
            if (!this.stack.isEmpty()) {
                var globalStack = RenderSystem.getModelViewStack();
                globalStack.pushPose();
                Minecraft mc = Minecraft.getInstance();
                globalStack.translate(this.xOffset + this.x - 8.0D + this.width / 2.0F, this.yOffset + this.y - 8.0D + this.height / 2.0F, 0.0F);
                mc.getItemRenderer().renderAndDecorateItem(this.stack, 0, 0);
                globalStack.popPose();
                RenderSystem.applyModelViewMatrix();
            }
        }
    }

    @Override
    public void renderToolTip(PoseStack matrix, int mouseX, int mouseY) {
        List<Component> tooltip = new ArrayList<>();
        this.tooltipConsumer.accept(tooltip);
        if (!tooltip.isEmpty()) {
            this.screen.renderComponentTooltip(matrix, tooltip, mouseX, mouseY);
        }
    }

    public void blit(PoseStack matrix, Texture texture, int x, int y) {
        bindTexture(texture.getLocation());
        blit(matrix, x, y, texture.getU(), texture.getV(), texture.getWidth(), texture.getHeight());
    }

    public void bindTexture(ResourceLocation guiTexture) {
        RenderSystem.setShaderTexture(0, guiTexture);
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

    public Consumer<List<Component>> getTooltip() {
        return this.tooltipConsumer;
    }

    public IconButton setTooltip(Consumer<List<Component>> tooltipConsumer) {
        this.tooltipConsumer = tooltipConsumer;
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
        this.sound = SoundEvents.UI_BUTTON_CLICK;
        return this;
    }

    public IconButton setSound(@Nullable SoundEvent sound) {
        this.sound = sound;
        return this;
    }

    public static final IconButton EMPTY = new IconButton(0, 0, Texture.EMPTY, b -> {
    }, new ChatScreen(""));
}
