package owmii.powah.client.screen.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
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
import owmii.lib.client.screen.EnergyScreen;
import owmii.lib.client.util.Draw;
import owmii.lib.util.Ticker;
import owmii.powah.Powah;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.reactor.ReactorTile;
import owmii.powah.inventory.ReactorContainer;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ReactorScreen extends EnergyScreen<ReactorTile, ReactorContainer> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/gui/container/reactor.png");
    private static final ResourceLocation GUI_MACHINE = new ResourceLocation(Lollipop.MOD_ID, "textures/gui/container/blank_right_gauge.png");

    public ReactorScreen(ReactorContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
    }

    @Override
    protected void configButtons(int x, int y) {
        super.configButtons(x - 19, y);
    }

    @Override
    protected void drawBackground(float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(partialTicks, mouseX, mouseY);
        ReactorTile reactor = this.container.getTile();
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
        if (!this.configVisible) {
            bindTexture(GUI_TEXTURE);

            Ticker fuel = reactor.getFuel();
            if (!fuel.isEmpty()) {
                Draw.gaugeV(this.x + 96, this.y + 12, 5, 48, 176, 16, fuel.getMax(), fuel.getTicks());
            }

            Ticker carbon = reactor.getCarbon();
            if (!carbon.isEmpty()) {
                Draw.gaugeV(this.x + 43, this.y + 5, 5, 16, 176, 0, carbon.getMax(), carbon.getTicks());
            }

            Ticker redstone = reactor.getRedstone();
            if (!redstone.isEmpty()) {
                Draw.gaugeV(this.x + 43, this.y + 51, 5, 16, 181, 0, redstone.getMax(), redstone.getTicks());
            }

            Ticker solidCoolant = reactor.getSolidCoolant();
            if (!solidCoolant.isEmpty()) {
                Draw.gaugeV(this.x + 147, this.y + 51, 5, 16, 186, 0, solidCoolant.getMax(), solidCoolant.getTicks());
            }

            Ticker temp = reactor.getTemp();
            if (!temp.isEmpty()) {
                Draw.gaugeV(this.x + 107, this.y + 27, 4, 18, 191, 0, temp.getMax(), temp.getTicks());
            }
        }
    }

    @Override
    protected void renderHoveredToolTip(int mouseX, int mouseY) {
        super.renderHoveredToolTip(mouseX, mouseY);
        FluidTank tank = this.te.getTank();
        if (isMouseOver(mouseX - 161, mouseY - 3, 12, 66)) {
            List<String> list = new ArrayList<>();
            if (!tank.isEmpty()) {
                list.add(TextFormatting.GRAY + I18n.format("info.lollipop.coolant", TextFormatting.AQUA + tank.getFluid().getDisplayName().getString()));
                list.add(TextFormatting.GRAY + I18n.format("info.lollipop.fluid.stored", "" + TextFormatting.DARK_GRAY + tank.getFluidAmount(), tank.getCapacity()));
                list.add(TextFormatting.GRAY + I18n.format("info.lollipop.temperature", "" + TextFormatting.AQUA + PowahAPI.getCoolant(tank.getFluid().getFluid()), 100));
            } else {
                list.add(TextFormatting.GRAY + I18n.format("info.lollipop.coolant", TextFormatting.DARK_GRAY + "----"));
            }
            renderTooltip(list, mouseX, mouseY);
        }
        if (!this.configVisible) {

            Ticker temp = this.te.getTemp();
            if (isMouseOver(mouseX - 106, mouseY - 26, 6, 20)) {
                List<String> list = new ArrayList<>();
                list.add((TextFormatting.GRAY + String.format("%.1f", temp.getTicks()) + " C"));
                renderTooltip(list, mouseX, mouseY);
            }

            Ticker fuel = this.te.getFuel();
            if (isMouseOver(mouseX - 95, mouseY - 11, 7, 50)) {
                boolean b = fuel.isEmpty();
                List<String> list = new ArrayList<>();
                list.add(TextFormatting.GREEN + I18n.format("item.powah.uraninite"));
                list.add(TextFormatting.GRAY + I18n.format("info.lollipop.fluid.stored", TextFormatting.DARK_GRAY + String.format("%.0f", fuel.getTicks()), String.format("%.0f", fuel.getMax())));
                list.add(TextFormatting.GRAY + I18n.format("info.lollipop.fluid.using.per.tick", TextFormatting.GREEN + String.format("%.4f", this.te.calcConsumption())));
                renderTooltip(list, mouseX, mouseY);
            }

            Ticker carbon = this.te.getCarbon();
            if (isMouseOver(mouseX - 42, mouseY - 4, 7, 18)) {
                List<String> list = new ArrayList<>();
                boolean b = carbon.isEmpty();
                list.add((TextFormatting.GRAY + I18n.format("info.powah.carbon")));
                list.add(TextFormatting.GRAY + I18n.format("info.lollipop.fluid.stored", TextFormatting.DARK_GRAY + String.format("%.1f", carbon.getTicks()), String.format("%.1f", carbon.getMax())));
                list.add("");
                list.add(TextFormatting.DARK_GRAY + "+" + I18n.format("enchantment.minecraft.efficiency"));
                list.add(TextFormatting.DARK_RED + (b ? "+0 C" : "+180 C"));
                renderTooltip(list, mouseX, mouseY);
            }

            Ticker redstone = this.te.getRedstone();
            if (isMouseOver(mouseX - 42, mouseY - 50, 7, 18)) {
                List<String> list = new ArrayList<>();
                boolean b = redstone.isEmpty();
                list.add(TextFormatting.RED + I18n.format("info.powah.redstone"));
                list.add(TextFormatting.GRAY + I18n.format("info.lollipop.fluid.stored", TextFormatting.DARK_GRAY + String.format("%.1f", redstone.getTicks()), String.format("%.1f", redstone.getMax())));
                list.add("");
                list.add(TextFormatting.DARK_GRAY + "+" + I18n.format("info.powah.production"));
                list.add(TextFormatting.DARK_GRAY + "+" + I18n.format("info.powah.fuel.consumption"));
                list.add(TextFormatting.DARK_RED + (b ? "+0 C" : "+120 C"));
                renderTooltip(list, mouseX, mouseY);
            }

            Ticker solidCoolant = this.te.getSolidCoolant();
            if (isMouseOver(mouseX - 146, mouseY - 50, 7, 18)) {
                List<String> list = new ArrayList<>();
                list.add(TextFormatting.GRAY + I18n.format("info.powah.solid.coolant"));
                list.add(TextFormatting.GRAY + I18n.format("info.lollipop.fluid.stored", TextFormatting.DARK_GRAY + String.format("%.1f", solidCoolant.getTicks()), String.format("%.1f", solidCoolant.getMax())));
                list.add("" + TextFormatting.AQUA + this.te.getSolidCoolantTemp() + " C");
                renderTooltip(list, mouseX, mouseY);
            }
        }
    }

    @Override
    protected ResourceLocation getMachineBackGround() {
        return GUI_TEXTURE;
    }

    @Override
    protected ResourceLocation getConfigBackGround() {
        return GUI_MACHINE;
    }
}
