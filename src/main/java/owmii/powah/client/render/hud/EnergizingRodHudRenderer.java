package owmii.powah.client.render.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;
import owmii.powah.block.energizing.EnergizingRodBlockEntity;
import owmii.powah.lib.client.util.Draw;
import owmii.powah.util.Util;

public class EnergizingRodHudRenderer implements BlockHudRenderer {
    @Override
    public boolean renderHud(GuiGraphicsExtractor gui, BlockState state, Level world, BlockPos pos, Player player, BlockHitResult result,
            @Nullable BlockEntity te) {
        if (te instanceof EnergizingRodBlockEntity rod) {
            RenderSystem.getModelViewStack().pushMatrix();
            Minecraft mc = Minecraft.getInstance();
            Font font = mc.font;
            int x = mc.getWindow().getGuiScaledWidth() / 2;
            int y = mc.getWindow().getGuiScaledHeight();
            String s = ChatFormatting.GRAY + I18n.get("info.lollipop.stored") + ": " + I18n.get("info.lollipop.fe.stored",
                    Util.addCommas(rod.getEnergy().getEnergyStored()), Util.numFormat(rod.getEnergy().getCapacity()));

            Identifier energyOverlay = Identifier.fromNamespaceAndPath("lollipop", "textures/gui/ov_energy.png");
            gui.blit(
                    RenderPipelines.GUI_TEXTURED,
                    energyOverlay,
                    x - 37 - 1, y - 80,
                    0, 0,
                    74, 9,
                    256, 256);
            Draw.gaugeH(gui, energyOverlay, x - 37, y - 79, 72, 16, 0, 9, rod.getEnergy());
            gui.text(font, s, Math.round(x - (font.width(s) / 2.0f)), y - 67, 0xffffffff);
            RenderSystem.getModelViewStack().popMatrix();
        }
        return true;
    }
}
