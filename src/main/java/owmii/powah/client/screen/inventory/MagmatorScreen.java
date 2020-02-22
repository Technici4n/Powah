package owmii.powah.client.screen.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import owmii.lib.Lollipop;
import owmii.lib.client.screen.EnergyProviderScreenBase;
import owmii.lib.client.screen.widget.Gauge;
import owmii.lib.client.util.Draw;
import owmii.lib.util.Safe;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.magmator.MagmatorTile;
import owmii.powah.inventory.MagmatorContainer;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class MagmatorScreen extends EnergyProviderScreenBase<MagmatorTile, MagmatorContainer> {
    private static final ResourceLocation GUI_MACHINE = new ResourceLocation(Lollipop.MOD_ID, "textures/gui/container/blank_right_gauge.png");
    private Gauge buffer = GAUGE;

    public MagmatorScreen(MagmatorContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
    }

    @Override
    public void init(Minecraft mc, int w, int h) {
        super.init(mc, w, h);
        this.buffer = gauge(this.x + 82, this.y + 27, 11, 18, 13, 0, GUI_BUFFER)
                .setBG(this.x + 81, this.y + 26, 13, 20, 0, 0);
    }

    @Override
    protected void configButtons(int x, int y) {
        super.configButtons(x - 19, y);
    }

    @Override
    protected void refreshScreen() {
        super.refreshScreen();
        this.buffer.visible = !this.configVisible;
        if (this.buffer.isHovered()) {
            int percent = Safe.integer(this.te.getBuffer() > 0 ? (100 * this.te.getNextBuff()) / this.te.getBuffer() : 0);
            this.buffer.clearToolTip().tooltip("info.lollipop.buffer", TextFormatting.GRAY, "" + TextFormatting.DARK_GRAY + percent + "%");
        }
    }

    @Override
    protected void drawBackground(float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(partialTicks, mouseX, mouseY);
        this.buffer.render(this.te.getBuffer(), this.te.getNextBuff(), mouseX, mouseY);
        FluidTank tank = this.te.getTank();
        if (!tank.isEmpty()) {
            FluidStack fluidStack = tank.getFluid();
            FluidAttributes fa = fluidStack.getFluid().getAttributes();
            ResourceLocation still = fa.getStillTexture(fluidStack);
            if (still != null) {
                int color = fa.getColor(fluidStack);
                float red = (color >> 16 & 0xFF) / 255.0F;
                float green = (color >> 8 & 0xFF) / 255.0F;
                float blue = (color & 0xFF) / 255.0F;
                RenderSystem.color3f(red, green, blue);
                TextureAtlasSprite sprite = this.mc.getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(still);
                bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
                Draw.gaugeV(sprite, this.x + 162, this.y + 4, 10, 64, tank.getCapacity(), tank.getFluidAmount());
                RenderSystem.color3f(1.0F, 1.0F, 1.0F);
            }
        }
    }

    @Override
    protected void renderHoveredToolTip(int mouseX, int mouseY) {
        if (!this.buffer.renderToolTip(mouseX, mouseY)) {
            super.renderHoveredToolTip(mouseX, mouseY);
        }
        FluidTank tank = this.te.getTank();
        if (isMouseOver(mouseX - 161, mouseY - 3, 12, 66)) {
            List<String> list = new ArrayList<>();
            if (!tank.isEmpty()) {
                list.add(TextFormatting.GRAY + I18n.format("info.lollipop.fluid", TextFormatting.GOLD + tank.getFluid().getDisplayName().getString()));
                list.add(TextFormatting.GRAY + I18n.format("info.lollipop.fluid.stored", "" + TextFormatting.DARK_GRAY + tank.getFluidAmount(), tank.getCapacity()));
                list.add(TextFormatting.GRAY + I18n.format("info.lollipop.energy.per.mb", "" + TextFormatting.DARK_GRAY + PowahAPI.getMagmaticFluidHeat(tank.getFluid().getFluid()), 100));
            } else {
                list.add(TextFormatting.GRAY + I18n.format("info.lollipop.fluid", TextFormatting.DARK_GRAY + "----"));
            }
            renderTooltip(list, mouseX, mouseY);
        }
    }

    @Override
    protected ResourceLocation getMachineBackGround() {
        return GUI_MACHINE;
    }

    @Override
    protected ResourceLocation getConfigBackGround() {
        return GUI_MACHINE;
    }
}
