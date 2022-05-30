package owmii.lib.client.screen.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import owmii.lib.client.screen.Texture;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class IconButton extends Button {
    protected final Minecraft mc = Minecraft.getInstance();
    private Consumer<List<ITextComponent>> tooltipConsumer = stringList -> {
    };
    private Screen screen;
    private Texture texture;
    private Texture hovering;
    private ItemStack stack;
    private float xOffset;
    private float yOffset;

    @Nullable
    private SoundEvent sound;

    public IconButton(int x, int y, ITextComponent text, Texture texture, IPressable onPress, Screen screen) {
        this(x, y, ItemStack.EMPTY, texture, Texture.EMPTY, text, onPress, screen);
    }

    public IconButton(int x, int y, ITextComponent text, Texture texture, IPressable onPress, Texture hovering, Screen screen) {
        this(x, y, ItemStack.EMPTY, texture, hovering, text, onPress, screen);
    }

    public IconButton(int x, int y, Texture texture, IPressable onPress, Screen screen) {
        this(x, y, ItemStack.EMPTY, texture, Texture.EMPTY, new StringTextComponent(""), onPress, screen);
    }

    public IconButton(int x, int y, Texture texture, Texture hovering, IPressable onPress, Screen screen) {
        this(x, y, ItemStack.EMPTY, texture, hovering, new StringTextComponent(""), onPress, screen);
    }

    public IconButton(int x, int y, ItemStack stack, Texture texture, IPressable onPress, Screen screen) {
        this(x, y, stack, texture, Texture.EMPTY, new StringTextComponent(""), onPress, screen);
    }

    public IconButton(int x, int y, ItemStack stack, Texture texture, Texture hovering, IPressable onPress, Screen screen) {
        this(x, y, stack, texture, hovering, new StringTextComponent(""), onPress, screen);
    }

    public IconButton(int x, int y, ItemStack stack, Texture texture, Texture hovering, ITextComponent text, IPressable onPress, Screen screen) {
        super(x, y, texture.getWidth(), texture.getHeight(), text, onPress);
        this.texture = texture;
        this.screen = screen;
        this.hovering = hovering;
        this.stack = stack;
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float pt) {
        if (this.visible) {
            this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            if (this.isHovered && !this.hovering.isEmpty()) {
                this.hovering.draw(matrix, this.x, this.y);
            } else {
                this.texture.draw(matrix, this.x, this.y);
            }
            FontRenderer f = this.mc.fontRenderer;
            String s = getMessage().getString();
            if (!s.isEmpty()) {
                int width = f.getStringWidth(s);
                Color c = getMessage().getStyle().getColor();
                int color = c == null ? 0x555555 : c.getColor();
                f.drawString(matrix, s, this.xOffset + this.x + 0.5F + this.width / 2.0F - width / 2.0F, this.yOffset + this.y + this.height / 2.0F - 4, color);
            }
            if (!this.stack.isEmpty()) {
                RenderSystem.pushMatrix();
                Minecraft mc = Minecraft.getInstance();
                RenderSystem.translated(this.xOffset + this.x - 8.0D + this.width / 2.0F, this.yOffset + this.y - 8.0D + this.height / 2.0F, 0.0F);
                mc.getItemRenderer().renderItemAndEffectIntoGUI(this.stack, 0, 0);
                RenderSystem.popMatrix();
            }
        }
    }

    @Override
    public void renderToolTip(MatrixStack matrix, int mouseX, int mouseY) {
        List<ITextComponent> tooltip = new ArrayList<>();
        this.tooltipConsumer.accept(tooltip);
        if (!tooltip.isEmpty()) {
            this.screen.func_243308_b(matrix, tooltip, mouseX, mouseY);
        }
    }

    public void blit(MatrixStack matrix, Texture texture, int x, int y) {
        bindTexture(texture.getLocation());
        blit(matrix, x, y, texture.getU(), texture.getV(), texture.getWidth(), texture.getHeight());
    }

    public void bindTexture(ResourceLocation guiTexture) {
        this.mc.getTextureManager().bindTexture(guiTexture);
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

    public Consumer<List<ITextComponent>> getTooltip() {
        return this.tooltipConsumer;
    }

    public IconButton setTooltip(Consumer<List<ITextComponent>> tooltipConsumer) {
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
    public void playDownSound(SoundHandler handler) {
        if (this.sound != null) {
            handler.play(SimpleSound.master(this.sound, 1.0F));
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
