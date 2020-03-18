package owmii.powah.command.impl;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;
import owmii.powah.compat.crafttweaker.CrafttweakerCompat;

public class CraftTweakerFileGen {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("generate_crafttweaker_example")
                .requires(cs -> cs.hasPermissionLevel(2)).executes(context -> {
                    CrafttweakerCompat.setup();
                    context.getSource().asPlayer().sendMessage(new TranslationTextComponent("Generated at crafttweaker scripts folder!"));
                    return 0;
                });
    }
}
