package owmii.powah.client.screen.inventory;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import owmii.lib.client.util.Draw2D;
import owmii.lib.client.util.GL;
import owmii.lib.util.Text;
import owmii.lib.util.Ticker;
import owmii.powah.Powah;
import owmii.powah.api.PowahAPI;
import owmii.powah.block.generator.GeneratorTile;
import owmii.powah.block.generator.reactor.ReactorTile;
import owmii.powah.energy.PowahStorage;
import owmii.powah.inventory.ReactorContainer;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ReactorScreen extends PowahScreen<ReactorContainer> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/gui/container/reactor.png");
    private static final ResourceLocation GUI_CONFIG_TEXTURE = new ResourceLocation(Powah.MOD_ID, "textures/gui/container/configuration_reactor.png");

    public ReactorScreen(ReactorContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
    }

    @Override
    protected void addMainButtons(int x, int y, int space) {
        super.addMainButtons(x - 18, y - 1, space);
    }

    @Override
    protected void addSideConfig(int x, int y, int space) {
        super.addSideConfig(x - 18, y, space);
    }

    @Override
    protected void drawBackground(float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(partialTicks, mouseX, mouseY);
        ReactorTile reactor = this.container.getTile();
        FluidTank tank = reactor.getTank();
        if (!tank.isEmpty()) {
            FluidStack fluidStack = tank.getFluid();
            Fluid fluid = fluidStack.getFluid();
            AtlasTexture textureMap = Minecraft.getInstance().getTextureMap();
            FluidAttributes fa = fluid.getAttributes();
            ResourceLocation still = fa.getStill(fluidStack);
            if (still != null) {
                int color = fa.getColor(fluidStack);
                GL.color(color);
                TextureAtlasSprite sprite = textureMap.getSprite(still);
                bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
                Draw2D.gaugeV(sprite, this.x + 162, this.y + 4, 10, 64, tank.getCapacity(), tank.getFluidAmount());
                GlStateManager.color3f(1.0F, 1.0F, 1.0F);
            }
        }
        if (!this.sideButtons[0].visible) {
            bindTexture(GUI_TEXTURE);

            Ticker fuel = reactor.getFuel();
            if (!fuel.isEmpty()) {
                Draw2D.gaugeV(this.x + 101, this.y + 12, 5, 48, 153, 16, fuel.getMax(), fuel.getTicks());
            }

            Ticker carbon = reactor.getCarbon();
            if (!carbon.isEmpty()) {
                Draw2D.gaugeV(this.x + 48, this.y + 5, 5, 16, 153, 0, carbon.getMax(), carbon.getTicks());
            }

            Ticker redstone = reactor.getRedstone();
            if (!redstone.isEmpty()) {
                Draw2D.gaugeV(this.x + 48, this.y + 51, 5, 16, 158, 0, redstone.getMax(), redstone.getTicks());
            }

            Ticker solidCoolant = reactor.getSolidCoolant();
            if (!solidCoolant.isEmpty()) {
                Draw2D.gaugeV(this.x + 147, this.y + 51, 5, 16, 163, 0, solidCoolant.getMax(), solidCoolant.getTicks());
            }

            Ticker temp = reactor.getTemp();
            if (!temp.isEmpty()) {
                Draw2D.gaugeV(this.x + 112, this.y + 27, 4, 18, 168, 0, temp.getMax(), temp.getTicks());
            }
        }
    }

    @Override
    protected void renderHoveredToolTip(int mouseX, int mouseY) {
        super.renderHoveredToolTip(mouseX, mouseY);
        if (this.sideButtons[0].visible) return;
        ReactorTile reactor = this.container.getTile();
        FluidTank tank = reactor.getTank();
        if (isMouseOver(mouseX - 161, mouseY - 3, 12, 66)) {
            boolean b = tank.isEmpty();
            PowahStorage storage = this.tile.getInternal();
            List<String> list = new ArrayList<>();
            list.add(TextFormatting.GRAY + I18n.format("info.powah.coolant", b ? TextFormatting.DARK_GRAY + I18n.format("info.powah.empty") : TextFormatting.AQUA + tank.getFluid().getDisplayName().getString()));
            if (!b) {
                list.add(TextFormatting.GRAY + I18n.format("info.powah.stored.fluid", "" + TextFormatting.DARK_GRAY + tank.getFluidAmount(), tank.getCapacity()));
                int perSeq = PowahAPI.getReactorCoolant(tank.getFluid().getFluid());
                list.add(TextFormatting.GRAY + I18n.format("info.powah.coldness", "" + TextFormatting.AQUA + perSeq));
            }
            renderTooltip(list, mouseX, mouseY);
        }

        Ticker temp = reactor.getTemp();
        if (isMouseOver(mouseX - 111, mouseY - 26, 6, 20)) {
            List<String> list = new ArrayList<>();
            list.add((TextFormatting.GRAY + String.format("%.1f", temp.getTicks()) + " C"));
            renderTooltip(list, mouseX, mouseY);
        }

        Ticker fuel = reactor.getFuel();
        if (isMouseOver(mouseX - 100, mouseY - 11, 7, 50)) {
            boolean b = fuel.isEmpty();
            List<String> list = new ArrayList<>();
            list.add(TextFormatting.GREEN + I18n.format("item.powah.uraninite"));
            list.add(TextFormatting.GRAY + I18n.format("info.powah.stored.fluid", TextFormatting.DARK_GRAY + String.format("%.0f", fuel.getTicks()), String.format("%.0f", fuel.getMax())));
            list.add(TextFormatting.GRAY + I18n.format("info.powah.fluid.using", TextFormatting.GREEN + String.format("%.4f", reactor.calcConsumption())));
            renderTooltip(list, mouseX, mouseY);
        }

        Ticker carbon = reactor.getCarbon();
        if (isMouseOver(mouseX - 47, mouseY - 4, 7, 18)) {
            List<String> list = new ArrayList<>();
            boolean b = carbon.isEmpty();
            list.add((TextFormatting.GRAY + I18n.format("info.powah.carbon")));
            list.add(TextFormatting.GRAY + I18n.format("info.powah.stored.fluid", TextFormatting.DARK_GRAY + String.format("%.1f", carbon.getTicks()), String.format("%.1f", carbon.getMax())));
            list.add("");
            list.add(TextFormatting.DARK_GRAY + "+" + I18n.format("info.powah.efficiency"));
            list.add(TextFormatting.DARK_RED + (b ? "+0 C" : "+180 C"));
            renderTooltip(list, mouseX, mouseY);
        }

        Ticker redstone = reactor.getRedstone();
        if (isMouseOver(mouseX - 47, mouseY - 50, 7, 18)) {
            List<String> list = new ArrayList<>();
            boolean b = redstone.isEmpty();
            list.add(TextFormatting.RED + I18n.format("info.powah.redstone"));
            list.add(TextFormatting.GRAY + I18n.format("info.powah.stored.fluid", TextFormatting.DARK_GRAY + String.format("%.1f", redstone.getTicks()), String.format("%.1f", redstone.getMax())));
            list.add("");
            list.add(TextFormatting.DARK_GRAY + "+" + I18n.format("info.powah.production"));
            list.add(TextFormatting.DARK_GRAY + "+" + I18n.format("info.powah.fuel.consumption"));
            list.add(TextFormatting.DARK_RED + (b ? "+0 C" : "+120 C"));
            renderTooltip(list, mouseX, mouseY);
        }

        Ticker solidCoolant = reactor.getSolidCoolant();
        if (isMouseOver(mouseX - 146, mouseY - 50, 7, 18)) {
            List<String> list = new ArrayList<>();
            list.add(TextFormatting.GRAY + I18n.format("info.powah.solid.coolant"));
            list.add(TextFormatting.GRAY + I18n.format("info.powah.stored.fluid", TextFormatting.DARK_GRAY + String.format("%.1f", solidCoolant.getTicks()), String.format("%.1f", solidCoolant.getMax())));
            list.add("" + TextFormatting.AQUA + reactor.getSolidCoolantTemp() + " C");
            renderTooltip(list, mouseX, mouseY);
        }
    }

    @Override
    protected void renderEnergyTooltip(int mouseX, int mouseY, int cap, int stored, int out, int in) {
        if (isMouseOver(mouseX - 3, mouseY - 3, 16, 66)) {
            ReactorTile reactor = this.container.getTile();
            List<String> list = new ArrayList<>();
            list.add(TextFormatting.GRAY + getTitle().getString());
            list.add(" " + TextFormatting.GRAY + I18n.format("info.powah.stored", TextFormatting.DARK_GRAY + Text.addCommas(stored), Text.numFormat(cap)));
            if (this.tile instanceof GeneratorTile)
                list.add(" " + TextFormatting.GRAY + I18n.format("info.powah.generates", TextFormatting.DARK_GRAY + Text.numFormat((long) reactor.calcProduction())));
            list.add(" " + TextFormatting.GRAY + I18n.format("info.powah.max.io", TextFormatting.DARK_GRAY +
                    (in == out ? Text.numFormat(out) : in == 0 || out == 0 ? Text.numFormat(Math.max(in, out)) : (Text.numFormat(in) + "/" + Text.numFormat(out)))));
            renderTooltip(list, mouseX, mouseY);
        }
    }

    @Override
    protected ResourceLocation getSubBackGroundImage() {
        return GUI_TEXTURE;
    }

    @Override
    protected ResourceLocation getConfigBackGroundImage() {
        return GUI_CONFIG_TEXTURE;
    }
}
