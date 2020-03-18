package owmii.powah.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import owmii.powah.Powah;
import owmii.powah.command.impl.CraftTweakerFileGen;

public class PowahCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal(Powah.MOD_ID)
                        .then(CraftTweakerFileGen.register())
        );
    }
}
