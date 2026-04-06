package owmii.powah.lib.client.screen.container;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.fluids.FluidStack;
import owmii.powah.client.ClientUtils;
import owmii.powah.lib.block.IInventoryHolder;
import owmii.powah.lib.block.PowahBaseBlockEntity;
import owmii.powah.lib.client.screen.Texture;
import owmii.powah.lib.client.screen.widget.IconButton;
import owmii.powah.lib.client.util.Draw;
import owmii.powah.lib.client.util.RenderTypes;
import owmii.powah.lib.client.util.Text;
import owmii.powah.lib.logistics.fluid.Tank;
import owmii.powah.lib.logistics.inventory.BaseBlockEntityMenu;
import owmii.powah.network.Network;
import owmii.powah.network.packet.NextRedstoneModePacket;
import owmii.powah.util.Util;

public class PowahBaseBlockEntityScreen<T extends PowahBaseBlockEntity<?> & IInventoryHolder, C extends BaseBlockEntityMenu<T>>
        extends PowahBaseContainerScreen<C> {
    protected final T te;
    protected IconButton redStoneButton = IconButton.EMPTY;
    protected final List<TankArea> tankAreas = new ArrayList<>();

    public PowahBaseBlockEntityScreen(C container, Inventory inv, Component title, Texture backGround) {
        super(container, inv, title, backGround);
        this.te = container.blockEntity;
    }

    protected void extractTooltip(GuiGraphicsExtractor gui, int x, int y) {
        for (var tankArea : tankAreas) {
            if (tankArea.contains(x - leftPos, y - topPos)) {
                var tank = tankArea.tank().get();

                List<Component> list = new ArrayList<>();
                if (!tank.isEmpty()) {
                    list.add(Component.translatable(tankArea.purposeTranslationKey()).withStyle(ChatFormatting.GRAY).append(Text.COLON)
                            .append(tank.getFluid().getHoverName().plainCopy().withStyle(ChatFormatting.AQUA)));
                    list.add(Component.translatable("info.lollipop.stored").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                            .append(Util.formatTankContent(tank)));
                    tankArea.extraInfoSupplier().accept(tank.getFluid(), list);
                } else {
                    list.add(Component.translatable("info.lollipop.fluid").withStyle(ChatFormatting.GRAY).append(Text.COLON)
                            .append(Component.literal("---").withStyle(ChatFormatting.DARK_GRAY)));
                }
                gui.setComponentTooltipForNextFrame(font, list, x, y);
                return;
            }
        }

        super.extractTooltip(gui, x, y);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event.button() == 0 || event.button() == 1) {
            for (var tankArea : tankAreas) {
                if (tankArea.contains(event.x() - leftPos, event.y() - topPos)) {
                    menu.interactWithTank(event.button() == 1);
                    return true;
                }
            }
        }

        return super.mouseClicked(event, doubleClick);
    }

    protected record TankArea(Supplier<Tank> tank, int x, int y, int width, int height, String purposeTranslationKey,
            BiConsumer<FluidStack, List<Component>> extraInfoSupplier) {
        boolean contains(double x, double y) {
            return x >= this.x && y >= this.y && x < this.x + width && y < this.y + height;
        }
    }

    protected final void addTankArea(Supplier<Tank> tank, int x, int y, int width, int height, String purposeTranslationKey,
            BiConsumer<FluidStack, List<Component>> extraInfoSupplier) {
        tankAreas.add(new TankArea(tank, x, y, width, height, purposeTranslationKey, extraInfoSupplier));
    }

    protected void addRedstoneButton(int x, int y) {
        if (hasRedstone()) {
            this.redStoneButton = addRenderableWidget(new IconButton(this.leftPos + this.imageWidth + x + 2, this.topPos + y + 3,
                    Texture.REDSTONE.get(this.te.getRedstoneMode()), b -> {
                        Network.toServer(new NextRedstoneModePacket(this.te.getBlockPos()));
                        this.te.setRedstoneMode(this.te.getRedstoneMode().next());
                    }).setTooltipSupplier(() -> List.of(this.te.getRedstoneMode().getDisplayName())));
        }
    }

    @Override
    public void containerTick() {
        super.containerTick();
        if (hasRedstone()) {
            this.redStoneButton.setTexture(Texture.REDSTONE.get(this.te.getRedstoneMode()));
        }
    }

    @Override
    protected void drawBackground(GuiGraphicsExtractor guiGraphics, float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(guiGraphics, partialTicks, mouseX, mouseY);
        if (hasRedstone()) {
            Texture.REDSTONE_BTN_BG.draw(guiGraphics, this.redStoneButton.getX() - 2, this.redStoneButton.getY() - 4);
        }

        for (TankArea tankArea : tankAreas) {
            var tank = tankArea.tank().get();
            if (!tank.isEmpty()) {
                var fluidStack = tank.getFluid();
                var sprite = ClientUtils.getStillTexture(fluidStack);
                int color = ClientUtils.getFluidColor(fluidStack);
                Draw.gaugeV(guiGraphics,
                        RenderTypes.GUI_TEXTURED_NOBLEND,
                        sprite, this.leftPos + tankArea.x, this.topPos + tankArea.y, tankArea.width, tankArea.height(),
                        tank.getCapacity(),
                        tank.getFluidAmount(), ARGB.opaque(color));
            }
        }
    }

    protected boolean hasRedstone() { // TODO invert
        return true;
    }
}
